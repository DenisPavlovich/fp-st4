package ua.nure.romanenko.st4.dbcp;


import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;
import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Id;
import ua.nure.romanenko.st4.dto.Accounts;
import ua.nure.romanenko.st4.dto.Dto;
import ua.nure.romanenko.st4.dto.Users;

import java.awt.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by denis on 11.09.17.
 */
public class Mutator extends Component {

    private static final Logger LOGGER = Logger.getLogger(Mutator.class);

    public Dto write(Dto dto) throws SQLException {
        Connection con = getConnection();
        try (Statement statement = con.createStatement()) {
            return writeDto(dto, statement);
        } catch (SQLException e) {
            throw new SQLException(e); //// TODO: 12.09.17 log
        } finally {
            con.close();
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
        String updateQuery = buildUpdateQuery(object, filter);
        try (Connection con = getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.execute(updateQuery);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private
    ///////////////////////////////////////////////////////////////////////////

    private Dto writeDto(Dto dto, Statement statement) throws SQLException {
        String query = buildInsertQuery(dto);
        LOGGER.debug("try execute : " + query);
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
        String where = null;
        if (filter != null) {
            where = buildFilter(filter);
        } else if (dto.getId() != null) {
            where = String.format("%s = %s", getIdName(dto.getClass()), dto.getId());
        } else throw new SQLException("Update query without condition");

        Class clazz = dto.getClass();
        return String.format("UPDATE %s ", getTable(clazz)) +
                String.format("SET %s ", getColumnsAndValueWithoutId(dto)) +
                where;
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

}
