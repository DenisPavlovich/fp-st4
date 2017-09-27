package ua.nure.romanenko.st4.filters;

import ua.nure.romanenko.st4.dto.Apartments;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by denis on 27.09.17.
 */
@WebFilter(filterName = "CalcFilter", urlPatterns = "/pages/client/booked_rooms.jsp")
public class CalcFilter implements Filter {

    private static final ResourceBundle rb = ResourceBundle.getBundle("app");

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
