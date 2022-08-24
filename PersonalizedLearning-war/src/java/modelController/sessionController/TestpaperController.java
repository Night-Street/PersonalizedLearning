package modelController.sessionController;

import entities.Knowledge;
import entities.Leadpoint;
import entities.Question;
import entities.Student;
import entities.Subject;
import entities.TeacherAdmin;
import entities.Testpaper;
import entities.WrongquestionCollection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("testpaperController")
@SessionScoped
public class TestpaperController extends CommonModelController<Testpaper> implements Serializable {

    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private StudenttestpaperController studentTestpaperController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private SchoolController schoolController;
    @Inject
    private SubjectController subjectController;
    @Inject
    private LeadpointController leadpointController;
    @Inject
    private modelController.applicationController.TestpaperController applicationTestpaperController;
    @Inject
    private modelController.applicationController.LeadpointController applicationLeadpointController;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.WrongquestionCollectionController applicationWrongquestionCollectionController;
    @Inject
    private modelController.sessionController.KnowledgeController knowledgeController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.applicationController.ContributorsController applicationContributorsController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.applicationController.TeacherAdminController applicationTeacherAdminController;
    private int specifiedInterval;
    private List<Testpaper> testpaperList;
    private String typeOfPraOrExam;
    private List<Subject> subjectList4Testpaper = new LinkedList<>();
    private final String createpage = "testpaper/Create",
            viewpage = "testpaper/View", secondPage = "testpaper/CreateSecond",
            handmake = "testpaper/Handmake",
            instantTest = "testpaper/instantTest",
            testentrance = "testpaper/entrance",
            testPaperView = "testpaper/testPaperView";

    public TestpaperController() {
    }
    protected Testpaper current;

    public Testpaper getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current=new Testpaper();
        }
        return current;
    }
    public void init() {
        if (null != teacherAdminController.getLogined()) {//是教师登录，则准备该教师的试卷
            pageOperation.setDataModelList(getTestpaperList());
        }
    }


    public void setSelected(Testpaper testpaper) {
        current = testpaper;
    }

    public void edit(Testpaper testpaper) {
        setSelected(testpaper);
        applicationTestpaperController.edit(testpaper);
        //this.evictForeignKey();
    }

    public List<Testpaper> getTestpaperList() {
        if (null == testpaperList || testpaperList.isEmpty()) {
            testpaperList = new LinkedList<>();
        }
        return testpaperList;
    }

    public void creatTestpapers(int type) {
        setPropertiesofTestpaper();
        switch (type) {
            case 0:
                mainXhtml.setPageName(secondPage);
                break;
            case 1:
                mainXhtml.setPageName(handmake);
                break;
        }
    }

    @Inject
    QuestionController questionController;

    public String createHandmakeTestpaper() {
        current.setIstest(true);
        applicationTestpaperController.create(current);
        List<Question> selectedQuestions = questionController.getSelectedQuestions();
        studentController.getSelectedSchoolStudents().forEach((student) -> {
            studentTestpaperController.generateTestpaper4Question(selectedQuestions, student, current);
        });
        userMessagor.addMessage(commonSession.getResourceBound().getString("Finished"));
        //JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("Finished"));
        return viewpage;
    }

    private void setPropertiesofTestpaper() {
        long interval = (getSelected().getEndtime().getTime() - getSelected().getStart().getTime()) / 60000;
        if (interval <= getSpecifiedInterval() || Calendar.getInstance().after(current.getSpecifieddate())) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Time")
                    + commonSession.getResourceBound().getString("Error"));
