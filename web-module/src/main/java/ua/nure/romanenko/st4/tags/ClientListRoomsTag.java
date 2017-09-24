package ua.nure.romanenko.st4.tags;

import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.dbcp.Query;
import ua.nure.romanenko.st4.dto.ApartmentStatus;
import ua.nure.romanenko.st4.dto.Apartments;

import javax.servlet.jsp.JspException;

import javax.swing.text.html.FormSubmitEvent.MethodType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by denis on 24.09.17.
 */
public class ClientListRoomsTag extends ViewRoomsTag {

    private static final Logger logger = Logger.getLogger(ClientListRoomsTag.class);
    private Query query = new Query();

    private String action;
    private String buttonName;

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
            rooms = (List<Apartments>) query.readRows(filter);
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
        }
        return SKIP_BODY;
    }

    private String apartmentToForm(Apartments apartment) {
        return genForm(MethodType.POST, String.format("<div class=\"room_list\">")
                + apartmentToHtml(apartment)
                + genButton()
                + "</div>");
    }

    private String genForm(MethodType method, String content) {
        return String.format("<form action=\"%s\" method=\"%s\">", action, method.toString()) +
                content + "</form>";
    }

    private String genButton() {
        return String.format("<button type=\"submit\">%s</button>", buttonName);
    }
}
