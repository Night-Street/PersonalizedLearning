package modelController.sessionController;

import entities.Edulevel;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author hgs
 */
@Named("edulevelController")
@SessionScoped
public class EdulevelController extends CommonModelController<Edulevel> implements java.io.Serializable {

    @Inject
    private modelController.applicationController.EdulevelController applicationEdulevelController;
    protected Edulevel current;

    public Edulevel getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Edulevel();
        }
        return current;
    }

    public void setSelected(Edulevel current) {
        this.current = current;
    }
   }
