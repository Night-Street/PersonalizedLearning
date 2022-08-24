package modelController.sessionController;

import entities.Scheduleclass;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("scheduleclassController")
@SessionScoped
public class ScheduleclassController extends CommonModelController<Scheduleclass> implements Serializable {

    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.ScheduleclassController applicationScheduleclassController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    String tableName = "scheduleclass", listpage = "", editpage = "", viewpage = "", createpage = "";
    private final List<Scheduleclass> scheduleclassList = new LinkedList<>();

    public ScheduleclassController() {
    }
    protected Scheduleclass current;

    public Scheduleclass getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Scheduleclass();
        }
        return current;
    }
    public void prepareList() {
        getPageOperation().refreshData();
        mainXhtml.setPageName(this.listpage);
    }

    public void prepareView() {
        setSelected( (Scheduleclass) getItems().getRowData());
        selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(viewpage);
        this.logs(current.getName(), tableName, StaticFields.OPERATIONSEARCH);
    }

    public void prepareCreate() {
        setSelected( new Scheduleclass());
        selectedItemIndex = -1;
        mainXhtml.setPageName(createpage);
    }

    public void create() {
        try {
            List<Scheduleclass> existedList = applicationScheduleclassController.getQueryResultList("select * from Scheduleclass where name='" + current.getName() + "'");
            if (existedList.size() <= 0) {
                applicationScheduleclassController.create(current);
                this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);

                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getName() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller schedule 3");
        }
    }

    public void prepareEdit() {
        setSelected( (Scheduleclass) getItems().getRowData());
        selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(editpage);
    }

     public void setSelected(Scheduleclass scheduleclass){
         this.current=scheduleclass;
     }
     
    public void update() {
        try {
            applicationScheduleclassController.edit(current);
            this.logs(current.getName(), tableName, StaticFields.OPERATIONUPDATE);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(viewpage);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller schedule 1");
        }
    }

    public void destroy() {
        current = (Scheduleclass) getItems().getRowData();
        selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        getPageOperation().refreshData();
        setSelected(null);
        mainXhtml.setPageName(listpage);
    }

    private void performDestroy() {
        try {
            applicationScheduleclassController.remove(current);
            this.logs(current.getName(), tableName, StaticFields.OPERATIONDELETE);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller schedule 2");
        }
    }

    public void destroyAndView() {
        performDestroy();
        updateCurrentItem();
        getPageOperation().refreshData();
        if (selectedItemIndex >= 0) {
            mainXhtml.setPageName(viewpage);
        } else {
            // all items were removed - go back to list
            mainXhtml.setPageName(listpage);
        }
    }

    private void updateCurrentItem() {
        int count = applicationScheduleclassController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( (Scheduleclass) applicationScheduleclassController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }
}
