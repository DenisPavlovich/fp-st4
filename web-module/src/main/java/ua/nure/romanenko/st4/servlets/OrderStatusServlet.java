package ua.nure.romanenko.st4.servlets;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dao.impl.OrderDaoImpl;
import ua.nure.romanenko.st4.dto.ApartmentType;
import ua.nure.romanenko.st4.dto.Apartments;
import ua.nure.romanenko.st4.dto.OrderStatus;
import ua.nure.romanenko.st4.dto.Orders;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static ua.nure.romanenko.st4.dto.OrderStatus.WAITED;

/**
 * Created by denis on 25.09.17.
 */
@WebServlet(name = "OrderStatusServlet", urlPatterns = "/orderstatus")
public class OrderStatusServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(OrderStatusServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("OrderServlet tut");
        OrderDaoImpl orderDao = new OrderDaoImpl();
        String newOrderStatus = request.getParameter("orderStatus");

        Orders orderFilter = new Orders();
        orderFilter.setAccountId(getAccountId(request));
        orderFilter.setApartmentId(getInteger(request, "id"));

        try {
            orderDao.update(OrderStatus.valueOf(newOrderStatus), orderFilter);
            response.sendRedirect("/index.jsp");
        } catch (SQLException e) {
            logger.error("can't update order status!", e);
            response.sendError(409, e.getMessage());
        }

        //// TODO: 26.09.17 client can make many orders for one apartment (safe) =>
        //// TODO: 26.09.17 manager can make new status for apartment from that orders (unsafe)

    }

    private Integer getInteger(HttpServletRequest request, String paramName) {
        String param = request.getParameter(paramName);
        if (param != null) {
            return Integer.valueOf(param);
        }
        return null;
    }

    private Integer getAccountId(HttpServletRequest request) {
        Object id = request.getSession().getAttribute("accountId");
        if (id != null)
            return Integer.valueOf(id.toString());
        return null;
    }

    private OrderStatus getOrderStatus(String orderStatus) {
        if (orderStatus == null) return null;
        return OrderStatus.valueOf(orderStatus);
    }

//    private Orders buildOrder(HttpServletRequest request) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Orders order = new Orders();
//
//        order.setAccountId(Integer.valueOf(request.getSession().getAttribute("accountId").toString()));
//        order.setPersonCount(Integer.valueOf(request.getParameter("person_count")));
//        order.setApartmentType(ApartmentType.valueOf(request.getParameter("apartmentType")));
//        order.setFrom(dateFormat.parse(request.getParameter("from")));
//        order.setTo(dateFormat.parse(request.getParameter("to")));
//
//        return order;
//    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
