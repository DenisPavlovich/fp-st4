package ua.nure.romanenko.st4.contollers;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dbcp.Query;
import ua.nure.romanenko.st4.dto.AccountType;
import ua.nure.romanenko.st4.dto.Accounts;

import java.sql.SQLException;

/**
 * Created by denis on 24.09.17.
 */
public class Authorization {

    private Query query = new Query();

    public Accounts singIn(Accounts account) throws SQLException {
        return (Accounts) query.readRow(account);
    }


}
