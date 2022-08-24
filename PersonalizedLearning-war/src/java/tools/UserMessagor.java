package tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author haogs
 */
@Named
@SessionScoped
public class UserMessagor implements java.io.Serializable {

    List<String> messagesList = new LinkedList<>();
    List<FacesMessage> msgs = new ArrayList<>();

    public void addMessage(String message) {
        messagesList.add(message);
        FacesContext.getCurrentInstance().addMessage("latestMessage", new FacesMessage(message));
    }

    public String getAlertMessage() {
        StringBuilder sb = new StringBuilder();
        messagesList.forEach(mess -> {
            sb.append(mess);
        });
        messagesList.clear();//用完即销毁
        return sb.toString();
    }
}
