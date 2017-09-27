package ua.nure.romanenko.st4.tags;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Table;
import ua.nure.romanenko.st4.dbcp.Query;
import ua.nure.romanenko.st4.dto.*;

import javax.servlet.jsp.JspException;
import javax.swing.text.html.FormSubmitEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by denis on 24.09.17.
 */
public class ManagerOrdersTag extends ViewRoomsTag {

    private static final Logger logger = Logger.getLogger(ManagerOrdersTag.class);

    private String trueButtonAction;
    private String falseButtonAction;

    public void setTrueButtonAction(String trueButtonAction) {
        this.trueButtonAction = trueButtonAction;
    }

    public void setFalseButtonAction(String falseButtonAction) {
        this.falseButtonAction = falseButtonAction;
    }

    @Override
    public int doStartTag() throws JspException {
        List<Orders> orders;
        Orders filter = new Orders();
        filter.setStatus(OrderStatus.UNCHECKED);

        try {
            orders = readOrders(filter);
            System.out.println(orders.size());

            StringBuilder html = new StringBuilder();
            html.append("<div>");

            for (Orders order : orders) {
                html.append(orderToHtml(order));
            }

            html.append("</div>");

            pageContext.getOut().print(html.toString());
        } catch (SQLException e) {
            logger.error("can't read order!", e);
        } catch (IOException e) {
            logger.error("bad html code", e);
        }

        return SKIP_BODY;
    }

    private String dtoToHtml(Dto dto) {
        Class<? extends Dto> clazz = dto.getClass();
        StringBuilder result = new StringBuilder();
        String tableName = clazz.getAnnotation(Table.class).value();

        result.append("<div class=\"form\" >");
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                String paramName = field.getAnnotation(Column.class).value();
                String name = paramName;
                String value = String.valueOf(field.get(dto));

                result.append(fieldToHtml(name, paramName, value));

            } catch (IllegalAccessException e) {
                logger.fatal("REFLECTION: can't access to field!", e);
            }
        }
        result.append("</div>");
        return result.toString();
    }

    private String action;

    public void setAction(String action) {
        this.action = action;
    }

    private String genForm(FormSubmitEvent.MethodType method, String content) {
        StringBuilder form = new StringBuilder();
        form.append(String.format("<form action=\"%s\" method=\"%s\">", action, method.toString()));
        form.append(content + "</form>");
        return form.toString();
    }

    private String orderToHtml(Orders order) {
        StringBuilder result = new StringBuilder();
        String htmlDto = dtoToHtml(order);

        result.append("<div>");
        result.append(htmlDto);
        result.append("<input class=\"input\" name=\"apartmentId\" type=\"number\">");
        result.append(genButton("orderStatus", OrderStatus.WAITED.toString(), "ok"));
        result.append(genButton("orderStatus", OrderStatus.CANCELED.toString(), "cancel"));
        result.append("</div>");

        return genForm(FormSubmitEvent.MethodType.POST, result.toString());
    }

    private List<Orders> readOrders(Orders filter) throws SQLException {
        Query.QueryBuilder qb = query.getQueryBuilder(Orders.class);
        return (List<Orders>) qb.setWhere(filter)
                .readDtos();
    }

    private String genButton(String paramName, String paramValue, String name) {
        String button = String.format(
                "<button name=\"%s\" value=\"%s\" type=\"submit\">%s</button>",
                paramName, paramValue, name);
        return button;
    }


}