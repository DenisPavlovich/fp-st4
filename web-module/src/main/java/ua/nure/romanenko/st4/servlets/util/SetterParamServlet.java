package ua.nure.romanenko.st4.servlets.util;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dao.impl.AccountDaoImpl;
import ua.nure.romanenko.st4.dto.Accounts;
import ua.nure.romanenko.st4.servlets.SignInOut;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by denis on 19.09.17.
 */
@WebServlet(name = "SetterParamServlet", urlPatterns = "/set")
public class SetterParamServlet extends SignInOut {

    private final static Logger logger = Logger.getLogger(SetterParamServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String paramName = request.getParameter("paramName");
        String paramValue = request.getParameter("paramValue");

        System.out.println(paramName + " = " + paramValue);

        request.getSession().setAttribute(paramName, paramValue);
        System.out.println(request.getSession().getAttribute(paramName));
        response.sendRedirect(request.getParameter("pageUrl"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Servlet#doGet");
    }


}
