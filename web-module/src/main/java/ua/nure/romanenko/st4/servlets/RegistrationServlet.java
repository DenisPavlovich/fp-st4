package ua.nure.romanenko.st4.servlets;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dbcp.Mutator;
import ua.nure.romanenko.st4.dto.Accounts;
import ua.nure.romanenko.st4.dto.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by denis on 24.09.17.
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = "/signup")
public class RegistrationServlet extends SignInOut {

    private static final Logger logger = Logger.getLogger(RegistrationServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Mutator.TransactionWriter tw = Mutator.getTransactionWriter();

        Users infoUser = getUser(request);
        Accounts infoAccount = getAccount(request);

        try {
            tw.open();

            tw.write(infoUser);
            infoAccount.setUserId(infoUser.getId());
            tw.write(infoAccount);

            tw.close();

        } catch (SQLException e) {
            logger.error("can't sign up!", e);
            response.sendError(400);
        }
        response.sendRedirect("/index.jsp");
    }

    private Users getUser(HttpServletRequest request){
        Users user = new Users();
        user.setName(request.getParameter("name"));
        user.setPhoneNumber(request.getParameter("phone_number"));
        user.setEmail(request.getParameter("email"));
        user.setAge(Integer.valueOf(request.getParameter("age")));
        return user;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
