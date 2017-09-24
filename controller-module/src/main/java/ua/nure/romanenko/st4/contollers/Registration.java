package ua.nure.romanenko.st4.contollers;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dbcp.Mutator;
import ua.nure.romanenko.st4.dto.Accounts;
import ua.nure.romanenko.st4.dto.Users;

import java.sql.SQLException;

/**
 * Created by denis on 24.09.17.
 */
public class Registration {

    private Mutator mutator = new Mutator();

    public void singUp(Users user, Accounts account) throws SQLException {
            user = (Users) mutator.write(user);
            account.setUserId(user.getId());
            mutator.write(account);
    }
}
