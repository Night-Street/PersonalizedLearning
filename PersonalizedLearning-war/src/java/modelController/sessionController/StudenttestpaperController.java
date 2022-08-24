package modelController.sessionController;

import entities.Knowledge;
import entities.Leadpoint;
import entities.Question;
import entities.School;
import entities.Student;
import entities.Studenttestpaper;
import entities.Subject;
import entities.Testpaper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import modelController.viewerController.StudentScore;
import tools.PersonalSessionSetup;
import tools.StaticFields;
import tools.ga.GA4Testpaper;

@Named
@SessionScoped
public class StudenttestpaperController extends CommonModelController<Studenttestpaper> implements Serializable {
    @Inject
    private SubjectController subjectController;
    @Inject
    private SchoolController schoolController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private QuestionController questionController;
    @Inject
    private LeadpointController leadpointController;
    @Inject
    private TestpaperController testpaperController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.LeadpointController applicationLeadpointController;
    @Inject
    private modelController.applicationController.QuestionController applicationQuestionController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.applicationController.StudenttestpaperController applicationStudenttestpaperController;
    @Inject
    private modelController.applicationController.TestpaperController applicationTestpaperController;
    @Inject
    private tools.PublicFields applicationPublicFields;
    @Inject
    private PersonalSessionSetup personalSessionSetup;
    @Inject
    private tools.UserMessagor userMessagor;
    private HashMap<Question, String> studentanswerHashMap;//试题id/学生答案 
    private Set<Question> rightquestionList;//对题集合
    private Set<Question> wrongquestionList;//错题集合
    private HashSet<String> otherreasonSet;
    private final String tableName = "studenttestpaper", createpage = "Create",
            viewpage = "testpaper/View", exportpage = "testpaper/Readypages";
    private List<Studenttestpaper> studenttestpaperList;
    private final List<StudentScore> examinationList = new LinkedList<>();
    private final List<StudentScore> practiceList = new LinkedList<>();
    private final HashMap<Student, List<Studenttestpaper>> examinationTestpaper = new HashMap<>(); //存储考试的testpaper
    //存储平时练习的testpaper
    private final HashMap<Student, List<Studenttestpaper>> practiceTestpaper = new HashMap<>();//存储平时练习的testpaper
    private String typeOfPraOrExam;
    protected Studenttestpaper current;
   public StudenttestpaperController() {
    }
    public Studenttestpaper getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Studenttestpaper();
        }
        return current;
    }
 
    public void setSelected(Studenttestpaper studenttestpaper){
        current=studenttestpaper;
    }
