package ua.nure.romanenko.st4.tags;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Table;
import ua.nure.romanenko.st4.dbcp.Query;
import ua.nure.romanenko.st4.dto.ApartmentStatus;
import ua.nure.romanenko.st4.dto.ApartmentType;
import ua.nure.romanenko.st4.dto.Apartments;

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
    private Query query = new Query();
    private static final Logger logger = Logger.getLogger(ViewRoomsTag.class);

    private ApartmentType apartmentType;
    private ApartmentStatus apartmentStatus;

    private String urlServlet;
    private String buttonName;

    public void setApartmentType(String apartmentType) {
        this.apartmentType = ApartmentType.valueOf(apartmentType);
    }

    public void setApartmentStatus(String apartmentStatus) {
        this.apartmentStatus = ApartmentStatus.valueOf(apartmentStatus);
    }

    @Override
    public int doStartTag() throws JspException {
        Apartments filter = new Apartments();
        filter.setStatus(ApartmentStatus.FREE);
        List<Apartments> rooms = new LinkedList<>();

        try {
            rooms = (List<Apartments>) query.readRows(filter);
            logger.debug(String.format("get apartments (size %s)", rooms.size()));
        } catch (SQLException e) {
            logger.error("can't execute query!", e);
        }

        StringBuilder html = new StringBuilder();
        html.append("<div>");
        for (Apartments room : rooms) {
            html.append(apartmentToHtml(room));
        }
        html.append("</div>");

        JspWriter w = pageContext.getOut();
        try {
            w.print(html.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        pageContext.setAttribute("rooms", rooms);
        return SKIP_BODY;
    }

    protected String apartmentToHtml(Apartments apartment) {
        String className = apartment.getClass().getAnnotation(Table.class).value();
        StringBuilder result = new StringBuilder();
        result.append(String.format("<div class=\"%s\" >", className));

//        result.append(fieldToHtml("Number", "number", apartment.getNumber().toString()));
//        result.append(fieldToHtml("Rooms", apartment.getRooms().toString()));
//        result.append(fieldToHtml("PersonCount", apartment.getPersonCount().toString()));
//        result.append(fieldToHtml("Price", apartment.getPrice().toString()));
//        result.append(fieldToHtml("Status", apartment.getStatus().toString()));
//        result.append(fieldToHtml("Type", apartment.getType().toString()));
//        result.append(fieldToHtml("PhotoPath", apartment.getPhotoPath()));

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
        return String.format("<p> %s : <input class=\"text\" name=\"%s\" value=\"%s\" readonly> </p>",
                name, paramName, value);
    }
}
