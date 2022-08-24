package modelController.viewerController;

import entities.Question;
import entities.Questionstudentcosttime;
import entities.Studenttestpaper;
import entities.WrongquestionCollection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import modelController.sessionController.QuestionController;
import modelController.sessionController.StudentController;
import modelController.sessionController.StudenttestpaperController;
import modelController.sessionController.TestpaperController;
import modelController.sessionController.WrongReasonController;
import modelController.sessionController.WrongquestionCollectionController;
import sessionBeans.QuestionstudentcosttimeFacadeLocal;
import tools.StaticFields;

/**
 *
 * @author 周鹏, MIU Ya-Nan
 */
@Named
@SessionScoped
public class HaveTest implements Serializable {

    @EJB
    private QuestionstudentcosttimeFacadeLocal questionstudentcosttimeFacadeLocal;
    @Inject
    private StudenttestpaperController studentTestpaperController;
    @Inject
    private QuestionController questionController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private WrongReasonController wrongReasonController;
    @Inject
    TestpaperController testpaperController;
    @Inject
    private StudentController studentController;
    @Inject
    private WrongquestionCollectionController wrongquestionCollectionController;
    @Inject
    private StudenttestpaperController studenttestpaperController;
    @Inject
    private modelController.applicationController.QuestionController applicationQuestionController;
    @Inject
    private modelController.applicationController.StudenttestpaperController applicationStudenttestpaperController;
    @Inject
    private tools.PersonalSessionSetup personalSessionSetup;
    @Inject
    private modelController.sessionController.SubjectController subjectController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private Question currentQuestion;

    private int index = 0;
    private List<Question> testQuestions;
    private int studentscore;
    private HashMap<Question, String> studentanswerHashMap;//试题id/学生答案 
    String temStudentAnswer;//往数据库testpaper中添加学生答案的字符串
    private Studenttestpaper studenttestpaper;
    //   private Testpaper testpaper;
    Calendar startTime;
    private final String toFinishedPage = "haveTest/secondStep/finishedTest",
            collectAnswer = "haveTest/secondStep/collectAnswer4Questions",
            lookWrong = "haveTest/secondStep/lookwrong", congratulation = "haveTest/secondStep/congratulation";
    private boolean isSelfPractice = false;
    private final String path1 = "studenttestpaper/";

    public void refresh() {
        index = 0;
    }
//    //=======================计算时间================================

    public int leftMinutes() {
        if (isSelfPractice) {
            return 10;
        }
        //判断时间是否已用完
        int result = (int) ((studenttestpaper.getTestpaperId().getSpecifiedInterval() - studenttestpaper.getAnsweredInterval()) / 60000f);
        if (result < 0) {
            persistAnswer();
        }
        return result;
    }

    public boolean isTimeOver() {
        if (isSelfPractice) {
            return false;
        }
        return studenttestpaper.getTestpaperId().getSpecifiedInterval() - studenttestpaper.getAnsweredInterval() < 0;
    }
////=======================试卷中题目是否遍历结束================================

    public boolean isQuestionEnd() {
        return index >= testQuestions.size() - 1;
    }
///=======================试卷答题能否结束================================

    public boolean isLast() {
        if (index == testQuestions.size() - 1) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("AlreadytoEnd!"));
            return true;
        } else {
            return false;
        }
    }

    public int getNumber(Question question) {//获得题目的“数目”特征
        return applicationQuestionController.getNumber(question);
    }

    //与上面的getNumber对应，主要用于多项填空题，与各个空的值进行绑定
    private List<String> data = new LinkedList<>();

    public List<String> getData() {
        //先获取学生提交的答案，如果学生没提交答案，再返回默认的
        int temNumber = getNumber(currentQuestion);
        if (getStudentanswerHashMap().get(currentQuestion).trim().length() > 0) {//表明有学生答案了，否则是个空串
            String[] answer = getStudentanswerHashMap().get(currentQuestion).split(StaticFields.THIRDDELIMITED);
            //先把学生的答案加载进来
            //这里的前提是前面学生答案被完整地保留了下来，因为多项选择题界面上不允许为空
            data = getMultiFillShow(getStudentanswerHashMap().get(currentQuestion));
        } else if (data.isEmpty()) {//在调试时候，发现只有两个空，这个方法会被调用多次，因此，会被不断执行下面语句，为此，执行ige
            for (int i = 0; i < temNumber; i++) {
                data.add("");
            }
        }
        return data;
    }

    public void setData(List<String> data) {//收集多项填空题
        this.data = data;
    }

    //多项选择题导出显示
    public List<String> getMultiFillShow(String answer) {
        return Arrays.asList(answer.split(StaticFields.THIRDDELIMITED));
    }

    ////////////
