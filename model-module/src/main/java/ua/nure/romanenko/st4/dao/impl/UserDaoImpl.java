package ua.nure.romanenko.st4.dao.impl;

import ua.nure.romanenko.st4.dto.Users;

import java.sql.SQLException;

/**
 * Created by denis on 25.09.17.
 */
public class UserDaoImpl extends DaoBase<Users>{

    public Users read(int id) throws SQLException {
        Users user = new Users();
        user.setId(id);
        return (Users) query.readRow(user);
    }
}
