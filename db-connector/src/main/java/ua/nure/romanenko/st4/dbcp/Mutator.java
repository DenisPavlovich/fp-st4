package ua.nure.romanenko.st4.dbcp;


import ua.nure.romanenko.st4.dto.Dto;

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

    private Dto writeDto(Dto dto, Statement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery(buildQuery(dto))) {
            if (resultSet.next()) {
                dto.setId(resultSet.getInt(1));
            }
        }
        return dto;
    }

    private String buildQuery(Dto dto) {
        Class clazz = dto.getClass();
        return String.format("INSERT INTO %s (%s) VALUES (%s) RETURNING %s",
                getTable(clazz),
                getColumnsNamesWithoutId(clazz),
                getColumnsValuesWithoutId(clazz, dto),
                getIdName(clazz));
    }

}
