package modelController.sessionController;

import entities.Realizationofmydream;
import entities.Studentdream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("realizationofmydreamController")
@SessionScoped
public class RealizationofmydreamController extends CommonModelController<Realizationofmydream> implements Serializable {

    @Inject
    SchoolController schoolController;
    @Inject
    RoleinfoController roleinfoController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.StudentdreamController studentdreamController;
    @Inject
    private modelController.applicationController.RealizationOfMyDreamController applicationRealizationOfMyDreamController;
    private Realizationofmydream current;
    private List<Realizationofmydream> searchedRealizationofmydreamdreamsList = new LinkedList<>();
    private final String tableName = "realizationofmydream", editpage = "studentDream/realizationofmydream/Edit",
            viewpage = "studentDream/realizationofmydream/View", createpage = "studentDream/realizationofmydream/Create", 
            listpage = "studentDream/realizationofmydream/List",
            list4TeacherPage = "studentDream/realizationofmydream/List4Teacher";

    public RealizationofmydreamController() {
    }

    public Realizationofmydream getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Realizationofmydream();
        }
        return current;
    }

    public Studentdream getCurrentStudentdream() {
        if (getSelected().getId() != null) {
            return getSelected().getDreamid();
        } else {
            return null;
        }
    }

    public void setSelected(Realizationofmydream current) {
        this.current = current;
    }

    public void create() {
        try {
            getSelected().setDreamid(studentdreamController.getSelected());
            //media 和knowledges 需要插入
            applicationRealizationOfMyDreamController.create(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(current.getDreamid().getStuid().getName() + " RealizationOfDream", tableName, StaticFields.OPERATIONINSERT);
            mainXhtml.setPageName(this.viewpage);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Realizationofmydream");
        }
    }

    public void prepareEdit() {
        mainXhtml.setPageName(this.editpage);
    }


    public void update() {
        try {
            applicationRealizationOfMyDreamController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(viewpage);
            this.logs(current.getDreamid().getStuid().getName() + " RealizationOfDream", tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Realizationofmydream 2");
        }
    }
//下面是为表格处理

    public void setDataModelList() {
        pageOperation.setDataModelList(new LinkedList(studentdreamController.getSelected().getRealizationofmydreamSet()));
    }

    public void prepareList() {
        List<Studentdream> listStudentdreams = new LinkedList(studentController.getLogined().getStudentdreamSet());
        List<Realizationofmydream> resultList = new LinkedList<>();
        for (Studentdream studentdream : listStudentdreams) {
            resultList.addAll(studentdream.getRealizationofmydreamSet());
        }
        pageOperation.refreshData(resultList);
        mainXhtml.setPageName(this.listpage);
    }

    public void prepareView() {
        setSelected((Realizationofmydream) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.viewpage);
    }

    public String prepareCreate() {
        setSelected(new Realizationofmydream());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
        return null;
    }

//服务于教师浏览与编辑的方法
    public void getRealizationofmydream4Teacher(Studentdream studentdream) {
        pageOperation.refreshData(new LinkedList(studentdream.getRealizationofmydreamSet()));
        mainXhtml.setPageName(this.list4TeacherPage);
    }

  
    public void prepareEdit4Teacher(Realizationofmydream realizationofmydream) {
        this.setSelected(realizationofmydream);
        mainXhtml.setPageName(this.editpage);
    }
}
