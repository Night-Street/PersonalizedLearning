package modelController.sessionController;

import entities.Question;
import entities.Qwquestionwrong;
import entities.School;
import entities.Subject;
import entities.Teacschoolsubject;
import entities.WrongquestionCollection;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("wrongquestionCollectionController")
@SessionScoped
public class WrongquestionCollectionController extends CommonModelController<WrongquestionCollection> implements Serializable {

    private final String tableName = "wrongquestion", listpage = "wrongquestion/List";
    //editpage = "wrongquestion/Edit", viewpage = "wrongquestion/View", createpage = "wrongquestion/Create";
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.applicationController.WrongquestionCollectionController applicationWrongquestionCollectionController;
    @Inject
    private modelController.sessionController.SubjectController subjectController;
    @Inject
    private modelController.applicationController.QwquestionwrongController applicationQuestionwrongController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.SchoolController schoolController;
    private final List<WrongquestionCollection> wrongQuestionCollectionList = new LinkedList<>();
    //一方面要找到错，显示犯错的原因，另一方面要显示题目内容
    private List<Qwquestionwrong> qwquestionwrongs = new LinkedList<>();
    private final HashMap<Question, List<WrongquestionCollection>> questionWrongMap = new HashMap<>();
    private String wrongQuestionInformation = "";

    public WrongquestionCollectionController() {
    }
    protected WrongquestionCollection current;

    public WrongquestionCollection getSelected() {
        if (current == null) {
            current = new WrongquestionCollection();
            selectedItemIndex = -1;
        }
        return current;
    }

    public String create() {
        try {
            boolean existed = false;
            for (WrongquestionCollection wqc : current.getStudentId().getWrongquestionCollectionSet()) {
                if (wqc.getQuestionId().equals(wqc.getQuestionId())) {
                    if (wqc.getReasonId().equals(current.getReasonId())) {
                        existed = true;
                        break;
                    }
                }
            }
            if (!existed) {
                applicationWrongquestionCollectionController.create(current);
                current.getStudentId().getWrongquestionCollectionSet().add(current);
                pageOperation.refreshData();
                this.logs(current.getStudentId().getName(), tableName, StaticFields.OPERATIONINSERT);
                current = new WrongquestionCollection();
            }
            return null;
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller worng question collect 1");
            return null;
        }
    }
//
//    public void prepareCreate() {
//        current = new WrongquestionCollection();
//        selectedItemIndex = -1;
//    }

    public void destroy() {
        current = (WrongquestionCollection) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        mainXhtml.setPageName(listpage);
    }

    private void performDestroy() {
        try {
            applicationWrongquestionCollectionController.remove(current);
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            this.logs(current.getStudentId().getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller worng question collect 2");
        }
    }

    private void updateCurrentItem() {
        int count = applicationWrongquestionCollectionController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = (WrongquestionCollection) applicationWrongquestionCollectionController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }
    Teacschoolsubject accurateSchoolSubject = null;
    Set<WrongquestionCollection> temWrongquestions = new HashSet<>();

    public void refreshWrongQuestion() {
        temWrongquestions.clear();
        //1.获得课程；
        Subject subject = subjectController.getSelected();
        if (subject.getId() != null) {
            if (null != teacherAdminController.getLogined()) {//老师选中了一门课程
                Set<Teacschoolsubject> teacschoolsubjectSet = teacherAdminController.getLogined().getTeacschoolsubjectSet();
                School school = schoolController.getSelected();
                if (school.getId() != null) {//班级也确定了
                    for (Teacschoolsubject teaSchoSub : teacschoolsubjectSet) {
                        if (teaSchoSub.getSchoolid().equals(school) && teaSchoSub.getSubjectid().equals(subject)) {
                            accurateSchoolSubject = teaSchoSub;
                            break;
                        }
                    }
                }
                accurateSchoolSubject.getSchoolid().getStudentSet().forEach(student -> {//本班的学生
                    student.getWrongquestionCollectionSet().forEach(wrongquestion -> {
                        temWrongquestions.add(wrongquestion);
                    });
                });
            } else if (null != studentController.getLogined()) {
                studentController.getLogined().getWrongquestionCollectionSet().forEach(wrongquestion -> {
                    //是当前课程
                    temWrongquestions.add(wrongquestion);
                });
            } else if (null != parentController.getLogined()) {//双亲选中了一个孩子
                if (null != studentController.getSelected().getId()) {
                    studentController.getSelected().getWrongquestionCollectionSet().forEach(wrongquestion -> {
                        temWrongquestions.add(wrongquestion);
                    });
                }
            }
            qwquestionwrongs.clear();
            wrongQuestionCollectionList.clear();
            questionWrongMap.clear();

            //下面排除不是本课程的题目
            temWrongquestions.forEach(wrq -> {
                if (wrq.getQuestionId().getKnowledgeId().getSubjectId().equals(subjectController.getSelected())) {
                    wrongQuestionCollectionList.add(wrq);
                }
            });
            temWrongquestions.clear();
            //下面获得题目的id号，再通过这些id号，到视图中获得题目与错原因
            StringBuilder sb = new StringBuilder();
            wrongQuestionCollectionList.forEach(wq -> {
                sb.append(wq.getId()).append(",");
            });
            if (sb.length() > 0) {//有题目存在，下面查找视图中符合条件的记录
                String questionIds = sb.substring(0, sb.length() - 1);
                qwquestionwrongs = applicationQuestionwrongController.getQueryResultList(
                        "select * from Qwquestionwrong where qid in (" + questionIds + ")");
            }
            wrongQuestionInformation = commonSession.getResourceBound().getString("Number")
                    +commonSession.getResourceBound().getString("Is")
                    +wrongQuestionCollectionList.size() ;
        }
    }

    public List<WrongquestionCollection> getWrongQuestionCollectionList() {
        return wrongQuestionCollectionList;
    }

    public List<Qwquestionwrong> getQwquestionwrongs() {
        return qwquestionwrongs;
    }

    public String getWrongQuestionInformation() {
        refreshWrongQuestion();//刷新错集
        return subjectController.getSubjectName() + wrongQuestionInformation;
    }

    public void setWrongQuestionInformation(String wrongQuestionInformation) {
        this.wrongQuestionInformation = wrongQuestionInformation;
    }
}