//            JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("Time")
//                    + commonSession.getResourceBound().getString("Error")
//            );
            return;
        }
        current.setSpecifiedInterval(getSpecifiedInterval() * 60000);
        current.setSchoolid(schoolController.getSelected());
        StringBuilder sb = new StringBuilder();
        this.getSubjectList4Testpaper().forEach((subject) -> {
            sb.append(",").append(subject.getId());
        });
        String temString1 = sb.toString();
        temString1 = temString1.substring(1);
        current.setSubjectids(temString1);
        current.setCreateteacherid(teacherAdminController.getLogined());
    }

    public void creatTestpapers4TeachSchool(boolean isTest) {//Finished the test paper
        //Create testpapers according to school
        //1. According to login teacher and school, get the subjects in this semester
        //2. According to the subjects, get the knowledges;
        List<Knowledge> knowledgesList = new LinkedList<>();
        getSubjectList4Testpaper().forEach((subject) -> {
            knowledgesList.addAll(applicationKnowledgeController.getKnowledgeList4Subject(subject));
        });
        //remove those knowledge that named as "start" and "end" and those without questionSet
        List<Knowledge> toBeRemovedKnowledges = new LinkedList<>();
        for (Knowledge knowledge : knowledgesList) {
            if (knowledge.getQuestionSet().isEmpty()) {
                toBeRemovedKnowledges.add(knowledge);
            }
        }
        knowledgesList.removeAll(toBeRemovedKnowledges);
        //3. According to the knowledges, get the testpapers for every student
        current.setIstest(isTest);

        applicationTestpaperController.create(current);
        studentController.getSelectedSchoolStudents().forEach((student) -> {
            studentTestpaperController.generateTestpaper4KnowledgeList(knowledgesList, student, current);
        });
        mainXhtml.setPageName(testentrance);
        userMessagor.addMessage(commonSession.getResourceBound().getString("Finished"));
        //JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("Finished")); 
    }

    public String getTypeOfPraOrExam() {
        return typeOfPraOrExam;
    }

    public void setTypeOfPraOrExam(String typeOfPraOrExam) {
        this.typeOfPraOrExam = typeOfPraOrExam;
    }

    public int getSpecifiedInterval() {
        return specifiedInterval;
    }

    public void setSpecifiedInterval(int specifiedInterval) {
        this.specifiedInterval = specifiedInterval;
    }

    public String creatTestpapersForStudent(boolean according2Leadpoint) {
        int interval = 100;
        //Create testpapers according to school
        //1. According to login student, get the subjects in this semester
        Subject subject = subjectController.getSelected();
        //取得当前学生
        Student curStudent = studentController.getLogined();
        //2. According to the subjects, get the knowledges;
        List<Knowledge> knowledgesList = new LinkedList<>();
        if (according2Leadpoint) {//Knowledge list is from the leadpoint
            leadpointController.setSelected(applicationLeadpointController.getLeadpointFront4Subject4Student(subject, curStudent));
            leadpointController.refreshLeadpointList();
            List<Leadpoint> leadpointList = leadpointController.getLeadpointList();
            if (null != leadpointList && leadpointList.size() > 1) {
                for (int i = 0; i < leadpointList.size(); i++) {
                    knowledgesList.addAll(applicationKnowledgeController.getKnowledgesList4LeadingPoint(leadpointList.get(i)));
                }
            }
        }

        // List<Knowledge> knowledgesList = new LinkedList<>();
        //拿到当前课程下的所有知识点
        //拿到当前课程对应的学生的知识点前沿以前的知识点与错题集对应的知识点
        //拿到学生的知识点前沿
        List<Leadpoint> leadpoints = applicationLeadpointController.getQueryResultList("select * from leadpoint where student_id=" + curStudent.getId() + " and subjectid=" + subject.getId() + "order by id desc");
        Leadpoint leadpoint = leadpoints.isEmpty() ? null : (Leadpoint) leadpoints.get(0);
        List<Knowledge> curKnowledges = new ArrayList<>();
        if (leadpoint != null) {
            if (!leadpoint.getKnowledgeId().trim().equals("")) {
                String[] knowledgeStrings = leadpoint.getKnowledgeId().split(",");
                for (String knowTem : knowledgeStrings) {
                    curKnowledges.add(applicationKnowledgeController.find(Integer.valueOf(knowTem)));
                }
            }
        }

        //写入当前的知识点
        //knowledgesList.addAll(curKnowledges);
        //写入当前知识点的后继知识点
        for (Knowledge curKnowledge : curKnowledges) {
            knowledgesList.addAll(knowledgeController.getKnowledgeToRecursion(curKnowledge));
        }
        //写入错题集里面的所有的知识点
        List<WrongquestionCollection> wqcList = new ArrayList<>(curStudent.getWrongquestionCollectionSet());
        for (WrongquestionCollection wqc : wqcList) {
            if (Objects.equals(wqc.getQuestionId().getKnowledgeId().getSubjectId().getId(), subject.getId())) {
                knowledgesList.add(wqc.getQuestionId().getKnowledgeId());
            }
        }
        List<Knowledge> toBeRemovedKnowledges = new LinkedList<>();
        for (Knowledge knowledge : knowledgesList) {
            if (knowledge.getQuestionSet().isEmpty()) {
                toBeRemovedKnowledges.add(knowledge);
            }
        }
        knowledgesList.removeAll(toBeRemovedKnowledges);
        //3. According to the knowledges, get the testpapers for every student
        getSelected().setSpecifiedInterval(interval * 60000);
        getSelected().setIstest(false);
        getSelected().setSchoolid(studentController.getLogined().getSchoolId());
        getSelected().setScore(100);
        getSelected().setSubjectids(subjectController.getSelected().getId().toString());
        getSelected().setDegree(2);
        applicationTestpaperController.create(getSelected());
        if (knowledgesList.size() <= 0) {//Generate knowledge list
            String[] subjectIds = getSelected().getSubjectids().split(",");
            List<List<Integer>> randomScope = new ArrayList<>();
            //randomScope--- Store knowledge_id in this scope for the element in knowledgeList
            for (int i = 0; i < subjectIds.length; i++) {
                List<Integer> temList = applicationContributorsController.getQueryIntList("SELECT id from knowledge where subject_id=" + subjectIds[i]);
                randomScope.add(temList);
            }
            int countSum = 0;//The number of the candidate questions
            for (int i = 0; i < subjectIds.length; i++) {
                countSum += randomScope.get(i).size();
            }
            double sampleRate = StaticFields.QUESTIONNUMBER * 1.0 / countSum;

            List<Integer> sampleNumber = new ArrayList<>();
            //The number of questions to be sampled from i-th subject 
            for (int i = 0; i < subjectIds.length; i++) {
                sampleNumber.add(Double.valueOf(sampleRate * randomScope.get(i).size()).intValue());
            }
            //Sample knowledges from list
            HashSet<Knowledge> knowledgeSet = new HashSet<>();
            Random random = new Random();
            for (int i = 0; i < subjectIds.length; i++) {//For every subject
                for (int j = 0; j < sampleNumber.get(i); j++) {//sampling
                    knowledgeSet.add(applicationKnowledgeController.find(randomScope.get(i).get(
                            (int) (random.nextFloat() * randomScope.get(i).size())
                    )));
                }
            }

            knowledgesList = new ArrayList<>(knowledgeSet);
        }
        studentTestpaperController.generateTestpaper4KnowledgeList(knowledgesList, curStudent, getSelected());
        //System.out.println("knowledge number" + knowledgesList.size() + "====================000000000000?");
        userMessagor.addMessage(commonSession.getResourceBound().getString("Finished"));
        //JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("Finished"));
        return null;
    }

    public List<Subject> getSubjectList4Testpaper() {
        return subjectList4Testpaper;
    }

    public void setSubjectList4Testpaper(List<Subject> subjectList4Testpaper) {
        this.subjectList4Testpaper = subjectList4Testpaper;
        if (subjectList4Testpaper.size() > 0) {
            subjectController.setSelected(subjectList4Testpaper.get(0));
        }
    }

    //Page changes
    public void viewScore() {
        mainXhtml.setPageName(viewpage);
    }

    private Set<Testpaper> viewedTestpapers;

    public void viewTestpaper() {
        //如果是教师登录，根据教师信息(是testpaper中的一列)，获得其试卷信息(testpaper)，进一步获得使用该试卷规则(knowledge)生成的学生试卷(question)，从而可以查看任一学生的试卷信息。
        TeacherAdmin teacher = teacherAdminController.getLogined();//一定不为空，因为只有登录进来，才能看到
        //为 viewedTestpapers赋初值 
        viewedTestpapers = teacher.getTestpaperSet();
        schoolController.getCadidateSchools().clear();
        viewedTestpapers.forEach(testpaper -> {
            schoolController.getCadidateSchools().add(testpaper.getSchoolid());
        });

        mainXhtml.setPageName(testPaperView);
    }

    public void toCreateTestpaper() {
        mainXhtml.setPageName(createpage);
    }

    public void inputTestpaper() {
        mainXhtml.setPageName(instantTest);
    }
    String replaceString = "";

    public String getReplaceString() {
        return replaceString;
    }

    public void setReplaceString(String replaceString) {
        this.replaceString = replaceString;
    }
    String temString = "";

    public void setTemString(String inputString) {
        temString = inputString;
    }

    public String getTemString() {
        if (applicationTestpaperController.getTemString().length() == 0 || replaceString.trim().length() > 0) {
            return "";
        }
        if (null != studentController.getLogined()) {
            return applicationTestpaperController.getTemString().replaceAll(replaceString, studentController.getLogined().getName());
        } else {
            return applicationTestpaperController.getTemString();
        }
    }

    public void ok() {
        if (temString.contains(replaceString)) {
            applicationTestpaperController.setTemString(temString);
        } else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + ":"
                    + commonSession.getResourceBound().getString("Not")
                    + commonSession.getResourceBound().getString("Contain") + "\"" + replaceString + "\"");
//            JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("Failed") + ":"
//                    + commonSession.getResourceBound().getString("Not")
//                    + commonSession.getResourceBound().getString("Contain") + "\"" + replaceString + "\""
//            );
        }
        userMessagor.addMessage(commonSession.getResourceBound().getString("Finished"));
        //JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("Finished"));
        mainXhtml.setPageName(testentrance);
    }

    public Set<Testpaper> getViewedTestpapers() {
        return viewedTestpapers;
    }
}
