package ua.nure.romanenko.st4.servlets;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dao.impl.AccountDaoImpl;
import ua.nure.romanenko.st4.dto.Accounts;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by denis on 19.09.17.
 */
@WebServlet(name = "SignIn", urlPatterns = "/signin")
public class AuthorizationServlet extends SignInOut {

    private final static Logger logger = Logger.getLogger(AuthorizationServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountDaoImpl accountDao = new AccountDaoImpl();

        Accounts account = null;
        logger.info("SIGN IN");
        try {
            account = accountDao.read(getAccount(request));
            if (account == null) throw new Exception(String.format("have no account (login : %s)", account.getLogin()));

            request.getSession().setAttribute("auth", account);
            logger.debug("login : " + account.getLogin());
            response.sendRedirect("/index.jsp");
        } catch (SQLException e) {
            response.sendError(500);
            logger.error("can't execute query!", e);
        } catch (Exception e) {
            response.sendError(401);
            logger.error("can't sign in!", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Servlet#doGet");
    }


}