//=======================下一题 and 上一题================================
    public String nextQues() {// 下一题
        index++;
        if (studenttestpaper.getFinished()) {//已经完成了试卷，正在检查错误原因
            checkIfEnd(testQuestions);
            //检查本题是否为错题，如果是，则
            if (studentTestpaperController.getWrongquestionList().contains(currentQuestion)) {
                WrongquestionCollection wcc = wrongquestionCollectionController.getSelected();
                wcc.setQuestionId(currentQuestion);
                wcc.setReasonId(wrongReasonController.getSelected());
                wcc.setStudentId(studentController.getLogined());
                wrongquestionCollectionController.create();
                //重置该值，以便学生选择新的出错原因，由于sessionscopped原因
                wrongReasonController.setSelected(null);
            }
        } else {//没有完成试卷，正在向后翻阅，这里暂且只处理了多项选择题，应该是所有题目的答案都在这里处理
            //处理做题时间
            int costTime = (int) (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis());
            //保存学生做这道题所花的时间
            Questionstudentcosttime temQSC = new Questionstudentcosttime(currentQuestion.getId(), studenttestpaper.getStudentId().getId());
            temQSC.setCosttime(costTime);
            questionstudentcosttimeList.add(temQSC);
            studenttestpaper.setAnsweredInterval(studenttestpaper.getAnsweredInterval() + costTime);
            if (applicationQuestionController.isMULTIFILL(currentQuestion)) {
                //因为多项填空题是用多个inputText来收集答案的，因此，只能借助这个命令按钮来保存值
                String studentanswer = "";
                int i = 0;
                for (; i < getNumber(currentQuestion) - 1; i++) {
                    studentanswer += data.get(i).trim() + StaticFields.THIRDDELIMITED;
                }
                studentanswer += data.get(getNumber(currentQuestion) - 1).trim();//最后一个加入
                if (!getStudentanswerHashMap().get(currentQuestion).trim().equals(studentanswer)) {
                    getStudentanswerHashMap().put(currentQuestion, studentanswer);
                }
                data = new LinkedList<>();//因为是保存在Session中的，所以需要清空上次的内容
            }
            startTime = Calendar.getInstance();
            if (editorNot()) {
                persistAnswer();
            }
        }
        return null;
    }
    List<Questionstudentcosttime> questionstudentcosttimeList = new LinkedList<>();

    private void fillQuestionStudentCosttime() {
        for (Questionstudentcosttime questionStudentcosttime : questionstudentcosttimeList) {
             Questionstudentcosttime tem = questionstudentcosttimeFacadeLocal.find(questionStudentcosttime.getQuestionstudentcosttimePK());
            if(tem==null) {//没找到
                questionstudentcosttimeFacadeLocal.create(questionStudentcosttime);
            }else{//已经存在了
                if (tem.getCosttime() != null && tem.getCount() != null) {
                    tem.setCosttime((tem.getCosttime() * tem.getCount() + questionStudentcosttime.getCosttime()) / (tem.getCount() + 1));
                }else{
                    tem.setCount(1);
                    tem.setCosttime(questionStudentcosttime.getCosttime());
                }
                tem.setCount(tem.getCount() + 1);
                questionstudentcosttimeFacadeLocal.edit(tem);
            }
        }
        questionstudentcosttimeList.clear();
    }

    private boolean checkIfEnd(List<Question> questionList) {
        if (index >= questionList.size()) {
            index = 0;
            userMessagor.addMessage(commonSession.getResourceBound().getString("SFBAG") + "!");
            return true;
        }
        return false;
    }

    private boolean editorNot() {
        //每隔一段时间(StaticFields.QUESTIONANSWERINTERVAL)就保存到数据库中，以防断电或意外;
        //或者当已经完成全部题目时，保存到数据库
        //A decision tree
        if (checkIfEnd(testQuestions)) {//做完了
            return true;
        }
        if (leftMinutes() <= 0) {
            studenttestpaper.setFinished(true);
            return true;
        } else if (index >= testQuestions.size()) {
            index = 0;
            userMessagor.addMessage(commonSession.getResourceBound().getString("Finished") + "!"
                    + commonSession.getResourceBound().getString("SFBAG") + "!");
            return true;
        } else if (index % StaticFields.QUESTIONANSWERINTERVAL == 0) {
            return true;
        }
        return false;
    }

    private void persistAnswer() {
        temStudentAnswer = "";
        testQuestions.forEach((Question q) -> {
            temStudentAnswer += StaticFields.FIRSTDELIMITED + getStudentanswerHashMap().get(q);
        });
        temStudentAnswer = temStudentAnswer.substring(StaticFields.FIRSTDELIMITED.length());
        studenttestpaper.setStudentAnswer(temStudentAnswer);
        studentTestpaperController.edit(studenttestpaper);
        //保存学生完成各题的时间
        fillQuestionStudentCosttime();
    }

    public String previous() {//上一题
        index--;
        return null;
    }

