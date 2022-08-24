package modelController.sessionController;

import entities.Knowledge;
import entities.Question;
import entities.Reexamination;
import entities.Student;
import entities.Subject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author haogs
 */
@Named
@SessionScoped
public class MyPublishedAll extends CommonModelController<Knowledge> implements java.io.Serializable {

    @Inject
    private modelController.applicationController.ReexaminationController applicationReexaminationController;
    @Inject
    private modelController.sessionController.KnowledgeController knowledgeController;
    @Inject
    private modelController.sessionController.SchoolController schoolController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.sessionController.SubjectController subjectController;
    @Inject
    private modelController.sessionController.MajorController majorController;
    @Inject
    private tools.UserMessagor userMessagor;
    private int type = 0;//0表示按照录入者所在班级查询，1表示按照录入的课程查询 
    // private List<StudentPublished> studentAllPublished = new LinkedList<>();
    protected Knowledge current;

    public Knowledge getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Knowledge();
        }
        return current;
    }

    public MyPublishedAll() {
    }

    //=================发布者的知识点和习题汇集==============begin===================
    public void collectMine() {
        collectData();
        // mainXhtml.setPageName(this.listpage);
    }

    private void collectData() {
        //准备相关变量
        if (null != majorController.getSelected() && null != majorController.getSelected().getId()) {
            HashMap<Student, List<Integer>> studentKnowledgeMap = new HashMap<>(), studentQuestionMap = new HashMap<>();
            List<Reexamination> allReexaminations;
            studentPublisthHashMap.clear();
            //通过学生名单获得每个学生录入的知识点与习题
            StringBuilder sb = new StringBuilder();
            if (isClass()) {
                sb.append("studentid in (");
                Set<Student> studentSet = schoolController.getSelected().getStudentSet();
                for (Student student : studentSet) {
                    sb.append(student.getId()).append(",");
                }
                String whereCond = sb.toString();
                if (whereCond.length() > 15) {//已经有了studentid in (
                    whereCond = whereCond.substring(0, whereCond.length() - 1);
                    whereCond += ")";
                    allReexaminations = applicationReexaminationController.getQueryResultList("select * from Reexamination where " + whereCond);
                } else {//没有学生
                    allReexaminations = new LinkedList<>();
                }
                //上面这两个Map分别存储知识点与习题，下面进行填充
                for (Reexamination reexamination : allReexaminations) {
                    if (reexamination.getKnowledgeid() != null) {
                        if (studentKnowledgeMap.get(reexamination.getStudentid()) == null) {
                            LinkedList knowledgeList = new LinkedList();
                            knowledgeList.add(reexamination.getKnowledgeid().getId());
                            studentKnowledgeMap.put(reexamination.getStudentid(), knowledgeList);
                        } else {
                            studentKnowledgeMap.get(reexamination.getStudentid()).add(reexamination.getKnowledgeid().getId());
                        }
                    } else if (reexamination.getQuestionid() != null) {
                        if (studentQuestionMap.get(reexamination.getStudentid()) == null) {
                            LinkedList questionList = new LinkedList();
                            questionList.add(reexamination.getQuestionid().getId());
                            studentQuestionMap.put(reexamination.getStudentid(), questionList);
                        } else {
                            studentQuestionMap.get(reexamination.getStudentid()).add(reexamination.getQuestionid().getId());
                        }
                    }
                }
            }
            if (isSubject()) {
                Subject subject = subjectController.getSelected();
                Set<Knowledge> knowledgeSet = subject.getKnowledgeSet();
                for (Knowledge knowledge : knowledgeSet) {
                    Set<Reexamination> reexamination = knowledge.getReexaminationSet();
                    if (reexamination.size() > 0) {
                        for (Reexamination localReexamination : reexamination) {
                            if (localReexamination.getStudentid() != null) {
                                if (!studentKnowledgeMap.containsKey(localReexamination.getStudentid())) {
                                    LinkedList<Integer> knowledgeList = new LinkedList<>();
                                    knowledgeList.add(localReexamination.getKnowledgeid().getId());
                                    studentKnowledgeMap.put(localReexamination.getStudentid(), knowledgeList);
                                } else {
                                    studentKnowledgeMap.get(localReexamination.getStudentid()).add(localReexamination.getKnowledgeid().getId());
                                }
                            }

                        }
                    }
                    Set<Question> question4KnowledgeSet = knowledge.getQuestionSet();
                    for (Question question : question4KnowledgeSet) {
                        Set<Reexamination> questionReexaminationSet = question.getReexaminationSet();
                        for (Reexamination localReexamination : questionReexaminationSet) {
                            if (null != localReexamination.getStudentid()) {
                                if (!studentQuestionMap.containsKey(localReexamination.getStudentid())) {
                                    LinkedList<Integer> questionList = new LinkedList<>();
                                    questionList.add(localReexamination.getQuestionid().getId());
                                    studentQuestionMap.put(localReexamination.getStudentid(), questionList);
                                } else {
                                    studentQuestionMap.get(localReexamination.getStudentid()).add(localReexamination.getQuestionid().getId());
                                }
                            }
                        }
                    }
                }
            }
            //HashMap填充完毕，下面开始完善studentPublished
            for (Entry<Student, List<Integer>> entry : studentKnowledgeMap.entrySet()) {
                //刚开始，map中一定不存在这个学生信息，所以要put
                studentPublisthHashMap.put(entry.getKey(), new LinkedList<>());
                studentPublisthHashMap.get(entry.getKey()).add(entry.getValue());
                if (studentQuestionMap.get(entry.getKey()) != null) {
                    studentPublisthHashMap.get(entry.getKey()).add(studentQuestionMap.get(entry.getKey()));
                    //已经添加进去了，所以删除，剩下的是还没添加进去的
                    studentQuestionMap.remove(entry.getKey());
                } else {
                    //这个学生没录入习题
                    studentPublisthHashMap.get(entry.getKey()).add(new LinkedList<>());
                }
            }
            //接着对剩下的questionMap进行处理,剩下的一定尚未包含在studentAllPublished中，即未录入知识点，只录入了习题
            for (Entry<Student, List<Integer>> entry : studentQuestionMap.entrySet()) {
                studentPublisthHashMap.put(entry.getKey(), new LinkedList<>());
                studentPublisthHashMap.get(entry.getKey()).add(new LinkedList<>());//对应的知识点为空表
                studentPublisthHashMap.get(entry.getKey()).add(studentQuestionMap.get(entry.getKey()));
            }
        } else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Please")
                    +commonSession.getResourceBound().getString("Select")
                    +commonSession.getResourceBound().getString("Major")
            );
        }
        getPageOperation().refreshData(new LinkedList(studentPublisthHashMap.entrySet()));
    }
//=================发布者的知识点汇集==============end===================
//------------------prepare View, edit, list,create------------------

    public void prepareList() {
        if (null == getItems()) {
            collectData();
        }
        //pageOperation.refreshData(studentAllPublished);
        //mainXhtml.setPageName(this.listpage);
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
        }
        return pageOperation.getItems();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isClass() {
        return type == 0;//0表示按照录入者所在班级查询，1表示按照录入的课程查询 
    }

    public boolean isSubject() {
        return type == 1;
    }

    HashMap<Student, List<List<Integer>>> studentPublisthHashMap = new HashMap<>();
//
//    class StudentPublished implements java.io.Serializable{
//        private Student student;
//        private List<Integer> knowledgeList;
//        private List<Integer> questionList;
//        public Student getStudent() {
//            return student;
//        }
//
//        public List<Integer> getKnowledgeList() {
//            return knowledgeList;
//        }
//        public List<Integer> getQuestionList() {
//            return questionList;
//        }
//
//    }
}
