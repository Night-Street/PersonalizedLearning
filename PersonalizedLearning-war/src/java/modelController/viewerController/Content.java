package modelController.viewerController;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author asus
 */
@Named
@SessionScoped
public class Content implements java.io.Serializable {

    private String outcome;

    public Content() {
    }

    public String getOutcome() {
        if (null == outcome) {
            outcome = "default";
        }
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