//=======================检查,返回第一题================================
    //=======================是否查看错题================================
    public void lookWrong() {// 查看错题
        if (getWrongQuestions().size() > 0) {
            index = 0;
            mainXhtml.setPageName(lookWrong);
        } else {
            mainXhtml.setPageName(congratulation);
        }
    }

    //=======================get and set================================
    public Question getQuestion() {
        if (index >= testQuestions.size()) {
            index--;
        }
        currentQuestion = testQuestions.get(index);
        questionController.setSelected(currentQuestion);
        return questionController.getSelected();
    }

    public String getQuestionType() {
        return commonSession.getResourceBound().getString("QuestionType" + currentQuestion.getType());
    }

    public void setQuestion(Question question) {
        this.currentQuestion = question;
    }

    public String getQuestionTitle(Question question) {
        currentQuestion = question;
        String result = "";
        switch (question.getType()) {
            case StaticFields.JUDGMENT:
            case StaticFields.MULTISELECTION:
            case StaticFields.SIMPLEANSWER:
            case StaticFields.SINGLEFILL:
            case StaticFields.SINGLESELECTION:
            case StaticFields.MULTIFILL:
                result = question.getValueinfo();
                break;
            case StaticFields.OBJECTIVEPROGRMA:
            case StaticFields.SUBJECTIVEPROGRAM:
                result = question.getValueinfo().replaceAll(QuestionController.SCHOOLNAMESTUDENTNAME,
                        "s" + studentController.getLogined().getSchoolId().getId() + studentController.getLogined().getId());
                result = result.replaceAll(QuestionController.STUDENTNAME, "s" + studentController.getLogined().getId());
                break;
        }
        return result;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getStudentscore() {
        return studentscore;
    }

    public void setStudentscore(int studentscore) {
        this.studentscore = studentscore;
    }

    public int getLeftMinutes() {
        return studenttestpaper.getTestpaperId().getSpecifiedInterval() - studenttestpaper.getAnsweredInterval();
    }

    public String getStudentanswer() {
        return getStudentanswerHashMap().get(currentQuestion);
    }

    public void setStudentanswer(String studentanswer) {
        int costTime = (int) (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis());
        studenttestpaper.setAnsweredInterval(studenttestpaper.getAnsweredInterval() + costTime);
        //这道题的答案
        //除多项选择、编程和多个填空外，都可以在这里处理
        //把多余的空格替换掉
        studentanswer = StaticFields.replaceSpacewithOne(studentanswer);
        if (!getStudentanswerHashMap().getOrDefault(currentQuestion, "").trim().equals(studentanswer)) {
            getStudentanswerHashMap().put(currentQuestion, studentanswer);
        }
        getStudentanswerHashMap().put(currentQuestion, studentanswer);
        startTime = Calendar.getInstance();
    }

//下面处理多项选择题========================begin==========================
    private String[] studentAnswer4multi = {"", "", "", "", ""};

    public String[] getStudentAnswer4multi() {
        //多个答案用逗号分隔
        if (null != getStudentanswerHashMap().get(currentQuestion)) {
            String[] temStrings = getStudentanswerHashMap().get(currentQuestion).split(",");
            //下面把学生的答案放里边，不足部分补空串
            int i = 0;
            for (; i < temStrings.length; i++) {
                studentAnswer4multi[i] = temStrings[i];
            }
            while (i < studentAnswer4multi.length) {
                studentAnswer4multi[i] = "";
                i++;
            }
        } else {
            studentAnswer4multi = new String[]{"", "", "", "", ""};
        }
        return studentAnswer4multi;
    }

    public void setStudentAnswer4multi(String[] studentAnswer4multi) {

        String result = "";
        if (studentAnswer4multi.length > 1) {
            for (int i = 0; i < studentAnswer4multi.length - 1; i++) {
                result += studentAnswer4multi[i] + ",";
            }
        }
        result += studentAnswer4multi[studentAnswer4multi.length - 1];

        result = StaticFields.replaceSpacewithOne(result);
        getStudentanswerHashMap().put(currentQuestion, result);
        studentAnswer4multi = new String[]{"", "", "", "", ""};//为下一题做准备
    }
//多项选择题处理完毕============================end=========================
    // private final boolean isTest = true;

    public String startSelfPractice() {//完成自测
        setIsSelfPractice(true);
        this.refresh();
        this.studenttestpaper = studentTestpaperController.getTestpaper4leadPoint();
        prepareTestpaper(studenttestpaper);
        return null;
    }

    public String startSelfHomeworkPractice() {//完成自测作业 
        HashSet<Studenttestpaper> feasibleTestpaper = studentTestpaperController.getStudentTestPaper(false, subjectController.getSelected());
        return this.commonPractice(feasibleTestpaper, true);
    }

    public String machineDecide() {
        testpaperController.creatTestpapersForStudent(true);//Create new testpaper according to the leadpoint
        HashSet<Studenttestpaper> testpaper = studentTestpaperController.getStudentTestPaper(false, subjectController.getSelected());
        return this.commonPractice(testpaper, true);
    }

    /*public String startPractice() {
        //testpaperController.creatTestpapersForStudent();
        HashSet<Studenttestpaper> feasibleTestpaper = studentTestpaperController.getStudentTestPaper(false);
        return this.commonPractice(feasibleTestpaper);
    }*/
    public String startHomework() {//
        // testpaperController.creatTestpapersForStudent();
        HashSet<Studenttestpaper> feasibleTestpaper = studentTestpaperController.getStudentTestPaper(false, subjectController.getSelected());
        return this.commonPractice(feasibleTestpaper, false);
    }

    /*
    @param create: 如果不存在，是否要生成
     */
    private String commonPractice(HashSet<Studenttestpaper> feasibleTestpaper, boolean create) {
        if (feasibleTestpaper.isEmpty() && create) {
            //没有试卷，则生成试卷
            testpaperController.creatTestpapersForStudent(false);//No leadpoint are referenced
            feasibleTestpaper = studentTestpaperController.getStudentTestPaper(false, subjectController.getSelected());
        }
        refresh();
        if (!feasibleTestpaper.isEmpty()) {
            this.studenttestpaper = feasibleTestpaper.iterator().next();
        } else {
            this.studenttestpaper = null;
        }
        studentTestpaperController.setSelected(this.studenttestpaper);
        prepareTestpaper(this.studenttestpaper);
        return null;
    }

    public String startExamination() {
        HashSet<Studenttestpaper> feasibleTestpaper = studentTestpaperController.getStudentTestPaper(true, subjectController.getSelected());
        if (!feasibleTestpaper.isEmpty()) {
            refresh();
            Iterator it = feasibleTestpaper.iterator();
            int feasibleCount = 0;
            while (it.hasNext()) {
                Studenttestpaper testpaper1 = (Studenttestpaper) it.next();
                if (isInTestTime(testpaper1)) {
                    studentTestpaperController.setSelected(testpaper1);
                    this.studenttestpaper = testpaper1;
                    prepareTestpaper(this.studenttestpaper);
                    feasibleCount++;
                    break;
                }
            }
            if (feasibleCount <= 0) {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Not")
                        + commonSession.getResourceBound().getString("Test")
                        + commonSession.getResourceBound().getString("Time"));
            }
        } else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("No")
                    + commonSession.getResourceBound().getString("Testpaper"));
        }
        return null;
    }

    private boolean isInTestTime(Studenttestpaper studenttestpaper) {
        boolean result = true;
        Calendar current = Calendar.getInstance();
        Calendar testTimeBegin = Calendar.getInstance();
        testTimeBegin.setTime(studenttestpaper.getTestpaperId().getSpecifieddate());

        if (current.get(Calendar.YEAR) == testTimeBegin.get(Calendar.YEAR)
                && current.get(Calendar.MONTH) == testTimeBegin.get(Calendar.MONTH)
                && current.get(Calendar.DAY_OF_MONTH) == testTimeBegin.get(Calendar.DAY_OF_MONTH)) {
            Calendar startTime1 = Calendar.getInstance();
            startTime1.setTime(studenttestpaper.getTestpaperId().getStart());

            testTimeBegin.set(Calendar.HOUR_OF_DAY, startTime1.get(Calendar.HOUR_OF_DAY));
            testTimeBegin.set(Calendar.MINUTE, startTime1.get(Calendar.MINUTE));

            if (current.before(testTimeBegin)) {
                result = false;
            } else {
                Calendar endTime = Calendar.getInstance();
                endTime.setTime(studenttestpaper.getTestpaperId().getEndtime());
                testTimeBegin.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
                testTimeBegin.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
                result = !current.after(testTimeBegin);
            }
        } else {
            result = false;
        }
        return result;
    }

    private void prepareTestpaper(Studenttestpaper studenttestpaperestpaper) {
        if (null != studenttestpaperestpaper) {
            testQuestions = applicationStudenttestpaperController.getRegular(studentTestpaperController.getSelected());
            //testQuestions = applicationQuestionController.getTestQuestion(studentTestpaperController.getSelected());
            setStudentanswerHashMap(new HashMap<>());
            testQuestions.forEach(question1 -> {
                getStudentanswerHashMap().put(question1, "");
            });
            if (studenttestpaperestpaper.getFinished()) {
                mainXhtml.setPageName(toFinishedPage);
            } else {
                startTime = Calendar.getInstance();
                mainXhtml.setPageName(collectAnswer);
            }
        } else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("No")
                    + commonSession.getResourceBound().getString("Testpaper")
                    + commonSession.getResourceBound().getString("Or")
                    + commonSession.getResourceBound().getString("Practice"));
        }
    }

    public int questionListSize() {
        return this.testQuestions.size();
    }

    public String endPractice() {
        return null;
    }

    public HashMap<Question, String> getStudentanswerHashMap() {
        return studentanswerHashMap;
    }

    public void setStudentanswerHashMap(HashMap<Question, String> studentanswerHashMap) {
        this.studentanswerHashMap = studentanswerHashMap;
    }

    public void submit() {
        studenttestpaper.setFinished(true);
        this.persistAnswer();
        //为在内存中判卷，把学生答案直接传递过去。
        studentTestpaperController.setStudentanswerHashMap(studentanswerHashMap);
        //在finishedTest.xhmtl中调用了testpaperController.correctTestpaper();
        mainXhtml.setPageName(toFinishedPage);
    }
    //For subjective and objective program
    private Part answerPart;

    public Part getAnswerPart() {
        //因为这道题是主观题，所以可能将会花较长时间，所以先保存以前的题目
        persistAnswer();
        return answerPart;
    }

    public void setAnswerPart(Part answerPart) {
        this.answerPart = answerPart;
        //1）以学生的登录＋题目名称作为新文件的；
        // 2)将学生的上舒心传上传到指定目录；
        if (null != answerPart) {
            try (InputStream input = answerPart.getInputStream()) {
                String fileName = answerPart.getSubmittedFileName();
                File fold = new File(personalSessionSetup.getFilePath() + "/" + this.path1 + studenttestpaper.getId());
                if (!fold.exists()) {
                    fold.mkdir();
                }
                fold = new File(personalSessionSetup.getFilePath() + "/" + this.path1 + studenttestpaper.getId() + "/" + currentQuestion.getId() + "/");
                if (!fold.exists()) {
                    fold.mkdir();
                }
                File targetFile = new File(fold.getAbsolutePath() + "/" + fileName);
                if (targetFile.exists()) {
                    targetFile.delete();
                }
                Files.copy(input, new File(fold.getAbsolutePath(), fileName).toPath());
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                currentQuestion.setAnswer(fileName);
            } catch (IOException e) {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " have test 1");
            }
        }
    }
    //学生答案的URL，方便学生下载
    HashSet<String> programFileType;

    private HashSet<String> getProgramType() {//Only calculate once, but how to know the parameter is changed?
        if (null == programFileType || programFileType.isEmpty()) {
            programFileType = new HashSet<>();
            String[] programType = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("programQuestionFileType").split(",");
            for (String type : programType) {
                programFileType.add(type);
            }
        }
        return programFileType;
    }

    public void partValidator(FacesContext ctx, UIComponent comp, Object value) {
        String message = "";
        Part file = (Part) value;
        if (null != value) {
            if (file.getSize() > maxSize) {
                message = commonSession.getResourceBound().getString("Too")
                        + commonSession.getResourceBound().getString("Big");
                userMessagor.addMessage(message);
            } else {
                boolean infileType = false;
                try {
                    String loadfileType = file.getSubmittedFileName().substring(file.getSubmittedFileName().lastIndexOf(".") + 1);
                    for (String fileType : getProgramType()) {
                        if (loadfileType.equals(fileType)) {
                            infileType = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    infileType = false;
                }
                if (infileType) {//Persist the file
                    int costTime = (int) (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis());
                    studenttestpaper.setAnsweredInterval(studenttestpaper.getAnsweredInterval() + costTime);
                    //这道题的答案
                    //Except multi-Selection and programing, all the answer can be dealt with here
                    //把多余,的空格替换掉
                    String studentanswer = file.getSubmittedFileName();
//                    if (!getStudentanswerHashMap().getOrDefault(currentQuestion, "").trim().equals(studentanswer)) {
//                        getStudentanswerHashMap().put(currentQuestion, studentanswer);
//                    }
                    getStudentanswerHashMap().put(currentQuestion, studentanswer);
//                    setAnswerPart(file);
                    startTime = Calendar.getInstance();
                } else {
                    message = commonSession.getResourceBound().getString("Not") + " "
                            + commonSession.getResourceBound().getString("Validate") + " "
                            + commonSession.getResourceBound().getString("File") + " "
                            + commonSession.getResourceBound().getString("Type");
                    userMessagor.addMessage(message);
                }
            }
            if (message.trim().length() > 0) {
                throw new ValidatorException(new FacesMessage(message));
            }
        }
    }
    private final int maxSize = 1024000;

    public boolean isIsSelfPractice() {
        return isSelfPractice;
    }

    public void setIsSelfPractice(boolean isSelfPractice) {
        this.isSelfPractice = isSelfPractice;
    }

    //Wrong questions ============begin===========================
    //查看错题
    public List<Question> getWrongQuestions() {
        return studentTestpaperController.getTemWrongQuestionList();
    }

    public Question getWrongQuestion() {
        if (index >= getWrongQuestions().size()) {
            index--;
        }

        currentQuestion = getWrongQuestions().get(index);
        questionController.setSelected(currentQuestion);
        return questionController.getSelected();
    }

    public int wrongQuestionListSize() {
        return this.getWrongQuestions().size();
    }

    public String previousWrongquest() {//上一题
        index--;
        return null;
    }

    public String nextWrongQues() {// 下一题
        index++;
        checkIfEnd(getWrongQuestions());
        //检查本题是否为错题，如果是，则
        WrongquestionCollection wcc = wrongquestionCollectionController.getSelected();
        wcc.setQuestionId(currentQuestion);
        wcc.setReasonId(wrongReasonController.getSelected());
        wcc.setStudentId(studentController.getLogined());
        wrongquestionCollectionController.create();
        //重置该值，以便学生选择新的出错原因，由于sessionscopped原因
        wrongReasonController.setSelected(null);
        return null;
    }
    //Wrong questions ============end===========================
    //导出学生试卷==============================开始=================
    List<Question> testPaperQuestions = new LinkedList<>();

    public List<Question> getTestQuestion(Studenttestpaper temTestpaper) {
        if (testPaperQuestions.isEmpty()) {
            testPaperQuestions = applicationQuestionController.getTestQuestion(temTestpaper);
        }
        return testPaperQuestions;
    }

    public String nextStudentsTestpaper() {
        testPaperQuestions.clear();
        return "========================";
    }

    public String setCurrentQuestion(Question para) {
        if (null != para) {
            currentQuestion = para;
        }
        return "";
    }
    //导出学生试卷==============================结束=================

}
