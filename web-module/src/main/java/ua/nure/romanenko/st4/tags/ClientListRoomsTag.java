package ua.nure.romanenko.st4.tags;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dbcp.Query;
import ua.nure.romanenko.st4.dto.*;

import javax.servlet.jsp.JspException;

import javax.swing.text.html.FormSubmitEvent.MethodType;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by denis on 24.09.17.
 */
public class ClientListRoomsTag extends ViewRoomsTag {

    private ResourceBundle props = ResourceBundle.getBundle("fieldNameKeys");

    private Apartments apartmentFilter = null;
    private Orders orderFilter = null;

    private static final Logger logger = Logger.getLogger(ClientListRoomsTag.class);

    private String action;
    private String buttonName;
    private String apartmentStatus;

    private String apartmentType;
    private String withAccount;
    private String updateOrderStatus;

    public void setUpdateOrderStatus(String orderStatus) {
        this.updateOrderStatus = orderStatus;
    }

    public void setApartmentStatus(String apartmentStatus) {
        this.apartmentStatus = apartmentStatus;
        if (apartmentFilter == null) apartmentFilter = new Apartments();
        apartmentFilter.setStatus(ApartmentStatus.valueOf(apartmentStatus));
    }

    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
        if (apartmentFilter == null) apartmentFilter = new Apartments();
        apartmentFilter.setType(ApartmentType.valueOf(apartmentType));
    }

    public void setWithAccount(String withAccount) {
        this.withAccount = withAccount;
        if (orderFilter == null) orderFilter = new Orders();
        orderFilter.setAccountId(Integer.valueOf(pageContext.getSession().getAttribute("accountId").toString()));
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    @Override
    public int doStartTag() throws JspException {
        List<Apartments> rooms;
        Apartments filter = new Apartments();
        filter.setStatus(ApartmentStatus.FREE);

        try {
//            rooms = (List<Apartments>) query.readRows(filter);
            rooms = readApartmentFromBase();
            StringBuilder htmlResult = new StringBuilder();
            htmlResult.append("<div>");
            for (Apartments room : rooms) {
                htmlResult.append(apartmentToForm(room));
            }
            htmlResult.append("</div>");

            pageContext.getOut().print(htmlResult.toString());
            pageContext.setAttribute("rooms", rooms);
        } catch (SQLException e) {
            logger.error("can't get apartments from base!", e);
        } catch (IOException e) {
            logger.error("bad html code!", e);
        } catch (NoSuchFieldException e) {
            logger.fatal("REFLECTION: bad name field!", e);
        }
        return SKIP_BODY;
    }

    private List<Apartments> readApartmentFromBase() throws NoSuchFieldException, SQLException {
        String orderWithRoom = props.getString("order.with.room");
        String roomId = props.getString("room.id");

        Class joinTable = Orders.class;
        Field joinIdField = Orders.class.getDeclaredField(orderWithRoom);
        Field fromIdField = Apartments.class.getDeclaredField(roomId);

        Query.QueryBuilder qb = query.getQueryBuilder(Apartments.class);
        if (orderFilter != null && orderFilter.getAccountId() != null) {
            qb.setJoin(joinTable, joinIdField, fromIdField);
            qb.setWhere(apartmentFilter, orderFilter);
        } else qb.setWhere(apartmentFilter, orderFilter);

        return (List<Apartments>) qb.readDtos();
    }

    private String apartmentToForm(Apartments apartment) {
        return genForm(MethodType.POST, String.format("<div>")
                + apartmentToHtml(apartment)
                + genButton()
                + "</div>");
    }

    private String genForm(MethodType method, String content) {
        StringBuilder form = new StringBuilder();

        form.append(String.format("<form action=\"%s\" method=\"%s\">", action, method.toString()));
        if (updateOrderStatus != null)
            form.append(genHiddenOrderStatus(updateOrderStatus));
        form.append(content + "</form>");

        return form.toString();
    }

    private String genHiddenOrderStatus(String orderStatus) {
        return String.format("<input type=\"text\" name=\"orderStatus\" hidden value=\"%s\">", orderStatus);
    }

    private String genButton() {
        return String.format("<button type=\"submit\">%s</button>", buttonName);
    }
}
