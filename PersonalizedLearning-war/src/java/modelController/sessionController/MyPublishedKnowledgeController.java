package modelController.sessionController;

import entities.Knowledge;
import entities.Reexamination;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;

/**
 *
 * @author haogs
 */
@Named
@SessionScoped
public class MyPublishedKnowledgeController extends CommonModelController<Knowledge> implements java.io.Serializable {

    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private modelController.applicationController.ReexaminationController applicationReexaminationController;
    @Inject
    private modelController.sessionController.KnowledgeController knowledgeController;
    // private int selectedItemIndex;
    //private List<Knowledge> knowledgesList = new LinkedList<>();
    private final String listpage = "mine/mypublished/MyPublishedKnowledgeList";
    @Inject
    private MainXhtml mainXhtml;

    public MyPublishedKnowledgeController() {
    }

    //=================发布者的知识点汇集==============begin===================
    private List<Knowledge> myPublishedKnowledges = new LinkedList<>();
    protected Knowledge current;

    public Knowledge getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new  Knowledge();
        }
        return current;
    }
    public void collectMine() {
        collectData();
        mainXhtml.setPageName(this.listpage);
    }

    private void collectData() {
        StringBuilder whereString = new StringBuilder();
        if (null != studentController.getLogined() && null != teacherAdminController.getLogined()) {
            //教师在使用切换到学生的功能，可能切换回来了
            if (commonSession.getRoleinfo().equals(applicationRoleinfoController.getTeacherRoleinfo())) {
                //当前是老师
                whereString.append(" where  teacherid=").append(teacherAdminController.getLogined().getId());
            } else {
                //当前是学生
                whereString.append("  where studentid=").append(studentController.getLogined().getId());
            }
        }
        if (null != studentController.getLogined() && null == teacherAdminController.getLogined()) {
            whereString.append("  where studentid=").append(studentController.getLogined().getId());
        } else if (null != teacherAdminController.getLogined() && null == studentController.getLogined()) {
            whereString.append(" where  teacherid=").append(teacherAdminController.getLogined().getId());
        }
        List<Reexamination> allReexaminations = applicationReexaminationController.getQueryResultList("select * from Reexamination " + whereString.toString() + " and knowledgeId  is not null and isCreate=true");
        if (allReexaminations.size() > 0) {
            StringBuilder sb = new StringBuilder();
            allReexaminations.forEach(reex -> {
                sb.append(reex.getKnowledgeid().getId()).append(",");
            });
            String where = sb.toString();
            where = where.substring(0, where.length() - 1);
            myPublishedKnowledges = applicationKnowledgeController.getQueryResultList("select * from Knowledge where id in(" + where + ")");
        } else {
            myPublishedKnowledges = new LinkedList<>();
        }
        getPageOperation().refreshData(myPublishedKnowledges);
    }
//=================发布者的知识点汇集==============end===================
//------------------prepare View, edit, list,create------------------

    public void prepareList() {
        if (null == getItems()) {
            collectData();
        }
        pageOperation.refreshData(myPublishedKnowledges);
        mainXhtml.setPageName(this.listpage);
        //refreshKnowledges();//For the Edit viewer, where the selected and candidated subjects
    }

    public void prepareView() {
        knowledgeController.setFromPublished(true);
        knowledgeController.setSelected((Knowledge) (getItems().getRowData()));
        knowledgeController.prepareView();
        //setTemSelectedLeft(null);
    }

    public void prepareEdit() {
        //只为List表格中的“编辑按钮”服务
        knowledgeController.setFromPublished(true);
        knowledgeController.setSelected((Knowledge) (getItems().getRowData()));
        knowledgeController.prepareEdit();
    }

    @Override
    public DataModel getItems() {
        if (null == pageOperation.getItems()) {
            collectData();
            pageOperation.refreshData(myPublishedKnowledges);
        }
        return pageOperation.getItems();
    }

}
