package modelController.sessionController;

import entities.Major;
import entities.Teachermajor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("teachermajorController")
@SessionScoped
public class TeachermajorController extends CommonModelController<Teachermajor> implements Serializable {
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.MajorController applicationMajorController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.applicationController.TeachermajorController applicationTeachermajorController;
    @Inject
    private tools.UserMessagor userMessagor;
    private List<Teachermajor> teachermajorsList;
    private final String tableName = "teachermajor", listpage = "teachermajor/List", editpage = "teachermajor/Edit",
            viewpage = "teachermajor/View", createpage = "teachermajor/Create";

    @PostConstruct
    public void init() {
        applicationMajorController.setMajorRelatedEntity(modelController.applicationController.MajorController.MajorRelatedEntity.TEACHER);
    }
    protected Teachermajor current;

    public Teachermajor getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Teachermajor();
        }
        return current;
    }
    public List<Teachermajor> getTeachermajorsList() {
        if (null == teachermajorsList) {
            teachermajorsList = new LinkedList<>();
        }
        return teachermajorsList;
    }

    public void refresh() {
        teachermajorsList = null;
    }

    public TeachermajorController() {
    }
    
    public void setSelected(Teachermajor teachermajor){
        current=teachermajor;
    }

    public void prepareList() {
        pageOperation.refreshData(getTeachermajorsList());
        mainXhtml.setPageName(listpage);
    }

    public void prepareView() {
        setSelected((Teachermajor) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(viewpage);
    }

    public void prepareCreate() {
        setSelected( new Teachermajor());
        selectedItemIndex = -1;
        mainXhtml.setPageName(createpage);
    }

    public void create() {
        try {
            boolean existed = false;
            for (Teachermajor tm : getTeachermajorsList()) {
                if (tm.getMajorid().equals(current.getMajorid())) {
                    if (tm.getTeacherid().equals(current.getTeacherid())) {
                        existed = true;
                        break;
                    }
                }
            }
            if (!existed) {
                applicationTeachermajorController.create(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                this.logs(current.getTeacherid().getName() + "-" + current.getMajorid().getName(), tableName, StaticFields.OPERATIONINSERT);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getTeacherid().getName() + ":" + current.getMajorid().getName() + ":"
                        + commonSession.getResourceBound().getString("Already")
                        + commonSession.getResourceBound().getString("Exist")
                );
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher major 3");
        }
    }

    public void prepareEdit() {
        setSelected( (Teachermajor) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(editpage);
    }

    public void update() {
        try {
            applicationTeachermajorController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(viewpage);
            this.logs(current.getTeacherid().getName() + "-" + current.getMajorid().getName(), tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher major 1");
        }
    }

    public void destroy() {
        current = (Teachermajor) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(listpage);
    }

    private void performDestroy() {
        try {
            applicationTeachermajorController.remove(current);
            this.logs(current.getTeacherid().getName() + "-" + current.getMajorid().getName(), tableName, StaticFields.OPERATIONDELETE);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher major 2");
        }
    }

    private Set<Major> teachMajors;

    public void setTeachMajors(Set<Major> teachMajors) {
        this.teachMajors = teachMajors;
    }

    public Set<Major> getTeachMajors() {
        return applicationTeachermajorController.getTeachMajors(teacherAdminController.getSelected());
    }

    public void completeMajorSelection() {
        //根据教师确定专业，如果需要增加功能：根据专业确定教师的话，则可以参考这部分
        Set<Major> existedMajors_A = teachMajors;
        Set<Major> selectedMajors_B = this.teachMajors;
        //获得要删除的
        Set<Major> removedMajors = new HashSet<>();
        removedMajors.addAll(existedMajors_A);
        removedMajors.removeAll(selectedMajors_B);
        //执行删除
        removedMajors.forEach((major) -> {
            applicationTeachermajorController.executUpdate("delete from teachermajor where teacherid=" + teacherAdminController.getSelected().getId()
                    + " and majorid=" + major.getId());
            this.logs(teacherAdminController.getSelected().getName() + "-" + major.getName(),
                    tableName, StaticFields.OPERATIONDELETE);
        });
        //获得要增加的
        selectedMajors_B.removeAll(existedMajors_A);
        selectedMajors_B.forEach((Major major) -> {
            Teachermajor teachermajorajor = new Teachermajor();
            teachermajorajor.setTeacherid(teacherAdminController.getSelected());
            teachermajorajor.setMajorid(major);
            applicationTeachermajorController.create(teachermajorajor);
            this.logs(teacherAdminController.getSelected().getName() + "-" + major.getName(),
                    tableName, StaticFields.OPERATIONINSERT);
        });
        setTeachMajors(null);//置空，从而当后面选择别的教师后，可以获得对应教师专业
    }
}
