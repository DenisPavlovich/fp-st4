package ua.nure.romanenko.st4.servlets.manager;

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
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by denis on 25.09.17.
 */
@WebServlet(name = "ManagerServlet", urlPatterns = "/manager")
public class ManagerServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ManagerServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDaoImpl orderDao = new OrderDaoImpl();
        Integer id = Integer.valueOf(request.getParameter("id"));
        Integer apartmentId = Integer.valueOf(request.getParameter("apartmentId"));
        OrderStatus status = OrderStatus.valueOf(request.getParameter("orderStatus"));

        try {
            Orders filter = new Orders();
            filter.setId(id);
            filter.setApartmentId(apartmentId);
            filter.setStatus(status);
            orderDao.update(filter);
            response.sendRedirect("index.jsp");
        } catch (SQLException e) {
            logger.error("can't change param!", e);
            response.sendError(400, e.getMessage());
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("ManagerServlet#get");

        logger.debug("ManagerServlet#get");
    }
}
