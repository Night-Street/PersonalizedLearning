package modelController.sessionController;

import entities.Qwquestionwrong;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author haogs
 */
@Named("qwquestionwrongController")
@SessionScoped
public class QwquestionwrongController extends CommonModelController<Qwquestionwrong> implements Serializable {

    public QwquestionwrongController() {
    }
    protected Qwquestionwrong current;

    public Qwquestionwrong getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Qwquestionwrong();
        }
        return current;
    }
}
