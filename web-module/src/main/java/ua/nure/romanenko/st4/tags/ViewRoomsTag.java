package ua.nure.romanenko.st4.tags;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Table;
import ua.nure.romanenko.st4.dbcp.Query;
import ua.nure.romanenko.st4.dto.ApartmentStatus;
import ua.nure.romanenko.st4.dto.Apartments;
import ua.nure.romanenko.st4.dto.Orders;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by denis on 24.09.17.
 */
public class ViewRoomsTag extends TagSupport {
    protected Query query = new Query();
    private static final Logger logger = Logger.getLogger(ViewRoomsTag.class);

    protected String apartmentStatus;

    public void setApartmentStatus(String apartmentStatus) {
        this.apartmentStatus = apartmentStatus;
    }

    @Override
    public int doStartTag() throws JspException {
        Apartments filter = new Apartments();
        filter.setStatus(ApartmentStatus.FREE);
        List<Apartments> rooms = new LinkedList<>();

        try {
            rooms = readRooms();
            logger.debug(String.format("get apartments (size %s)", rooms.size()));
        } catch (SQLException e) {
            logger.error("can't execute query!", e);
        }

        StringBuilder html = new StringBuilder();
        html.append("<div>");
        for (Apartments room : rooms) {
            html.append(dtoToHtml(room));
        }
        html.append("</div>");

        JspWriter w = pageContext.getOut();
        try {
            w.print(html.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SKIP_BODY;
    }

    private List<Apartments> readRooms() throws SQLException {
        Query.QueryBuilder qb = query.getQueryBuilder(Apartments.class);
        if (apartmentStatus != null) {
            Apartments filter = new Apartments();
            filter.setStatus(ApartmentStatus.valueOf(apartmentStatus));
            qb.setWhere(filter);
        }
        if (getOrderBy() != null)
            qb.setOrder(getOrderBy());
        return (List<Apartments>) qb.readDtos();
    }

    protected String getOrderBy(){
        return (String) pageContext.getSession().getAttribute("order");
    }

    protected String genButton(String buttonName) {
        return String.format("<button type=\"submit\">%s</button>", buttonName);
    }

    protected String dtoToHtml(Apartments apartment) {
        String className = apartment.getClass().getAnnotation(Table.class).value();
        StringBuilder result = new StringBuilder();
//        result.append(String.format("<div class=\"%s\" >", className));
        result.append("<div class=\"form\" >");

        Class clazz = apartment.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String name = field.getAnnotation(Column.class).value();
                String paramName = field.getAnnotation(Column.class).value();
                String value = String.valueOf(field.get(apartment));

                result.append(fieldToHtml(name, paramName, value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        result.append(String.format("</div>", className));
        return result.toString();
    }

    protected String fieldToHtml(String name, String paramName, String value) {
        return String.format("<p> %s : <input class=\"text\" type=\"text\" name=\"%s\" value=\"%s\" readonly></p>",
                name, paramName, value);
    }
}
