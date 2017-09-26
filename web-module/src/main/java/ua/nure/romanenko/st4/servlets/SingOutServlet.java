package ua.nure.romanenko.st4.servlets;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by denis on 19.09.17.
 */
@WebServlet(name = "SignOut", urlPatterns = "/signout")
public class SingOutServlet extends SignInOut {

    private final static Logger logger = Logger.getLogger(SingOutServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("sign out user : " + request.getSession().getAttribute("accountId"));
        singOut(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("sign out user : " + request.getSession().getAttribute("accountId"));
        singOut(request, response);
    }

    private void singOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("index.jsp");
    }


}
