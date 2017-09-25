package ua.nure.romanenko.st4.dao.impl;

import ua.nure.romanenko.st4.dto.Accounts;
import ua.nure.romanenko.st4.dto.Apartments;

import java.sql.SQLException;

/**
 * Created by denis on 25.09.17.
 */
public class AccountDaoImpl extends DaoBase<Accounts>{

    public Accounts read(int id) throws SQLException {
        Accounts account = new Accounts();
        account.setId(id);
        return (Accounts) query.readRow(account);
    }

}
