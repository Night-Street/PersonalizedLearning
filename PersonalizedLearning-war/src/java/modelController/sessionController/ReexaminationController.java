package modelController.sessionController;

import entities.Knowledge;
import entities.Parent;
import entities.Question;
import entities.Reexamination;
import entities.Statusofresources;
import entities.Student;
import entities.TeacherAdmin;
import entities.User;
import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.applicationController.UserType;
import static modelController.applicationController.UserType.Teacher;
import modelController.viewerController.MainXhtml;
import sessionBeans.ReexaminationFacadeLocal;

@Named("reexaminationController")
@SessionScoped
public class ReexaminationController extends CommonModelController<Reexamination> implements Serializable {

    @Inject
    private modelController.applicationController.ReexaminationController applicationReexaminationController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.StatusofresourcesController applicationStatusofresourcesController;
    @EJB
    private ReexaminationFacadeLocal facadeLocal;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.sessionController.KnowledgeController knowledgeController;
    @Inject
    private modelController.sessionController.QuestionController questionController;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    private Integer resourceType;//0--knowledge,1--question，0表示知识点，1表示习题
    private Integer status, secondStatus;//
    private boolean isReexamin = false;
    private final String examinpage = "reexampination/Examinpage",
            toExaminPage = "reexampination/Toexaminpage",
            viewKnowledge = "reexampination/Viewknowledge",
            viewQuestion = "reexampination/Viewquestion";
    private String reexaminationSuggestion = "";
    protected Reexamination current;

