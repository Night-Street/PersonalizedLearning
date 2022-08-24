package modelController.sessionController;

import entities.Question;
import entities.Reexamination;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.PersonalSessionSetup;

@Named("myPublishedQuestionController")
@SessionScoped
public class MyPublishedQuestionController implements java.io.Serializable {

    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    protected modelController.sessionController.LeadpointController leadpointController;
    @Inject
    protected KnowledgeController knowledgeController;
    @Inject
    protected modelController.applicationController.ReexaminationController applicaReexaminationController;
    @Inject
    protected PersonalSessionSetup personalSessionSetup;
    @Inject
    protected TeacherAdminController teacherAdminController;
    @Inject
    protected SubjectController subjectController;
    @Inject
    protected MajorController majorController;
    @Inject
    protected MainXhtml mainXhtml;
    @Inject
    protected modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    protected modelController.applicationController.LogsController applicationLogsController;
    @Inject
    protected modelController.applicationController.QuestionController applicationQuestionController;
    @Inject
    protected modelController.applicationController.StudentController applicationStudentController;
    @Inject
    protected modelController.applicationController.SubjectController applicationSubjectController;
    @Inject
    protected modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    protected modelController.sessionController.LearningresourceController learningresourceController;
    @Inject
    protected modelController.sessionController.StudentController studentController;
    @Inject
    protected tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.QuestionController questionController;
    private Question current;
    private final String listpage = "mine/mypublished/MyPublishedQuestionList";
    final String splitInputOutputTest = "====";
    final String folder = "subjective";

    //覆盖父类的这个方法的目的，是使得继承父类的方法可以直接使用
    //=================发布者的知识点汇集==============begin===================
    public void collectMine() {
        collectData();
        questionController.getPageOperation().refreshData(myPublishedQuestionCollection);
        mainXhtml.setPageName(this.listpage);
    }
    private List<Question> myPublishedQuestionCollection;

    private void collectData() {
        String whereString = "";
        if (null != studentController.getLogined() && null != teacherAdminController.getLogined()) {
            //教师切换到了学生的情况
            if (commonSession.getRoleinfo().equals(applicationRoleinfoController.getTeacherRoleinfo())) {
                whereString = " where  teacherid=" + teacherAdminController.getLogined().getId();
            } else {
                whereString = " where  studentid=" + studentController.getLogined().getId();
            }
        }
        if (null != studentController.getLogined() && null == teacherAdminController.getLogined()) {
            //只有学生登录
            whereString = " where  studentid=" + studentController.getLogined().getId();
        } else if (null != teacherAdminController.getLogined() && null == studentController.getLogined()) {
            //只有老师登录 
            whereString = " where  teacherid=" + teacherAdminController.getLogined().getId();
        }
        List<Reexamination> allReexaminations = applicaReexaminationController.getQueryResultList("select * from Reexamination " + whereString + " and questionId  is not null and iscreate=true");
        if (allReexaminations.size() > 0) {
            StringBuilder sb = new StringBuilder();
            allReexaminations.forEach(reex -> {
                sb.append(reex.getQuestionid().getId()).append(",");
            });
            String where = sb.toString();
            where = where.substring(0, where.length() - 1);
            myPublishedQuestionCollection = applicationQuestionController.getQueryResultList("select * from question where id in (" + where + ")");
        } else {
            myPublishedQuestionCollection = new LinkedList<>();
        }
    }

    public void setDataModelList() {
        questionController.getPageOperation().setDataModelList(myPublishedQuestionCollection);
    }

    //------------------prepare View, edit, list,create------------------
    public void prepareList() {
        collectData();
        questionController.getPageOperation().refreshData(myPublishedQuestionCollection);
        mainXhtml.setPageName(this.listpage);
    }

    public void prepareView() {
        questionController.setFromPublished(true);
        questionController.setSelected((Question) (questionController.getItems().getRowData()));
        questionController.prepareSecondContent();
    }

    public void prepareEdit() {
        questionController.setFromPublished(true);
        questionController.setSelected((Question) (questionController.getItems().getRowData()));
        questionController.prepareEdit();
    }

    public void destroy() {
        questionController.destroy();
        mainXhtml.setPageName(this.listpage);
    }

}
