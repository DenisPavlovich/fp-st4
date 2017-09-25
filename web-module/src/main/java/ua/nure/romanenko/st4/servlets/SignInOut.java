package ua.nure.romanenko.st4.servlets;

import ua.nure.romanenko.st4.dto.Accounts;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by denis on 24.09.17.
 */
public abstract class SignInOut extends HttpServlet {

    protected Accounts getAccount(HttpServletRequest request) {
        Accounts account = new Accounts();
        account.setLogin(request.getParameter("username"));
        account.setPassword(request.getParameter("password"));
        return account;
    }

}
