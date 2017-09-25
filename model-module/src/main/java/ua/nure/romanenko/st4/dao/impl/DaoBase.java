package ua.nure.romanenko.st4.dao.impl;

import ua.nure.romanenko.st4.dbcp.Mutator;
import ua.nure.romanenko.st4.dbcp.Query;
import ua.nure.romanenko.st4.dto.Dto;

import java.sql.SQLException;

/**
 * Created by denis on 25.09.17.
 */
public abstract class DaoBase<T extends Dto> {

    protected Mutator mutator = new Mutator();
    protected Query query = new Query();

    public T insert(T dto) throws SQLException {
        return (T) mutator.write(dto);
    }

    public abstract T read(int id) throws SQLException;

    public void update(T dto) throws SQLException {
        mutator.update(dto);
    }

    public T delete(T dto) throws SQLException {
        return (T) query.readRow(dto);
    }

}
