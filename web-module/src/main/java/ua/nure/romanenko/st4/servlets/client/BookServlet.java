package ua.nure.romanenko.st4.servlets.client;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dao.impl.OrderDaoImpl;
import ua.nure.romanenko.st4.dbcp.Mutator;
import ua.nure.romanenko.st4.dto.ApartmentType;
import ua.nure.romanenko.st4.dto.OrderStatus;
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
        OrderDaoImpl orderDao = new OrderDaoImpl();
        Orders order = null;
        try {
            order = takeOrderFromRequest(request);
            orderDao.insert(order, OrderStatus.WAITED);
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
        Integer accountId = Integer.valueOf(request.getSession().getAttribute("accountId").toString());

        Orders order = new Orders();
        order.setAccountId(accountId);
        order.setApartmentId(Integer.valueOf(request.getParameter("id")));
        order.setApartmentType(ApartmentType.valueOf(request.getParameter("type")));
        order.setFrom(dateFormat.parse(request.getParameter("from")));
        order.setTo(dateFormat.parse(request.getParameter("to")));

        return order;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
