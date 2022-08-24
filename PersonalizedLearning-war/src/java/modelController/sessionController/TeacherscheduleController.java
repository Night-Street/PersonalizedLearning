package modelController.sessionController;

import entities.Teacherschedule;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("teacherscheduleController")
@SessionScoped
public class TeacherscheduleController extends CommonModelController<Teacherschedule> implements Serializable {

    private final String tableName = "teacherschedule", listpage = "teacherschedule/List", editpage = "teacherschedule/Edit",
            viewpage = "teacherschedule/View", createpage = "teacherschedule/Create";
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.applicationController.TeacherscheduleController applicationTeacherscheduleController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;

    public TeacherscheduleController() {
    }
    protected Teacherschedule current;

    public Teacherschedule getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Teacherschedule();
        }
        return current;
    }
    public void prepareList() {
        pageOperation.refreshData();
        mainXhtml.setPageName(listpage);
    }

    public void setSelected( Teacherschedule teacherschedule){
        this.current=teacherschedule;
    }
    public void prepareView() {
        setSelected((Teacherschedule) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(viewpage);
    }

    public void prepareCreate() {
        setSelected( new Teacherschedule());
        selectedItemIndex = -1;
        mainXhtml.setPageName(createpage);
    }

    public void create() {
        try {
            boolean existed = false;
            for (Teacherschedule ts : current.getUserid().getTeacherscheduleSet()) {
                if (ts.getEndtime().after(current.getEndtime())) {
                    existed = true;
                    break;
                }
            }
            if (!existed) {
                applicationTeacherscheduleController.create(current);
                current.getUserid().getTeacherscheduleSet().add(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //evictForeignKey();
                this.logs(current.getBelongclassid().getName(), tableName, StaticFields.OPERATIONDELETE);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getUserid().getName() + ":"
                        + commonSession.getResourceBound().getString("Already")
                        + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher schedule 1");
        }
    }

    public void prepareEdit() {
        setSelected( (Teacherschedule) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(editpage);
    }

    public void update() {
        try {
            applicationTeacherscheduleController.edit(current);
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(viewpage);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher schedule 2");
        }
    }

    public void destroy() {
        setSelected( (Teacherschedule) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        pageOperation.refreshData();
        mainXhtml.setPageName(listpage);
    }

    public void destroyAndView() {
        performDestroy();
        pageOperation.refreshData();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            mainXhtml.setPageName(viewpage);
        } else {
            // all items were removed - go back to list
            pageOperation.refreshData();
            mainXhtml.setPageName(listpage);
        }
    }

    private void performDestroy() {
        try {
            applicationTeacherscheduleController.remove(current);
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher schedule 3");
        }
    }

    private void updateCurrentItem() {
        int count = applicationTeacherscheduleController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( (Teacherschedule) applicationTeacherscheduleController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }
}
