package ua.nure.romanenko.st4.dbcp;


import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Id;
import ua.nure.romanenko.st4.annotation.Table;
import ua.nure.romanenko.st4.dto.ApartmentType;
import ua.nure.romanenko.st4.dto.Dto;
import ua.nure.romanenko.st4.dto.Orders;
import ua.nure.romanenko.st4.dto.Users;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by denis on 11.09.17.
 */
public class Mutator extends Component {

    private static final Logger logger = Logger.getLogger(Mutator.class);

    public Dto write(Dto dto) throws SQLException {
        try (Connection con = getConnection()) {
            return write(con, dto);
        }
    }

    public void writeTransaction(Dto... dtos) throws SQLException {
        Connection con = getConnection();
        con.setAutoCommit(false);
        con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        try (Statement statement = con.createStatement()) {
            for (Dto dto : dtos) {
                writeDto(dto, statement);
            }
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw new SQLException(e); //// TODO: 12.09.17 log
        } finally {
            con.close();
        }
    }

    public void update(Dto object) throws SQLException {
        update(object, null);
    }

    public void update(Dto object, Dto filter) throws SQLException {
        try (Connection con = getConnection()) {
            update(con, object, filter);
        }
    }

    public void delete(Dto dto) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s ",
                dto.getClass().getAnnotation(Table.class).value(),
                buildFilter(dto));

        try (Connection con = getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            logger.error("can't execute sql : " + sql, e);
            throw new SQLException(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // static
    ///////////////////////////////////////////////////////////////////////////

    public static TransactionWriter getTransactionWriter() {
        return new Mutator().new TransactionWriter();
    }

    ///////////////////////////////////////////////////////////////////////////
    // private
    ///////////////////////////////////////////////////////////////////////////

    private void update(Connection con, Dto object, Dto filter) throws SQLException {
        String updateQuery = buildUpdateQuery(object, filter);
        logger.info("sql " + updateQuery);
        try (Statement statement = con.createStatement()) {
            statement.execute(updateQuery);
        }
    }

    private Dto write(Connection con, Dto dto) throws SQLException {
        try (Statement statement = con.createStatement()) {
            return writeDto(dto, statement);
        } catch (SQLException e) {
            throw new SQLException(e); //// TODO: 12.09.17 log
        }
    }

    private Dto writeDto(Dto dto, Statement statement) throws SQLException {
        String query = buildInsertQuery(dto);
        logger.debug("try execute : " + query);
        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                dto.setId(resultSet.getInt(1));
            }
        }
        return dto;
    }

    private String buildInsertQuery(Dto dto) {
        Class clazz = dto.getClass();
        StringBuilder names = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(dto);
                String nameField = field.getAnnotation(Column.class).value();
                String valueField = convertToString(value);
                Boolean isDefault = field.getAnnotation(Column.class).defaultValue();

                if (field.getAnnotation(Id.class) == null &&
                        (value != null || !isDefault)) {
                    names.append(String.format("%s, ", nameField));
                    values.append(String.format("%s, ", valueField));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace(); //// TODO: 24.09.17 log
            }
        }
        names.delete(names.length() - 2, names.length());
        values.delete(values.length() - 2, values.length());

        return String.format("INSERT INTO %s (%s) VALUES (%s) RETURNING %s",
                getTable(clazz),
                names,
                values,
                getIdName(clazz));
    }

    private String buildUpdateQuery(Dto dto, Dto filter) throws SQLException {
        String where = "";

        if (filter != null) {
            where = String.format("WHERE %s ", buildFilter(filter));
        } else if (dto.getId() != null) {
            where = String.format("WHERE %s = %s ", getIdName(dto.getClass()), dto.getId());
        }

        String query = String.format("UPDATE %s SET %s %s",
                getTable(dto.getClass()),
                getColumnsAndValueWithoutId(dto),
                where);

        return query;
    }

    private String getColumnsAndValueWithoutId(Dto dto) {
        Class clazz = dto.getClass();
        StringBuilder result = new StringBuilder();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(dto) != null && field.getAnnotation(Id.class) == null) {
                    result.append(String.format("%s = %s",
                            field.getAnnotation(Column.class).value(),
                            convertToString(field.get(dto))));
                    result.append(", ");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(); //// TODO: 20.09.17 log
            }
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }

    public class TransactionWriter {
        private Connection con;

        public TransactionWriter open() throws SQLException {
            logger.debug("OPEN TRANSACTION");
            con = getConnection();
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return this;
        }

        public TransactionWriter write(Dto dto) throws SQLException {
            logger.debug("WRITE TRANSACTION");
            try {
                Mutator.this.write(con, dto);
            } catch (SQLException e) {
                rollback(e);
            }
            return this;
        }

        public TransactionWriter update(Dto dto) throws SQLException {
            logger.debug("UPDATE TRANSACTION");
            try {
                Mutator.this.update(con, dto, null);
            } catch (SQLException e) {
                rollback(e);
            }
            return this;
        }

        public void close() throws SQLException {
            logger.debug("CLOSE TRANSACTION");
            con.commit();
            con.close();
        }

        private void rollback(SQLException e) throws SQLException {
            logger.debug("ROLLBACK TRANSACTION");
            try {
                con.rollback();
            } catch (SQLException e1) {
                throw new SQLException(e1);
            } finally {
                con.close();
            }
            throw new SQLException(e);
        }
    }

}
