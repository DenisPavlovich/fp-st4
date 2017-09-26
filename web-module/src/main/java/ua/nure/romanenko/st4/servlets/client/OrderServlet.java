package ua.nure.romanenko.st4.servlets.client;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dao.impl.OrderDaoImpl;
import ua.nure.romanenko.st4.dto.ApartmentType;
import ua.nure.romanenko.st4.dto.OrderStatus;
import ua.nure.romanenko.st4.dto.Orders;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

/**
 * Created by denis on 25.09.17.
 */
@WebServlet(name = "OrderServlet", urlPatterns = "/order")
public class OrderServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(OrderServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("send order");
        OrderDaoImpl orderDao = new OrderDaoImpl();
        try {
            Orders order = buildOrder(request);
            orderDao.insert(order);
            response.sendRedirect("index.jsp");
        } catch (ParseException e) {
            logger.error("can't parse date!", e);
            response.sendError(401, e.getMessage());
        } catch (SQLException e) {
            logger.error("can't write order!", e);
            response.sendError(401, e.getMessage());
        }
    }

    private Orders buildOrder(HttpServletRequest request) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Orders order = new Orders();

        order.setAccountId(Integer.valueOf(request.getSession().getAttribute("accountId").toString()));
        order.setPersonCount(Integer.valueOf(request.getParameter("person_count")));
        order.setApartmentType(ApartmentType.valueOf(request.getParameter("apartmentType")));
        order.setFrom(dateFormat.parse(request.getParameter("from")));
        order.setTo(dateFormat.parse(request.getParameter("to")));

        return order;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
