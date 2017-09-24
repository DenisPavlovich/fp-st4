package ua.nure.romanenko.st4.servlets.client;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dbcp.Mutator;
import ua.nure.romanenko.st4.dto.Orders;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by denis on 25.09.17.
 */
@WebServlet(name = "BookServlet", urlPatterns = "/client/bookroom")
public class BookServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(BookServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Mutator mutator = new Mutator();
        Orders order = null;
        try {
            order = takeOrderFromRequest(request);
            mutator.write(order);
            response.sendRedirect("/index.jsp");
        } catch (SQLException e) {
            logger.error("can't book apartment!", e);
            response.sendError(401, e.getMessage());
        } catch (ParseException e) {
            logger.error("can't parse date!", e);
            response.sendError(500, e.getMessage());
        }
    }

    private Orders takeOrderFromRequest(HttpServletRequest request) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Orders order = new Orders();
        System.out.println("accountid");
        Integer accountId = Integer.valueOf(request.getSession().getAttribute("accountId").toString());
        System.out.println("accountid:"+accountId);

        order.setAccountId(accountId);
        order.setApartmentId(Integer.valueOf(request.getParameter("id")));
        System.out.println("id");
        order.setApartmentType(request.getParameter("type"));
        System.out.println("type");
        order.setFrom(dateFormat.parse(request.getParameter("from")));
        System.out.println("from");
        order.setTo(dateFormat.parse(request.getParameter("to")));
        System.out.println("to");

        return order;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
