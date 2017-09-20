package ua.nure.romanenko.st4.dbcp;


import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Id;
import ua.nure.romanenko.st4.dto.Dto;
import ua.nure.romanenko.st4.dto.Users;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by denis on 11.09.17.
 */
public class Mutator extends Component {

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
        try (ResultSet resultSet = statement.executeQuery(buildInsertQuery(dto))) {
            if (resultSet.next()) {
                dto.setId(resultSet.getInt(1));
            }
        }
        return dto;
    }

    private String buildInsertQuery(Dto dto) {
        Class clazz = dto.getClass();
        return String.format("INSERT INTO %s (%s) VALUES (%s) RETURNING %s",
                getTable(clazz),
                getColumnsNamesWithoutId(clazz),
                getColumnsValuesWithoutId(clazz, dto),
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