//=======================判卷================================
    float score = 0, sum = 0;
    //  private boolean newScoreNeeded = false;//如果是仅仅刷新页面，而没有重新做题，则不再重新计算分数和保存数据到数据库

    private final List<Question> temWrongQuestionList = new LinkedList<>();

    //用来记录当前考试过程中的错题，在批改试卷过程中加入
    //当开始新考试时，该列表要被清空
    public List<Question> getTemWrongQuestionList() {
        return temWrongQuestionList;
    }

    public void temWrongQuestionListClear() {
        temWrongQuestionList.clear();
    }

    public String correctTestpaper() {//判卷
        if (current == null) {
            return "";
        }
        // if (isNewScoreNeeded()&& null==current) {
        score = 0;//学生得分
        sum = 0;//整张试卷的分数
        getRightquestionList().clear();
        getWrongquestionList().clear();
        temWrongQuestionList.clear();
        getStudentanswerHashMap().forEach((question, studentAnswer) -> {
            sum += question.getScore();
            switch (question.getType()) {
                case StaticFields.JUDGMENT:
                case StaticFields.SINGLESELECTION:
                case StaticFields.MULTISELECTION:
                    score += getScore(question, studentAnswer);
                    break;
                case StaticFields.SINGLEFILL:
                    score += getSingFillScore(question, studentAnswer);
                    break;
                case StaticFields.MULTIFILL:
                    score += getMultiFillScore(question, studentAnswer);
                    break;
                case StaticFields.OBJECTIVEPROGRMA://能够自动判卷
                    score += getSingFillScore(question, studentAnswer);
                    break;
                case StaticFields.SUBJECTIVEPROGRAM://不能自动判卷
                    score += getSingFillScore(question, studentAnswer);
                    break;
                case StaticFields.SIMPLEANSWER://需要加入语义距离计算
                    //本来应该调用方法getSimpleAnswerScore(question, studentAnswer);，但由于成本太高（需要大数据平台，需要深度学习），目前尚未实现，所以先不实现
                    score += getSingFillScore(question, studentAnswer);
                    break;
            }
        });
        //保存分数，错题集
        current.setTestscore((double) Math.round(score / sum * 100));
        if (!getWrongquestionList().isEmpty()) {
            String wrongQuestionString = "";
            wrongQuestionString = getWrongquestionList().stream().map((question) -> "," + question.getId()).reduce(wrongQuestionString, String::concat);
            wrongQuestionString = wrongQuestionString.substring(1);
            if (current.getFirstWrongquestionids().trim().length() <= 0) {//是第一次做这套试卷
                current.setFirstWrongquestionids(wrongQuestionString);
                current.setOtherwrongids(wrongQuestionString);
            } else {//不是第一次做这个试卷了
                current.setOtherwrongids(wrongQuestionString);
            }
        }
        //根据错题集和对题集更新知识前点前
        leadpointController.updateLeadPoint(getRightquestionList(), getWrongquestionList());
        applicationStudenttestpaperController.edit(current);
        //  current = null;
        // }
        return score + "/" + sum;
    }

    private int getScore(Question question, String studentAnswer) {
        int temscore = 0;
        try {
            String questionString = question.getAnswer().replaceAll(" ", "");
            String studentTemAnswer = studentAnswer.replaceAll(" ", "");
            if (questionString.equals(studentTemAnswer)) {
                temscore = question.getScore();
                getRightquestionList().add(question);
            } else {
                getWrongquestionList().add(question);
                temWrongQuestionList.add(question);
            }
        } catch (Exception e) {
        }
        return temscore;
    }

    private int getSimpleAnswerScore(Question question, String studentAnswer) {
//        double similarity=applicationAppparaminfoController.getSimilarValue(
//                question.getAnswer().replaceAll(" ", ""), 
//                studentAnswer.replaceAll(" ", ""));
        int temscore = 0;
//        try {
//            if (similarity>=StaticFields.SIMPLEANSWERCORECTTHRESHOLD) {//认为掌握
//                temscore = (int)Math.ceil(question.getScore()*similarity);
//                getRightquestionList().add(question);
//            } else {
//                getWrongquestionList().add(question);
//                temscore = (int)Math.floor(question.getScore()*similarity);
//                temWrongQuestionList.add(question);
//            }
//        } catch (Exception e) {
//        }
        return temscore;
    }

    public void viewScore() {
        mainXhtml.setPageName(viewpage);
    }

    public void toCreateTestpaper() {
        mainXhtml.setPageName(createpage);
    }

    public void toExportTestpaper() {
        mainXhtml.setPageName(exportpage);
    }

    private int getSingFillScore(Question question, String studentAnswer) {
        int temscore = 0;
        //Match with multi-option and remove the meaningless character
        String[] questionAnswers = question.getAnswer().split(StaticFields.SECONDDELIMITED);//候选正确的选项由SECONDDELIMITED决定
        boolean correct = false;
        for (int i = 0; i < questionAnswers.length; i++) {//候选答案个数
            if (studentAnswer.replaceAll(" ", "").equals(questionAnswers[i].replaceAll(" ", ""))) {
                temscore = question.getScore();
                getRightquestionList().add(question);
                correct = true;
                break;
            }
        }
        if (!correct) {
            getWrongquestionList().add(question);
            temWrongQuestionList.add(question);
            temscore = 0;
        }
        return temscore;
    }

    private int getMultiFillScore(Question question, String studentAnswer) {
        //多项填空题批改
        int temscore = 0;
        String[] studentAnswerArray = studentAnswer.split(StaticFields.THIRDDELIMITED);//学生答案的各个答案分隔开
        String[] referenceAnswerStrings = question.getAnswer().split(StaticFields.THIRDDELIMITED);//参考答案的各个答案分隔开
        HashSet<String> questionAnswers = new HashSet<>();
        for (String referAns : referenceAnswerStrings) {
            //在集合中保存的是去掉多余空格的字符串
            questionAnswers.add(referAns.replaceAll(" ", ""));
        }
        int correctNum = 0;
        for (String stuAns : studentAnswerArray) {
            //学生提供的答案个数
            String temString = stuAns.replaceAll(" ", "");
            if (questionAnswers.contains(temString)) {
                questionAnswers.remove(temString);
                correctNum++;
            }
        }
        //是否完全掌握
        boolean completeMaster = false;
        if (correctNum == referenceAnswerStrings.length) {
            //完全掌握，不用计算分值了
            completeMaster = true;
            temscore = question.getScore();
        } else {
            float corr = (float) correctNum, allNum = (float) referenceAnswerStrings.length;
            float pro = corr / allNum;
            float ftemScore = question.getScore() * pro;
            temscore = (int) (ftemScore);
            completeMaster = false;
        }
        if (completeMaster) {
            getRightquestionList().add(question);

        } else {
            getWrongquestionList().add(question);
            temWrongQuestionList.add(question);
        }
        return temscore;
    }

    //批改所有的未批改的试卷
    public void correctAllTestPapers(String studentIds) {
        HashMap<Question, String> studentAnswer4Question = new HashMap<>();
        List<Question> questions = new LinkedList<>();
        String[] studentAnswer;
        int i;
        List<Studenttestpaper> testpapers = applicationStudenttestpaperController.getQueryResultList("select * from studenttestpaper where testscore=0 and length(student_Answer)>0 and student_id in (" + studentIds + ")");
        for (Studenttestpaper testpaper : testpapers) {
            i = 0;
            setSelected( (Studenttestpaper) testpaper);
            studentAnswer = current.getStudentAnswer().split(StaticFields.FIRSTDELIMITED);
            questions.clear();
            studentAnswer4Question.clear();
            questions = applicationQuestionController.getTestQuestion(current);
            for (Question question : questions) {
                studentAnswer4Question.put(question, studentAnswer[i++]);
            }
            setStudentanswerHashMap(studentAnswer4Question);
            correctTestpaper();
        }
    }

    public void exportTestPapers() {
        //生成文件，并给出超链接
        mainXhtml.setPageName(exportpage);
    }

    //根据班级和学科与教师从数据库中获得相应的试卷
    //Calculte the score for the school
    public String getScore() {
        //type==1 means to get the score
        //type==2 means to export the practice and the testpapers
        //Prepare the datatable
        examinationList.clear();
        practiceList.clear();
        practiceTestpaper.clear();
        examinationTestpaper.clear();
        //Prepare studentIds
        String studentIdString = "";
        School currentSchool = schoolController.getSelected();
        studentIdString = currentSchool.getStudentSet().stream().map(student -> "," + student.getId()).reduce(studentIdString, String::concat);
        studentIdString = studentIdString.substring(1);
        //search the testpaper
        List<Testpaper> testpapers = applicationTestpaperController.getQueryResultList("select * from testpaper "
                + " where locate('" + subjectController.getSelected().getId() + "',subjectIds)>0"
                //+ " and createteacherId=" + teacherAdminController.getLoginTeacherAdmin().getId()
                + " and schoolid=" + currentSchool.getId()
        );
        //过滤课程======================begin===================
        testpapers = testpapers.stream().filter((Testpaper t) -> {//下面是实现Predicate<T>类中的boolean test方法，
            String[] subjects = t.getSubjectids().split(",");
            for (String subject : subjects) {
                if (subject.equals(String.valueOf(subjectController.getSelected().getId()))) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        //过滤课程======================end===================
        String testpaperIdString = "";
        testpaperIdString = testpapers.stream().map(testpaper -> "," + testpaper.getId()).reduce(testpaperIdString, String::concat);
        List<Studenttestpaper> testpapersList = new LinkedList<>();
        if (testpaperIdString.trim().length() > 0) {
            testpaperIdString = testpaperIdString.substring(1);
            testpapersList = applicationStudenttestpaperController.getQueryResultList("select * from studenttestpaper"
                    + "  where testpaperid in (" + testpaperIdString + ") "
                    + " and student_id in (" + studentIdString + ")");
        }
        //put the testpaperlist into two HashMap which represents the practice and examination respectively
        for (Studenttestpaper studenttestpaper : testpapersList) {
            Studenttestpaper studenttestpaper1 = (Studenttestpaper) studenttestpaper;
            if (studenttestpaper1.getTestpaperId().getIstest()) {
                if (null == examinationTestpaper.get(studenttestpaper1.getStudentId())) {
                    examinationTestpaper.put(studenttestpaper1.getStudentId(), new LinkedList<>());
                    examinationTestpaper.get(studenttestpaper1.getStudentId()).add(studenttestpaper1);
                } else {//only get the last examination, therefore clear those records that are not the last one
                    if (examinationTestpaper.get(studenttestpaper1.getStudentId()).get(0).getId() > studenttestpaper.getId()) {
                        //the stored is the last one, therefore,just keep it

                    } else {//the new comming is the last one, first remove the existed and then add it to the list
                        examinationTestpaper.get(studenttestpaper1.getStudentId()).clear();
                        examinationTestpaper.get(studenttestpaper1.getStudentId()).add(studenttestpaper1);
                    }
                }

            } else {
                if (null == practiceTestpaper.get(studenttestpaper1.getStudentId())) {
                    practiceTestpaper.put(studenttestpaper1.getStudentId(), new LinkedList<>());
                }
                practiceTestpaper.get(studenttestpaper1.getStudentId()).add(studenttestpaper1);
            }
        }
        //prepare for the data for the datatable
        prepareDataTable(practiceTestpaper, practiceList);
        prepareDataTable(examinationTestpaper, examinationList);
        return null;
    }

    public String export2Excel() {
        //Get Data from testMap
        userMessagor.addMessage(commonSession.getResourceBound().getString("Excel") + " "
                + commonSession.getResourceBound().getString("OK"));
        return null;
    }

    private void prepareDataTable(HashMap<Student, List<Studenttestpaper>> testMap, List<StudentScore> studentScoresList) {
        studentScoresList.clear();
        testMap.forEach((Student student, List<Studenttestpaper> testpaperList1) -> {
            float sum1 = 0;
            List<Double> scoreList = new LinkedList<>();
            for (Studenttestpaper studenttestpaper : testpaperList1) {
                sum1 += studenttestpaper.getTestscore();
                scoreList.add(studenttestpaper.getTestscore());
            }
            StudentScore temStudentScore = new StudentScore();
            temStudentScore.setStudent(student);
            temStudentScore.setScore(scoreList);
            temStudentScore.setAverageScore(sum1 / testpaperList1.size());
            studentScoresList.add(temStudentScore);
        });
    }

    public HashMap<String, List<StudentScore>> getScoreMap() {
        HashMap<String, List<StudentScore>> result = new HashMap<>();
        result.put(applicationStudenttestpaperController.getPracticeString(), practiceList);
        result.put(applicationStudenttestpaperController.getExaminiationString(), examinationList);
        return result;
    }

    public HashMap<String, HashMap<Student, List<Studenttestpaper>>> getTestpaperMap() {
        if (examinationTestpaper.size() == 0 || practiceTestpaper.size() == 0 || subjectController.equals(sum) || subjectController.isRenewed()) {
            getScore();
        }
        HashMap<String, HashMap<Student, List<Studenttestpaper>>> result = new HashMap<>();
        result.put(applicationStudenttestpaperController.getExaminiationString(), examinationTestpaper);
        result.put(applicationStudenttestpaperController.getPracticeString(), practiceTestpaper);
        return result;
    }

//=======================得到学生的当前准备做的试卷========
//=====注意这个试卷是只为当前的学生，根据其知识点前沿生成的测试试卷===================
    //isTest dedicate whether the tesp paper is for examination or just practice, true for examination and falsue for practice
    public HashSet<Studenttestpaper> getStudentTestPaper(boolean isTest, Subject subject) {
        return new HashSet<>(applicationStudenttestpaperController.getStudenttestpapers(studentController.getLogined(), subjectController.getSelected(), false, isTest));
    }

    public Studenttestpaper getTestpaper4leadPoint() {
        //学生自己要求生成的，用于学生自己巩固学习的
        //下面是根据知识点前沿生成测试题，以及对知识点前沿进行更新
        Leadpoint leadpoint = applicationLeadpointController.getLeadpointFront4Subject4Student(subjectController.getSelected(), studentController.getLogined());
        leadpointController.setSelected(leadpoint);
        if (null != leadpoint) {
            List<Studenttestpaper> studenttestpapersList = new LinkedList<>();
            studenttestpapersList.addAll(applicationStudenttestpaperController.getQueryResultList("select * from STUDENTTESTPAPER where "
                    + "leadpoint_id= " + leadpoint.getId() + " and finished= false"));
            if (!studenttestpapersList.isEmpty()) {//取尚未完成的试卷
                setSelected( (Studenttestpaper) studenttestpapersList.get(0));
            } else {//已经全部提交答案或者还没有任何已经生成的试卷，因此，需要生成一份新的试卷
                generateTestpaper4Leadpoint(leadpoint, studentController.getLogined());
            }
        }
        return current;
    }

    public List<Studenttestpaper> getStudentTestpaperList() {
        if (null == studenttestpaperList || studenttestpaperList.isEmpty()) {
            studenttestpaperList = new LinkedList<>();
        }
        return studenttestpaperList;
    }

    public List<SelectItem> getCurrentStudenttestpapers() {
        List<SelectItem> result = new LinkedList<>();
        if (studentController.getSelected().getId() != null) {
            Set<Studenttestpaper> stutestpapers = studentController.getSelected().getStudenttestpaperSet();
            stutestpapers.forEach(sttp -> {
                result.add(new SelectItem(sttp, applicationStudenttestpaperController.getName(sttp)));
            });
        }
        return result;
    }

    /*
    *Only for self practice
     */
    private void generateTestpaper4Leadpoint(Leadpoint leadpoint, Student student) {
        /*对KNF中的每个知识点进行如下处理：
         {
            1. 当前的知识点是否有习题？
          1.1 有，转2
          1.2 无，转3
        2. 选择不在对题集中的题目，生成候选题目；
          3. 则继续循环
            }
            4. 获得的题目集合是否为空？
          4.1 是，则
               4.1.1 知识点前沿前移；
                  4.1.1.1 是否已经到达end结点？
                   4.1.1.1.1 是，结束，并通知用户，练习结束；
                       4.1.1.1.2 否，基于该知识点继续调用本方法
         4.2 否，则返回题目集合；
         */

        //1. 当前的知识点是否有习题？
        List<Knowledge> knowledgesList = applicationKnowledgeController.getKnowledgesList4LeadingPoint(leadpoint);
        List<Question> questionsList = new LinkedList<>();
        for (Knowledge knowledge : knowledgesList) {//对KNF中的每个知识点进行如下处理：
            if (knowledge.getQuestionSet().isEmpty()) { //1. 当前的知识点是否有习题？
                continue;// 3. 有无后继？
            } else {//2. 选择不在对题集中的题目，生成候选题目；
                Set<Question> temQuestionsSet = new HashSet<>();
                temQuestionsSet.addAll(knowledge.getQuestionSet());//不破坏原集合
                temQuestionsSet.removeAll(questionController.getSubjectCorrectQuestion().get(knowledge.getSubjectId()));
                questionsList.addAll(temQuestionsSet);
            }
        }

        if (questionsList.isEmpty()) {// 4. 获得的题目集合是否为空？ 4.1 是，则
            //4.1.1 知识点前沿前移；
            Leadpoint nextLeadpoint = applicationLeadpointController.getNextLeadpoint(leadpoint);
            //4.1.1.1 是否已经到达end结点？
            if (applicationLeadpointController.isEnd(nextLeadpoint))//4.1.1.1.1 是，结束，并通知用户，练习结束；
            {
                userMessagor.addMessage(commonSession.getResourceBound().getString("KnowledgeOver"));
            } else {   //4.1.1.1.2 否，基于该知识点继续调用本方法
//                applicationLeadpointController.create(nextLeadpoint);
//                leadpoint=nextLeadpoint;
//                generateTestpaper4Leadpoint(nextLeadpoint, student);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Noquestions"));
            }
        } else {//4.2 否，则返回题目集合；
            setSelected( new Studenttestpaper());
            String questionIds = "";
            questionIds = questionsList.stream().map((question) -> "," + question.getId()).reduce(questionIds, String::concat);
            questionIds = questionIds.substring(1);
            //看看是否有相同的试卷了
            Studenttestpaper sameTestpapers = null;
            boolean existed = false;
            for (Studenttestpaper tp : student.getStudenttestpaperSet()) {
                if (tp.getQuestionIds().equals(questionIds)) {
                    sameTestpapers = tp;
                    existed = true;
                    break;
                }
            }
            if (!existed) {
                current.setTestpaperId(null);
                current.setQuestionIds(questionIds);
                current.setStudentId(student);
                current.setLeadpointId(leadpoint);
                current.setAnsweredInterval(0);
                current.setFinished(false);
                current.setTestscore(0d);
                current.setStudentAnswer("");
                current.setFirstWrongquestionids("");
                applicationStudenttestpaperController.create(current);
            } else {//存在相同的试卷
                setSelected( sameTestpapers);
                current.setAnsweredInterval(0);
                current.setFinished(false);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Thisis")
                        + commonSession.getResourceBound().getString("Old")
                        + commonSession.getResourceBound().getString("Testpaper")
                );
            }

        }
    }

    public void generateTestpaper4Question(List<Question> questionList, Student student, Testpaper testpaper) {
        //考虑做题时所需要的时间与安排的时间之间的关系，是否也作为一个约束条件 ？
        setSelected( new Studenttestpaper());
        String questionIds = "";
        questionIds = questionList.stream().map((question) -> "," + question.getId()).reduce(questionIds, String::concat);
        if (questionIds.length() > 1) {
            questionIds = questionIds.substring(1);
            dealwithDuplicateTestpaper(questionIds, student, testpaper);
        }
    }

    private void dealwithDuplicateTestpaper(String questionIds, Student student, Testpaper testpaper) {
        //看看是否有相同的试卷了
        Studenttestpaper sameTestpapers = null;
        boolean existed = false;
        for (Studenttestpaper tp : student.getStudenttestpaperSet()) {
            if (tp.getQuestionIds().equals(current.getQuestionIds())) {
                sameTestpapers = tp;
                existed = true;
                break;
            }
        }
        if (!existed) {
            current.setTestpaperId(testpaper);
            current.setQuestionIds(questionIds);
            current.setStudentId(student);
            current.setLeadpointId(null);
            current.setAnsweredInterval(0);
            current.setFinished(false);
            current.setTestscore(0d);
            current.setStudentAnswer("");
            current.setFirstWrongquestionids("");
            applicationStudenttestpaperController.create(current);
        } else {//存在相同的试卷
            setSelected( sameTestpapers);
            current.setAnsweredInterval(0);
            current.setFinished(false);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Thisis")
                    + commonSession.getResourceBound().getString("Old")
                    + commonSession.getResourceBound().getString("Testpaper")
            );
        }
    }

    /**
     * For testpaper or homework, the leadpoint will not be provided, therefore,
     * leadpoint is null here
     *
     * @param knowledgesList
     * @param student
     * @param testpaper
     */
    public void generateTestpaper4KnowledgeList(List<Knowledge> knowledgesList, Student student, Testpaper testpaper) {
        try {
            //考虑做题时所需要的时间与安排的时间之间的关系，是否也作为一个约束条件 ？
            setSelected( new Studenttestpaper());
            String questionIds = "";
            GA4Testpaper ga = new GA4Testpaper();
            List<Question> questionsList = new LinkedList<>();
            if (null != testpaper) {
                questionsList = ga.getCandidateQuestions(knowledgesList, testpaper.getScore(), testpaper.getDegree());
            } else {//It is from self practice
                for (Knowledge knowledge : knowledgesList) {
                    questionsList.addAll(knowledge.getQuestionSet());
                }
            }
            questionIds = questionsList.stream().map((question) -> "," + question.getId()).reduce(questionIds, String::concat);
            if (questionIds.length() > 1) {
                questionIds = questionIds.substring(1);
                dealwithDuplicateTestpaper(questionIds, student, testpaper);
            }
        } catch (CloneNotSupportedException ex) {
        }
    }
    //=======================get and set================================

    public void setCurrent(Studenttestpaper current) {
        this.current = current;
    }

    public HashSet<String> getOtherreasonSet() {
        return otherreasonSet;
    }

    public void setOtherreasonSet(HashSet<String> otherreasonSet) {
        this.otherreasonSet = otherreasonSet;
    }

    public Set<Question> getWrongquestionList() {
        if (wrongquestionList == null) {
            wrongquestionList = new HashSet<>();
        }
        return wrongquestionList;
    }

    public void setWrongquestionList(Set<Question> wrongquestionList) {
        this.wrongquestionList = wrongquestionList;
    }

    public Set<Question> getRightquestionList() {
        if (rightquestionList == null) {
            rightquestionList = new HashSet<>();
        }
        return rightquestionList;
    }

    public void setRightquestionList(Set<Question> rightquestionList) {
        this.rightquestionList = rightquestionList;
    }

    public HashMap<Question, String> getStudentanswerHashMap() {
        if (null == studentanswerHashMap) {
            studentanswerHashMap = new HashMap<>();
        }
        return studentanswerHashMap;
    }

    public void setStudentanswerHashMap(HashMap<Question, String> studentanswerHashMap) {
        this.studentanswerHashMap = studentanswerHashMap;
    }
    //Page changes

    public HashMap<Student, List<Studenttestpaper>> getExaminationTestpaper() {
        return examinationTestpaper;
    }

    public HashMap<Student, List<Studenttestpaper>> getPracticeTestpaper() {
        return practiceTestpaper;
    }

    public String getTypeOfPraOrExam() {
        return typeOfPraOrExam;
    }

    public void setTypeOfPraOrExam(String typeOfPraOrExam) {
        this.typeOfPraOrExam = typeOfPraOrExam;
    }

    public String prepareView() {
        setCurrent((Studenttestpaper) getItems().getRowData());
        selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        setCurrent(new Studenttestpaper());
        selectedItemIndex = -1;
        return "Create";
    }

    public void edit(Studenttestpaper testpaper) {
        setSelected( testpaper);
        applicationStudenttestpaperController.edit(testpaper);
    }

    public String create() {
        try {
            //Whether this testpaper existed
            if (!current.getStudentId().getStudenttestpaperSet().contains(current)) {
                current.setFinished(false);
                applicationStudenttestpaperController.create(current);
                current.getStudentId().getStudenttestpaperSet().add(current);
                this.logs(current.getStudentId() + "-"
                        + current.getLeadpointId().getSubjectid().getName() + commonSession.getResourceBound().getString("Testpaper"), tableName, StaticFields.OPERATIONINSERT);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //pageOperation.refreshData(getStudentTestpaperList());
                return prepareCreate();
            } else {
                userMessagor.addMessage(current.getStudentId().getName() + ":" + commonSession.getResourceBound().getString("Already")
                        + commonSession.getResourceBound().getString("Exist"));
                return null;
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student testpaper 1");
            return null;
        }
    }

    //For export student testpaper
    Character start = 'A';

    public String writeStudentTestpapers2File() {
        File file = new File(personalSessionSetup.getFileName(personalSessionSetup.getFilePath() + "/" + testpaperController.getTypeOfPraOrExam(),
                "TP" + teacherAdminController.getLogined().getId(), "html"));
        try {
            FileWriter fw = null;
            StringBuilder stpSB = new StringBuilder();

            fw = new FileWriter(file);
            fw.append("<html> <head><title></title><meta charset=\"UTF-8\">"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                    + "</head><body>");
            fw.flush();
            int radioGroupNumber = 0;//For radio group
            for (Entry<Student, List<Studenttestpaper>> item : this.getTestpaperMap()
                    .get(testpaperController.getTypeOfPraOrExam()).entrySet()) {
                if (!(item.getKey().getSchoolId().equals(schoolController.getSelected()))) {
                    continue;
                }
                //Code 
                fw.append(stpSB.toString());
                fw.flush();
                stpSB.replace(0, stpSB.length(), "");//clear all the content in it
                stpSB.append(item.getKey().getSchoolId().getName()).append(":");
                stpSB.append(item.getKey().getName()).append("<br>");//Student schoolID
                stpSB.append(item.getKey().getSecondname()).append(" ").append(item.getKey().getFirstname()).append("<br>");
                for (Studenttestpaper temTestpaper : item.getValue()) {
                    stpSB.append("=============================================");
                    stpSB.append(temTestpaper.getTestscore()).append("<br>");
                    int questionNumberLabel = 0;
                    String[] studentAnswerStrings = applicationStudenttestpaperController.getStudentAnswerStrings(temTestpaper);

                    for (Question question : applicationQuestionController.getTestQuestion(temTestpaper)) {
                        stpSB.append("-------------------------<br>");
                        stpSB.append(questionNumberLabel + 1).append("<br>");
                        stpSB.append(question.getValueinfo()).append("<br>");
                        stpSB.append(commonSession.getResourceBound().getString("Your")).append(commonSession.getResourceBound().getString("Answer"))
                                .append(":");
                        if (questionNumberLabel >= studentAnswerStrings.length) {
                            stpSB.append(commonSession.getResourceBound().getString("No")).append(" ")
                                    .append(commonSession.getResourceBound().getString("Finished")).append("<br>");
                            break;//Students didn't finished this exam
                        } else {
                            stpSB.append(studentAnswerStrings[questionNumberLabel])
                                    .append("<br>");
                        }
                        if (applicationQuestionController.isSingleFill(question)) {
                            stpSB.append(studentAnswerStrings[questionNumberLabel]).append("<br>");
                        }
                        if (applicationQuestionController.isJudgment(question)) {
                            stpSB.append("<input type=\"radio\" name =\"StudentAnswer")
                                    .append(radioGroupNumber).append("\"")
                                    //.append(" value=\" \"")
                                    .append(studentAnswerStrings[questionNumberLabel]
                                            .trim().equals("T") ? " checked" : "")
                                    .append(">")
                                    .append(commonSession.getResourceBound().getString("True"))
                                    .append("</input>")
                                    .append("<input type=\"radio\" name =\"StudentAnswer")
                                    .append(radioGroupNumber).append("\"")
                                    //.append(" value=\"\"")
                                    .append(studentAnswerStrings[questionNumberLabel]
                                            .trim().equals("F") ? " checked" : "")
                                    .append(">")
                                    .append(commonSession.getResourceBound().getString("False"))
                                    .append("</input>");
                            radioGroupNumber++;
                        }

                        if (applicationQuestionController.isSimpleAnswer(question)) {
                            stpSB.append(studentAnswerStrings[questionNumberLabel]).append("<br>");
                        }
                        if (applicationQuestionController.isSingleSelection(question)) {
                            int j = 0;
                            for (String optionDescription : questionController.getSelectionOptionStrings(question)) {
                                stpSB.append("<input type=\"radio\" name =\"StudentAnswer")
                                        .append(radioGroupNumber).append("\"")
                                        // .append(" value=\" ")
                                        .append(studentAnswerStrings[questionNumberLabel]
                                                .trim().equals(String.valueOf(j + 1)) ? " checked" : " ")
                                        .append(">")
                                        .append(Character.valueOf((char) (start + j++)).toString())
                                        .append(".")
                                        .append(optionDescription)
                                        .append("<br>");
                            }
                            radioGroupNumber++;
                        }

                        if (applicationQuestionController.isMultiSelection(question)) {
                            stpSB.append("<select multiple=\"multiple\">");
                            int j = 1;
                            for (String optionDescription : questionController.getSelectionOptionStrings(question)) {
                                stpSB.append("<option");
                                stpSB.append(studentAnswerStrings[questionNumberLabel].contains(String.valueOf(j)) ? " selected=\"selected\"" : "");
                                stpSB.append(">");
                                stpSB.append(Character.valueOf((char) (start + j - 1)).toString())
                                        .append(".")
                                        .append(optionDescription)
                                        .append("</option>");
                                j++;
                            }

                            stpSB.append("</select>");
                        }
                        stpSB.append("<br>").append(commonSession.getResourceBound().getString("Reference")).append(commonSession.getResourceBound().getString("Answer"))
                                .append(":").append(question.getAnswer()).append("<br>");
                        questionNumberLabel++;
                    }
                }
            }
            fw.append(stpSB.toString());
            fw.append("</body>\n</html>");
            fw.flush();
            fw.close();
            stpSB.replace(0, stpSB.length(), "");//clear the content and free the memory
        } catch (IOException ex) {
        }

        return applicationPublicFields.getFileRepository() + testpaperController.getTypeOfPraOrExam() + "/" + file.getName();
    }

}