    public Reexamination getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Reexamination();
        }
        return current;
    }

    public void setSelected(Reexamination reexamination) {
        current = reexamination;
    }

    public String myRecord(int resourceType, int status) {
        //resourceType--题目或知识点；status--0:等待审核；1--审核通过；2--审核未通过
        setStatus(status);
        setResourceType(resourceType);
        prepareMyRecordedList(resourceType, status);
        mainXhtml.setPageName(examinpage);
        return null;
    }

    private void prepareMyRecordedList(int resourceType, int status) {
        setStatus(status);
        setSecondStatus(null);
        if (resourceType == 0) {
            knowledgeController.prepareMyRecorderKnowledgeList(status);
        } else {
            questionController.prepareQuestionList(status);
        }
    }

    public String myExaminedPage(int resourceType, int status) {
        //resourceType--题目或知识点；status--0:等待审核；1--审核通过；2--审核未通过
        setStatus(status);
        setResourceType(resourceType);
        //判断是教师，还是审核员
        switch (commonSession.getUserType()) {
            case Teacher:
                prepareMyExaminList(resourceType, status, null);//普通教师不进行二次审核
                break;
            case Reexamin:
                prepareMyExaminList(resourceType, 1, status);//审核员只审核第一次已经通过审核的
                break;
        }
        mainXhtml.setPageName(toExaminPage);
        return null;
    }

    private void prepareMyExaminList(int resourceType, int status, Integer dsecondStatus) {
        setStatus(status);
        if (resourceType == 0) {
            knowledgeController.prepareMyExaminedKnowledgeList(status, dsecondStatus);
        } else {
            questionController.prepareMyExaminedQuestionList(status, dsecondStatus);
        }
    }

    public ReexaminationController() {
    }

    public void setUser4(Reexamination reexamination) {
        User user = commonSession.getUser();
        switch (commonSession.getUserType()) {//审核员没考虑进来，因为审核员不录入
            case Student:
                reexamination.setStudentid((Student) user);
                break;
            case Teacher:
                reexamination.setTeacherid((TeacherAdmin) user);
                break;
            case Parent:
                reexamination.setParentid((Parent) user);
                break;
        }
    }

    public boolean isEditable(Knowledge knowledge, Question question) {
        User user = commonSession.getUser();
        Reexamination reexamination;
        if (null != question) {
            Set<Reexamination> reexaminations = applicationReexaminationController.getReexamination4Question(question);
            if (reexaminations.size() > 0) {
                reexamination = ((Reexamination) reexaminations.toArray()[0]);
            } else {
                return true;
            }
            switch (commonSession.getUserType()) {//审核员没考虑进来，因为审核员不录入，也不编辑
                case Student:
                    if (reexamination.getStudentid() != null) {
                        return reexamination.getStudentid().getId().equals(user.getId())
                                && (reexamination.getStatus().isWaitingExamin() || reexamination.getStatus().isSaved());//一审还没开始
                    } else {
                        return false;
                    }
                case Teacher://
                    return true;
                //return reexamination.getStatus().isWaitingExamin();//审核完成;
                case Parent:
                    if (reexamination.getParentid() != null) {
                        return reexamination.getParentid().getId().equals(user.getId())
                                && (reexamination.getStatus().isWaitingExamin() || reexamination.getStatus().isSaved());//一审还没开始
                    } else {
                        return false;
                    }
            }
        } else if (null != knowledge) {
            Set<Reexamination> reexaminations = (Set<Reexamination>) applicationReexaminationController.getReexamination4Knowledge(knowledge);
            if (reexaminations.isEmpty()) {
                return true;
            } else {
                reexamination = (Reexamination) reexaminations.toArray()[0];
            }
            switch (commonSession.getUserType()) {
                case Student:
                    if (reexamination.getStudentid() != null) {
                        return reexamination.getStudentid().getId().equals(user.getId())
                                && (reexamination.getStatus().isWaitingExamin() || reexamination.getStatus().isSaved());//一审还没开始
                    } else {
                        return false;
                    }
                case Teacher:
                    return true;
                //return !reexamination.getStatus().isWaitingExamin();//审核完成;
                case Parent:
                    if (reexamination.getParentid() != null) {
                        return reexamination.getParentid().getId().equals(user.getId())
                                && (reexamination.getStatus().isWaitingExamin() || reexamination.getStatus().isSaved());//一审还没开始
                    } else {
                        return false;
                    }
            }
        }
        return false;
    }

    private boolean isDeletableThen(Knowledge knowledge, Question question) {//是owner
        User user = commonSession.getUser();
        if (null != user) {
            if (null != question) {
                Set<Reexamination> setReexaminations = applicationReexaminationController.getReexamination4Question(question);
                if (setReexaminations.isEmpty()) {
                    return false;
                } else {
                    Reexamination reexamination = (Reexamination) setReexaminations.toArray()[0];
                    switch (commonSession.getUserType()) {//审核员没考虑进来，因为审核员不录入，也不删除
                        case Student:
                            if (reexamination.getStudentid() != null) {
                                return reexamination.getStudentid().getId().equals(user.getId())
                                        && (reexamination.getStatus().isExaminFailed() || reexamination.getStatus().isSaved());//审核未通过才可能删除
                            } else {
                                return false;
                            }
                        case Teacher:
                            if (reexamination.getTeacherid() != null) {
                                return reexamination.getTeacherid().getId().equals(user.getId())
                                        && (reexamination.getStatus().isExaminFailed() || reexamination.getStatus().isSaved());
                            } else {
                                return false;
                            }
                        case Parent:
                            if (reexamination.getParentid() != null) {
                                return reexamination.getParentid().getId().equals(user.getId())
                                        && (reexamination.getStatus().isExaminFailed() || reexamination.getStatus().isSaved());
                            } else {
                                return false;
                            }
                    }
                }
            } else if (null != knowledge) {
                Reexamination reexamination;
                Set<Reexamination> reexaminations = applicationReexaminationController.getReexamination4Knowledge(knowledge);
                if (reexaminations.isEmpty()) {
                    return false;
                } else {
                    reexamination = (Reexamination) reexaminations.toArray()[0];
                }
                switch (commonSession.getUserType()) {
                    case Student:
                        if (reexamination.getStudentid() != null) {
                            return reexamination.getStudentid().getId().equals(user.getId())
                                    && (reexamination.getStatus().isExaminFailed() || reexamination.getStatus().isSaved());
                        } else {
                            return false;
                        }
                    case Teacher:
                        if (reexamination.getTeacherid() != null) {
                            return reexamination.getTeacherid().getId().equals(user.getId())
                                    && (reexamination.getStatus().isExaminFailed() || reexamination.getStatus().isSaved());
                        } else {
                            return false;
                        }
                    case Parent:
                        if (reexamination.getParentid() != null) {
                            return reexamination.getParentid().getId().equals(user.getId())
                                    && (reexamination.getStatus().isExaminFailed() || reexamination.getStatus().isSaved());
                        } else {
                            return false;
                        }
                }
            }
        }
        return false;
    }

    public boolean isDeletable(Knowledge knowledge, Question question) {
        if (null != teacherAdminController.getLogined()
                && teacherAdminController.getLogined().getRoleId().equals(applicationRoleinfoController.getSecretaryRoleinfo())) {
            return true;
        } else {
            return isDeletableThen(knowledge, question);
        }
    }

    public void createReexamination(Knowledge knowledge, Question question) {
        Reexamination reexamination = new Reexamination();
        reexamination.setStatus(applicationStatusofresourcesController.getStatusofresources(0));
        reexamination.setCreatedDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        this.setUser4(reexamination);
        if (null != knowledge) {
            if (knowledge.getReexaminationSet().size() > 0) {
                reexamination.setIsCreate(false);//是在更新，而不是在创建新的
            } else {
                reexamination.setIsCreate(true);//是在更新，而不是在创建新的
            }
            reexamination.setKnowledgeid(knowledge);
            reexamination.setToteacher(teacherAdminController.getWhom2Examin(commonSession.getUserType(), null, knowledge));
            applicationReexaminationController.create(reexamination);
            knowledge.getReexaminationSet().add(reexamination);
        }
        if (null != question) {
            if (question.getReexaminationSet().size() > 0) {
                reexamination.setIsCreate(false);//是在更新，而不是在创建新的
            } else {
                reexamination.setIsCreate(true);//是在更新，而不是在创建新的
            }
            reexamination.setQuestionid(question);
            reexamination.setToteacher(teacherAdminController.getWhom2Examin(commonSession.getUserType(), question, null));
            applicationReexaminationController.create(reexamination);
            question.getReexaminationSet().add(reexamination);
        }

    }

    private List<Reexamination> getReexaminationListRecorderBy(User user, Integer status, int isQuestion) {
        //type=0表示知识点；type=1表示题目
        String temString = "";
        switch (isQuestion) {
            case 0:
                temString += " knowledgeId is not null";
                break;
            case 1:
                temString += " questionid is not null";
                break;
            default:
                return new LinkedList<>();
        }
        setStatus(status);
        switch (commonSession.getUserType()) {//审核员没考虑进来，因为审核员不录入
            case Student:
                temString += " and studentid=" + ((Student) (user)).getId();
                break;
            case Parent:
                temString += " and parentid=" + ((Parent) (user)).getId();
                break;
            case Teacher:
                temString += " and teacherid=" + ((TeacherAdmin) (user)).getId();
                break;
        }
        if (null != status) {//第一次审核
            temString += " and  status=" + status;
        }
        return facadeLocal.getQueryResultList("select * from Reexamination where " + temString);
    }

    public List<Reexamination> getReexaminationKnowledgeListRecorderBy(User user, Integer status) {
        setStatus(status);
        setSecondStatus(null);
        return getReexaminationListRecorderBy(user, status, 0);
    }

    public List<Reexamination> getReexaminationQuestionListRecorderBy(User user, Integer status) {
        setStatus(status);
        setSecondStatus(null);
        return getReexaminationListRecorderBy(user, status, 1);
    }

    public List<Reexamination> getReexaminationNeedToBeExamined(TeacherAdmin teacherAdmin, int status, int isQuestion, Integer dsecondStatus) {
        //type=0表示知识点；type=1表示题目
        String temString = "";
        switch (isQuestion) {
            case 0:
                temString += " knowledgeId is not null";
                break;
            case 1:
                temString += " questionid is not null";
                break;
            default:
                return new LinkedList<>();
        }
        setStatus(status);
        setSecondStatus(dsecondStatus);
        if (null == dsecondStatus) {//第1次审核
            temString += " and toteacher=" + teacherAdmin.getId();
            temString += " and  status=" + status;
        } else {//第2次审核的条件是第1次审核通过！！
            temString += " and  status=1 and status2nd=" + dsecondStatus;
        }
        return facadeLocal.getQueryResultList("select * from Reexamination where " + temString);
    }

    public boolean isMineDue2Reexamin(Knowledge knowledge, Question question) {
        switch (commonSession.getUserType()) {
            case Teacher:
                if (applicationStatusofresourcesController.getStatusofresources(getStatus()).equals(applicationStatusofresourcesController.getReexaming())) {
                    if (null != knowledge) {
                        Set<Reexamination> reexaminations = applicationReexaminationController.getReexamination4Knowledge(knowledge);
                        if (!reexaminations.isEmpty()) {
                            return statusEqual(status, (Reexamination) reexaminations.toArray()[0]);
                        }
                    } else if (null != question) {
                        return statusEqual(status, ((Reexamination) applicationReexaminationController.getReexamination4Question(question).toArray()[0]));
                    }
                }
                break;
            case Reexamin:
                if (applicationStatusofresourcesController.getStatusofresources(getSecondStatus()).equals(applicationStatusofresourcesController.getReexaming())) {
                    if (null != knowledge) {
                        Set<Reexamination> reexaminations = applicationReexaminationController.getReexamination4Knowledge(knowledge);
                        if (!reexaminations.isEmpty()) {
                            return statusEqual(secondStatus, ((Reexamination) reexaminations.toArray()[0]));
                        }
                    } else if (null != question) {
                        return statusEqual(secondStatus, ((Reexamination) applicationReexaminationController.getReexamination4Question(question).toArray()[0]));
                    }
                }
                break;
        }
        return false;
    }

    private boolean statusEqual(int status, Reexamination reexamination) {
        return reexamination.getStatus().getId() == status;
    }

    public String toKnowledgeReexamin(Knowledge knowledge) {
        // selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        knowledgeController.setSelected(knowledge);
        mainXhtml.setPageName(this.viewKnowledge);
        return null;
    }

    public String toQuestionReexamin(Question question) {
        //selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        questionController.setSelected(question);
        mainXhtml.setPageName(this.viewQuestion);
        return null;
    }

    public String setPassed(int isQuestion) {
        return setPassStatus(true, isQuestion);
    }

    public String setPassFailed(int isQuestion) {
        return setPassStatus(false, isQuestion);
    }

    private String setPassStatus(boolean passed, int isQuestion) {
        //type=0表示知识点；type=1表示题目
        Reexamination reexamination = null;
        if (isQuestion == 0) {
            Set<Reexamination> reexaminations = applicationReexaminationController.getReexamination4Knowledge(knowledgeController.getSelected());
            if (!reexaminations.isEmpty()) {
                reexamination = ((Reexamination) reexaminations.toArray()[0]);
            }
        } else if (isQuestion == 1) {
            reexamination = ((Reexamination) applicationReexaminationController.getReexamination4Question(questionController.getSelected()).toArray()[0]);
        }
        if (null != reexamination) {
            reexamination.setDetails(reexaminationSuggestion);
            if (!passed) {
                if (commonSession.getUserType() == UserType.Teacher || commonSession.getUserType() == UserType.Secretary) {
                    reexamination.setStatus(applicationStatusofresourcesController.getPassedFaild());
                } else if (commonSession.getUserType() == UserType.Reexamin) {
                    reexamination.setStatus2nd(applicationStatusofresourcesController.getPassedFaild());
                    reexamination.setStatus(applicationStatusofresourcesController.getPassedFaild());//二审没通过，则一审也自动没通过
                }
            } else {
                if (commonSession.getUserType() == UserType.Teacher || commonSession.getUserType() == UserType.Secretary) {
                    reexamination.setStatus(applicationStatusofresourcesController.getPassed());
                } else if (commonSession.getUserType() == UserType.Reexamin) {
                    reexamination.setStatus2nd(applicationStatusofresourcesController.getPassed());
                    reexamination.setStatus(applicationStatusofresourcesController.getPassedFaild());//二审没通过，则一审也自动没通过
                }
            }
            applicationReexaminationController.edit(reexamination);
            setReexaminationSuggestion("");
        }
        mainXhtml.setPageName(toExaminPage);
        return null;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getStatusName(Question question, Knowledge knowledge) {
        Statusofresources statusofresources = applicationReexaminationController.getStatus(question, knowledge);
        return commonSession.getResourceBound().getString(statusofresources.getMeaning());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSecondStatus() {
        return secondStatus;
    }

    public void setSecondStatus(Integer secondStatus) {
        this.secondStatus = secondStatus;
    }

    public String getReexaminationSuggestion() {
        return reexaminationSuggestion;
    }

    public void setReexaminationSuggestion(String reexaminationSuggestion) {
        this.reexaminationSuggestion = reexaminationSuggestion;
    }

    public boolean isIsReexamin() {
        return isReexamin;
    }

    public void setIsReexamin(boolean isReexamin) {
        this.isReexamin = isReexamin;
    }

    public boolean isReexaminPageNow() {
        return mainXhtml.getPageName().equals(this.examinpage)
                || mainXhtml.getPageName().equals(this.toExaminPage)
                || mainXhtml.getPageName().equals(this.viewKnowledge)
                || mainXhtml.getPageName().equals(this.viewQuestion);
    }
}
