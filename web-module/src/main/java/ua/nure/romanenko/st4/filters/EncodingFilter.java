package ua.nure.romanenko.st4.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by denis on 27.09.17.
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = "/*")
public class EncodingFilter implements Filter {

    private static final ResourceBundle rb = ResourceBundle.getBundle("app");

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String encoding = rb.getString("app.encoding");

        req.setCharacterEncoding(encoding);
        resp.setCharacterEncoding(encoding);

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
