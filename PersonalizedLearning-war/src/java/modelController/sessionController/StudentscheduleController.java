package modelController.sessionController;

import entities.Student;
import entities.Studentschedule;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("studentscheduleController")
@SessionScoped
public class StudentscheduleController extends CommonModelController<Studentschedule> implements Serializable {

    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.applicationController.StudentscheduleController applicationStudentscheduleController;
    @Inject
    private SchoolController schoolController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private final String tableName = "studentschedule", listpage = "studentschedule/List", editpage = "studentschedule/Edit",
            viewpage = "studentschedule/View", createpage = "studentschedule/Create";
    private List<Studentschedule> studentScheduleList = new LinkedList<>();
    protected Studentschedule current;

    public Studentschedule getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Studentschedule();
        }
        return current;
    }
    public void init() {
        getPageOperation().setDataModelList(applicationStudentscheduleController.getStudentscheduleList(schoolController.getSelected()));
    }

    public StudentscheduleController() {
    }

    public void setSelected(Studentschedule studentschedule) {
        current = studentschedule;
    }

    public void prepareList() {
        pageOperation.refreshData(applicationStudentscheduleController.getStudentscheduleList(schoolController.getSelected()));
        mainXhtml.setPageName(listpage);
    }

    public void prepareView() {
        setSelected( (Studentschedule) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(viewpage);
    }

    public void prepareCreate() {
        setSelected( new Studentschedule());
        selectedItemIndex = -1;
        mainXhtml.setPageName(createpage);
    }

    public void studentscheduleSetCreate() {
        for (Student student : getLeftSelectedStudentList()) {
            Set<Studentschedule> existedScheduleSet = student.getStudentscheduleSet();
            boolean existed = false;
            for (Studentschedule studentSchedule : existedScheduleSet) {
                if (studentSchedule.getEndtime().after(getSelected().getEndtime())
                        && studentSchedule.getBelongclassid().equals(current.getBelongclassid())) {
                    existed = true;
                }
            }
            if (!existed) {
                Studentschedule studentschedule = new Studentschedule();
                studentschedule.setUserid(student);
                studentschedule.setStarttime(getSelected().getStarttime());
                studentschedule.setEndtime(getSelected().getEndtime());
                studentschedule.setBelongclassid(current.getBelongclassid());
                //studentschedule.setMemo(memo);
                applicationStudentscheduleController.create(studentschedule);
                this.logs(student.getName() + current.getBelongclassid().getName(), tableName, StaticFields.OPERATIONINSERT);
            }
        }
        userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
    }

    public void create() {
        try {
            boolean existed = false;
            for (Studentschedule ss : studentController.getSelected().getStudentscheduleSet()) {
                if (ss.getBelongclassid().equals(current.getBelongclassid())) {
                    if (ss.getEndtime().after(current.getEndtime())) {
                        existed = true;
                        break;
                    }
                }
            }
            if (!existed) {
                applicationStudentscheduleController.create(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                this.logs(current.getBelongclassid().getName(), tableName, StaticFields.OPERATIONINSERT);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getUserid().getName() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student schedule 1");
        }
    }

    public void prepareEdit() {
        setSelected( (Studentschedule) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(editpage);
    }
    //================For student schedule==================
    private Set<Student> leftSelectedStudentSet = new HashSet<>();

    public Set<Student> getRightStudentSet() {
        return schoolController.getSelected().getStudentSet();
    }

    public Set<Student> getLeftSelectedStudentList() {
        return leftSelectedStudentSet;
    }

    public void setLeftSelectedStudentList(Set<Student> leftSelectedStudentSet) {
        this.leftSelectedStudentSet = leftSelectedStudentSet;
    }

    public void update() {
        try {
            applicationStudentscheduleController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(viewpage);
            this.logs(current.getBelongclassid().getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student schedule 2");
        }
    }

    public void destroy() {
        current = (Studentschedule) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        pageOperation.refreshData(applicationStudentscheduleController.getStudentscheduleList(schoolController.getSelected()));
        mainXhtml.setPageName(listpage);
        setSelected(null);
        this.logs(current.getBelongclassid().getName(), tableName, StaticFields.OPERATIONDELETE);
    }

    public void destroyAndView() {
        performDestroy();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            mainXhtml.setPageName(viewpage);
            pageOperation.refreshData(applicationStudentscheduleController.getStudentscheduleList(schoolController.getSelected()));
        } else {
            // all items were removed - go back to list
            pageOperation.refreshData();
            mainXhtml.setPageName(listpage);
        }
    }

    private void performDestroy() {
        try {
            applicationStudentscheduleController.remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student schedule 3");
        }
    }

    private void updateCurrentItem() {
        int count = applicationStudentscheduleController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( (Studentschedule) applicationStudentscheduleController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }
}
