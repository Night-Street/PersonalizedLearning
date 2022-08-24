package modelController.sessionController;

import entities.Knowledge;
import entities.Parent;
import entities.Question;
import entities.Reexamination;
import entities.Student;
import entities.Subject;
import entities.TeacherAdmin;
import entities.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import modelController.viewerController.MainXhtml;
import sessionBeans.QuestionFacadeLocal;
import tools.PersonalSessionSetup;
import tools.StaticFields;

@Named
@SessionScoped
public class QuestionController extends CommonModelController<Question> implements Serializable {

    @EJB
    private QuestionFacadeLocal questionFacadeLocal;
    @Inject
    private modelController.sessionController.LeadpointController leadpointController;
    @Inject
    private modelController.applicationController.ReexaminationController applicationReexaminationController;
    @Inject
    private modelController.sessionController.ReexaminationController reexaminationController;
    @Inject
    private KnowledgeController knowledgeController;
    @Inject
    private PersonalSessionSetup personalSessionSetup;
    @Inject
    private SubjectController subjectController;
    @Inject
    private MajorController majorController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.QuestionController applicationQuestionController;
    @Inject
    private modelController.applicationController.StatusofresourcesController applicationStatusofresourcesController;
    @Inject
    private modelController.applicationController.SubjectController applicationSubjectController;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private modelController.sessionController.LearningresourceController learningresourceController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.sessionController.MyPublishedQuestionController myPublishedQuestionController;
    private String searchName;
    private List<Question> searchedQuestionsList;
    private final String tableName = "question", listpage = "question/List",
            editpage = "question/Edit", editAnswerpage = "question/EditAnswer",
            viewpage = "question/View",
            createpage = "question/Create", imageFile = "QuestionsImage";
    final String splitInputOutputTest = "====";
    final String folder = "subjective";
    private String[] multSelectionStrings;
    public final static String SCHOOLNAMESTUDENTNAME = "SCHOOLNAMESTUDENTNAME", STUDENTNAME = "STUDENTNAME";
    private Part imagePart = null;
    private HashMap<Subject, Set<Question>> subjectQuestion = new HashMap<>();
    private int questionType;

    Part questionExcel = null;
    protected Question current;

    public Question getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Question();
        }
        return current;
    }
    public void setDataModelList() {
        pageOperation.setDataModelList(getQuestionList());
    }

    public Part getQuestionExcel() {
        return questionExcel;
    }

    public void setQuestionExcel(Part questionExcel) {
        this.questionExcel = questionExcel;
    }

//    int index = 1;
//
//    public String getIndex() {
//        return "q" + (++index);
//    }
//
//    public void resetIndex() {
//        this.index = 1;//重置List.xhmtl中用到的index
//    }
//
//    public String getSameIndex() {
//        return "q" + index;
//    }
    //-------------------------------For the search and viewall command button------------------
    public void search() {
        if (null != searchName && searchName.trim().length() > 0) {
            String whereString = "";
            if (subjectController.getSelected().getId() != null && subjectController.getSelected().getKnowledgeSet().size() > 0) {
                whereString = " and  KNOWLEDGE_ID in(" + applicationSubjectController.getKnowlegeIdsString(subjectController.getSelected()) + ")";
            }
            List<Question> searchedResult = applicationQuestionController.getQueryResultList("select * from question where "
                    + " (locate('" + searchName.toLowerCase() + "',LOWER(answer))>0 or "
                    + " locate('" + searchName.toLowerCase() + "',LOWER(valueinfo))>0 or "
                    + " locate('" + searchName.toLowerCase() + "',LOWER(analysis))>0 or "
                    + " locate('" + searchName.toLowerCase() + "',LOWER(secondcontent))>0)"
                    + whereString
            );
            pageOperation.refreshData(searchedResult);
            mainXhtml.setPageName(this.listpage);
        }
    }
//设置题目类型，ajax同时进行相关的操作==============begin==========================
    int temType = 0;

    public int getType() {
        if (null != getSelected().getId()) {
            temType = getSelected().getType();
        }
        return temType;
    }

    public void setType(int type) {
        getSelected().setType(type);
        switch (type) {
            case 0:
                break;
            case 6:
                break;
        }
        temType = type;
    }

//设置题目类型，ajax同时进行相关的操作==============end========================== 
    //------------------prepare View, edit, list,create------------------
    public void prepareList() {
        if (fromPublished) {
            fromPublished = false;
            myPublishedQuestionController.prepareList();
        } else {
            pageOperation.refreshData(getQuestionList());
            mainXhtml.setPageName(this.listpage);
        }
    }

    public void prepareView() {
        setSelected((Question) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.viewpage);
        prepareSecondContent();
    }

    public void prepareList(Set<Question> questionSet) {
        LinkedList<Question> temLinkedList = new LinkedList(questionSet);
        pageOperation.refreshData(temLinkedList);
        knowledgeController.setSelected(temLinkedList.get(0).getKnowledgeId());
        mainXhtml.setPageName(this.listpage);
    }

    public void prepareCreate() {
        setSelected(new Question());
        this.selectionString1 = "";
        this.selectionString2 = "";
        this.selectionString3 = "";
        this.selectionString4 = "";
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
        prepareSecondContent();
    }

    public String showedName(Question question) {
        StringBuilder sb = new StringBuilder();
        Reexamination reexamination;
        if (null != question) {
            Set<Reexamination> setOfReexaminations = applicationReexaminationController.getReexamination4Question(question);
            if (setOfReexaminations.size() > 0) {
                reexamination = (Reexamination) setOfReexaminations.toArray()[0];
            } else {
                return "";
            }
            TeacherAdmin teacher = reexamination.getTeacherid();
            if (null != teacher) {
                sb.append(null == teacher.getSecondname() ? "" : teacher.getSecondname())
                        .append(" ")
                        .append(null == teacher.getFirstname() ? "" : teacher.getFirstname());
            } else {
                Student student = reexamination.getStudentid();
                if (null != student) {
                    sb.append(null == student.getSecondname() ? "" : student.getSecondname())
                            .append(" ")
                            .append(null == student.getFirstname() ? "" : student.getFirstname());
                } else {
                    Parent parent = reexamination.getParentid();
                    if (null != parent) {
                        sb.append(null == parent.getSecondname() ? "" : parent.getSecondname())
                                .append(" ")
                                .append(null == parent.getFirstname() ? "" : parent.getFirstname());
                    }
                }
            }
        } else {
            sb.append("");
        }
        return sb.toString();
    }

    public void create() {
        //resetIndex();
        //0. Check whether the knowlege is selected
        if ( knowledgeController.getSelected().getId() != null) {
            //1. check whether the name existed? If true, deny the operation;else continue;
            if (null == applicationQuestionController.findByName(current.getValueinfo().trim())) {
                current.setKnowledgeId(knowledgeController.getSelected());
                if (current.getType().equals(3)) {//selection type question
                    setSecondContent();
                }
                if (current.getType().equals(6)) {//multi-Selection
                    setMultiSecondContent();
                    setMultiSelectionAnswer();
                }

                applicationQuestionController.create(current);
                saveQuestionImages();
                reexaminationController.createReexamination(null, current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //当题目列不保存在内存中时，可以不刷新
                pageOperation.refreshData(getQuestionList());
                this.logs(current.getValueinfo(), tableName, StaticFields.OPERATIONINSERT);
                //Refresh the cache of JPA
                mainXhtml.setPageName(this.viewpage);
                prepareSecondContent();
            } else {
                userMessagor.addMessage(current.getValueinfo() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Please")
                    + commonSession.getResourceBound().getString("Select")
                    + commonSession.getResourceBound().getString("Knowledge"));
        }
    }

    public String imageURL() {
        //System.out.println(imageFile + "/" + getSelected().getKnowledgeId().getId() + "/" + current.getFigure());
        return imageFile + "/" + getSelected().getKnowledgeId().getId() + "/" + current.getFigure();
    }

    private void saveQuestionImages() {
        if (imagePart != null) {
            try (InputStream is = imagePart.getInputStream()) {
                File file = new File(personalSessionSetup.getFilePath() + "/" + imageFile + "/" + current.getKnowledgeId().getId());
                // System.out.println("1==============" + picturePath + "/" + current.getKnowledgeId().getId() + ":" + "========================");
                if (!file.exists()) {
                    file.mkdir();
                }
                String filename = imagePart.getSubmittedFileName();
                //If there is some other figures with the same name, then delete it. 
                {//For the update 
                    //First read all the files name in this fold
                    File[] files = file.listFiles();
                    if (null != files) {
                        //Second, remove those files with the same name with the same name
                        for (int i = 0; i < files.length; i++) {
                            //String temFileName = files[i].getName().substring(0, files[i].getName().indexOf("."));
                            if (files[i].getName().substring(0, files[i].getName().indexOf(".")).equals(String.valueOf(current.getId()))) {
                                files[i].delete();
                            }
                        }
                    }
                }
                filename = current.getId() + filename.substring(filename.lastIndexOf("."));
//                try {
//                    FileWriter file1 = new FileWriter(new File("/root/program/glassfish5/glassfish/a.txt"), true);
//                    BufferedWriter bw = new BufferedWriter(file1);
//                    bw.write("==============" + picturePath + "/" + current.getKnowledgeId().getId() + ":" + filename + "========================");
//                    bw.flush();
//                    bw.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(QuestionController.class.getName()).log(Level.SEVERE, null, ex);
//                }
                Files.copy(is, new File(personalSessionSetup.getFilePath() + "/" + imageFile + "/" + current.getKnowledgeId().getId(), filename).toPath());
                current.setFigure(filename);
                Reexamination reexamination = (Reexamination) applicationReexaminationController.getReexamination4Question(current).toArray()[0];
                reexamination.setStatus(applicationStatusofresourcesController.getStatusofresources(0));//0表示未审核
                applicationQuestionController.edit(current);
                applicationReexaminationController.edit(reexamination);
            } catch (Exception e) {
            }
            //current.setFigure(folder);
        }
    }

    private void setMultiSelectionAnswer() {
        String tem = "";
        //这里的multSelectionStrings不调用其get方法，因为其get方法会读取原题目中的内容，而不是set过来的最新内容
        for (int i = 0; i < this.multSelectionStrings.length; i++) {
            tem += "," + this.multSelectionStrings[i];
        }
        if (tem.trim().length() > 0) {
            current.setAnswer(tem.substring(1));
        } else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Session Question 2");
        }
    }

    private void setSecondContent() {
        String secondContentString = this.getSelectionString1()
                + StaticFields.splitInString + this.getSelectionString2()
                + StaticFields.splitInString + this.getSelectionString3()
                + StaticFields.splitInString + this.getSelectionString4();
        current.setSecondcontent(secondContentString);
    }

    private void setMultiSecondContent() {
        String secondContentString = this.getSelectionString1()
                + StaticFields.splitInString + this.getSelectionString2()
                + StaticFields.splitInString + this.getSelectionString3()
                + StaticFields.splitInString + this.getSelectionString4()
                + StaticFields.splitInString + this.getSelectionString5();
        current.setSecondcontent(secondContentString);
    }
    List<Question> selectedQuestions = new LinkedList<>();

    public String selectThisQuestion() {
        try {
            setSelected(((Question) getItems().getRowData()));
            selectedQuestions.add(current);
            prepareSecondContent();
        } catch (Exception e) {//getItems().getRowData() return NoRowAvailableException
            selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        }
        return null;
    }

    public List<Question> getSelectedQuestions() {
        return selectedQuestions;
    }

    public void prepareEdit() {
        try {
            if (!fromPublished) {
                setSelected((Question) getItems().getRowData());
                selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
            }
            if (majorController.getSelected().getId() != null) {
                majorController.setSelected(current.getKnowledgeId().getSubjectId().getMajorsubjectSet().iterator().next().getMajorid());
            }
            if (subjectController.getSelected().getId() != null) {
                subjectController.setSelected(current.getKnowledgeId().getSubjectId());
            }
            if (knowledgeController.getSelected().getId() != null) {
                knowledgeController.setSelected(current.getKnowledgeId());
            }
            mainXhtml.setPageName(this.editpage);
            prepareSecondContent();
        } catch (Exception e) {//getItems().getRowData() return NoRowAvailableException
            mainXhtml.setPageName(this.editpage);
        }
        //resetIndex();
    }

    public void prepareEdit1() {
        mainXhtml.setPageName(this.editAnswerpage);
    }

    public void update() {
        try {
            switch (getSelected().getType()) {
                //其他类型的题目不用处理
                case StaticFields.SINGLESELECTION:
                    secondContentReady = false;//For the method "getSelectionString1()" to get "selectionString1"
                    setSecondContent();
                    break;
                case StaticFields.MULTISELECTION:
                    secondContentReady = false;//For the method "getSelectionString1()" to get "selectionString1"
                    setMultiSecondContent();
                    setMultiSelectionAnswer();
                    break;
                case StaticFields.OBJECTIVEPROGRMA:
                    getSelected().setSecondcontent(this.getInputDataString() + this.splitInputOutputTest + this.getOutputDataString());
                    break;
                case StaticFields.SUBJECTIVEPROGRAM:
                    //1.write into a file
                    InputStream input = answerFile.getInputStream();
                    String fileName = answerFile.getSubmittedFileName();
                    Files.copy(input, new File(folder, fileName).toPath());
                    //2.write the path into answer
                    break;
//                case StaticFields.MULTIFILL:
//                    //不用处理
////                    String tem = "";
////                    int temIndex = temMultiFillList.size();
////                    int j = temIndex;
////                    while (j > 1) {
////                        tem += temMultiFillList.get(temIndex - j) + StaticFields.THIRDDELIMITED;
////                        j--;
////                    }
////                    tem += temMultiFillList.get(temIndex - 1);//避免使用再删除最后字符串的麻烦
////                    getSelected().setAnswer(tem);
//                    break;
            }
            saveQuestionImages();
            applicationQuestionController.edit(current);
            Set<Reexamination> setOfReexaminations = applicationReexaminationController.getReexamination4Question(current);
            if (setOfReexaminations.size() > 0) {
                Reexamination reexamination = (Reexamination) setOfReexaminations.toArray()[0];
                reexamination.setStatus(applicationStatusofresourcesController.getStatusofresources(0));//0表示未审核
                applicationReexaminationController.edit(reexamination);
            } else {
                reexaminationController.createReexamination(null, current);
            }

            //publicFields.refreshQuestionList(null);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            pageOperation.refreshData(getQuestionList());
            mainXhtml.setPageName(this.viewpage);
            this.logs(getSelected().getValueinfo(), tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Session Question 3");
        }
    }

    public void destroy() {
        current = (Question) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        mainXhtml.setPageName(this.listpage);
        setSelected(null);
        //resetIndex();
    }

    private void performDestroy() {
        try {
            for (Reexamination reexamination : current.getReexaminationSet()) {
                applicationReexaminationController.remove(reexamination);
            }
            applicationQuestionController.remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            //searchedQuestionsList.remove(current);
            pageOperation.refreshData(getQuestionList());
            this.logs(current.getValueinfo(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Contro Question 4");
        }
    }

    List<Knowledge> knowledgeList = new LinkedList<>();
    List<Question> questionList = new LinkedList<>();

    public List<Question> getQuestionList() {
        //Get the knowledges of the current subject
        questionList.clear();
        if (knowledgeController.getSelected() == null || knowledgeController.getSelected().getId() == null) {
            knowledgeList = applicationKnowledgeController.getKnowledgeList4Subject(subjectController.getSelected());
            for (Knowledge knowledge : knowledgeList) {
                questionList.addAll(applicationQuestionController.getQuestion4Knowledge(knowledge));
            }
        } else {
            questionList.addAll(applicationQuestionController.getQuestion4Knowledge(knowledgeController.getSelected()));
        }
        searchedQuestionsList = new LinkedList<>();
        //prepare question filter
        String whereType = "0";//00=0,01=1,10=2,11=3
        //没有选择知识点
        if (knowledgeController.getSelected() == null || knowledgeController.getSelected().getId() == null) {
            whereType = "0";//0
        } else {//选择知识点
            whereType = "1";//1
        }
        if (0 == this.questionType) {//没有选择题型
            whereType += "0";//00 or 10
        } else {//选择题型
            whereType += "1";//01 or 11
        }
        switch (whereType) {
            case "00"://neigher knowledge nor quesitontype are selected, all questions are  returned
                searchedQuestionsList = questionList;
                break;
            case "01"://no knowledge selected but questionType is selected
                for (Question question : questionList) {
                    if (question.getType() == this.questionType) {
                        searchedQuestionsList.add(question);
                    }
                }
                break;
            case "10"://选择了知识点 but no questionType
                for (Question question : questionList) {
                    if (question.getKnowledgeId().getId() == knowledgeController.getSelected().getId()) {
                        searchedQuestionsList.add(question);
                    }
                }
                break;
            case "11"://both knowledge and questionType are selected
                for (Question question : questionList) {
                    if (question.getKnowledgeId() == knowledgeController.getSelected() && question.getType() == this.questionType) {
                        searchedQuestionsList.add(question);
                    }
                }
                break;
            default:
                searchedQuestionsList = questionList;
        }
        return Optional.ofNullable(searchedQuestionsList).orElse(new LinkedList<>());
    }

    private void updateCurrentItem() {
        int count = applicationQuestionController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((Question) applicationQuestionController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }
//===================Question Type==========

    public boolean isType0() {
        return null == getSelected().getType();
    }

    //show the corresponding answer on the pages after practice
    String[] replacedAnser = new String[]{"A", "B", "C", "D"};
    // String result = "";

    //======================For single selection type question=====================
    private String selectionString1, selectionString2, selectionString3, selectionString4, selectionString5;
    private boolean secondContentReady = false;//
    private String[] secondContentStrings = null;

    public void prepareSecondContent() {
        if (getSelected().getId() != null //不为空
                && (applicationQuestionController.isType(getSelected(), 3)//是单项选择题
                || applicationQuestionController.isType(getSelected(), 6))//是多项选择题
                ) {
            secondContentReady = true;
            secondContentStrings = getSelected().getSecondcontent().split(StaticFields.splictOutString);
        } else {
            secondContentReady = false;
            secondContentStrings = null;
        }
    }

    public String getSelectionString1() {
        return secondContentReady ? secondContentStrings[0] : selectionString1;
    }

    public void setSelectionString1(String selectionString1) {
        if (selectionString1.trim().length() == 0) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Shouldnotbeblank"));
        } else {
            this.selectionString1 = selectionString1;
        }
    }

    public String getSelectionString2() {
        return secondContentReady ? secondContentStrings[1] : selectionString2;
    }

    public void setSelectionString2(String selectionString2) {
        if (selectionString2.trim().length() == 0) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Shouldnotbeblank"));
        } else {
            this.selectionString2 = selectionString2;
        }
    }

    public String getSelectionString3() {
        return secondContentReady ? secondContentStrings[2] : selectionString3;
    }

    public void setSelectionString3(String selectionString3) {
        if (selectionString3.trim().length() == 0) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Shouldnotbeblank"));
        } else {
            this.selectionString3 = selectionString3;
        }
    }

    public String getSelectionString4() {
        return secondContentReady ? (secondContentStrings.length > 3 ? secondContentStrings[3] : "") : selectionString4;
    }

    public void setSelectionString4(String selectionString4) {
        if (selectionString4.trim().length() == 0) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Shouldnotbeblank"));
        } else {
            this.selectionString4 = selectionString4;
        }
    }

    public String getSelectionString5() {
        return secondContentReady ? (secondContentStrings.length > 4 ? secondContentStrings[4] : "") : selectionString5;
    }

    public void setSelectionString5(String selectionString5) {
        if (selectionString5.trim().length() == 0) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Shouldnotbeblank"));
        } else {
            this.selectionString5 = selectionString5;
        }
    }
    //===========================For objective programming, the test data=============
    private String inputDataString, outputDataString;

    public String getInputDataString() {
        return inputDataString;
    }

    public void setInputDataString(String inputDataString) {
        this.inputDataString = inputDataString;
    }

    public String getOutputDataString() {
        return outputDataString;
    }

    public void setOutputDataString(String outputDataString) {
        this.outputDataString = outputDataString;
    }
    //========================For subject programming, the file upload===============
    private Part answerFile;

    public Part getAnswerFile() {
        return answerFile;
    }

    public void setAnswerFile(Part answerFile) {
        this.answerFile = answerFile;
    }
//==============For multi selection question============

    public String[] getMultSelectionStrings() {
        if (secondContentReady) {
            multSelectionStrings = getSelected().getAnswer().split(",");
        }
        return multSelectionStrings;
    }

    public void setMultSelectionStrings(String[] multSelectionStrings) {
        this.multSelectionStrings = multSelectionStrings;
    }
//===============获取单项选择题目的选项===============

    public String[] getSelectionOptionStrings(Question question) {
        String[] answer = null;
        if (applicationQuestionController.isMultiSelection(question) || applicationQuestionController.isSingleSelection(question)) {
            answer = question.getSecondcontent().split("\\$#");
        }
        return answer;
    }
//===============

    //===============获取多项选择题目的选项===============
    public String[] getSelectionOptionStrings4Multi(Question question) {
        String[] multAnswer =null;
        String[] backupStrings = new String[5];
        if (applicationQuestionController.isMultiSelection(question) || applicationQuestionController.isSingleSelection(question)) {
            backupStrings = question.getSecondcontent().split("\\$#");
        }
        if (backupStrings.length == 4) {//有4个选项
            multAnswer = new String[5];
            System.arraycopy(backupStrings, 0, multAnswer, 0, 4);
            multAnswer[4] = "";
        } else {//有5个选项
            multAnswer = backupStrings;
        }
        return multAnswer;
    }
//===============
    //For multipFill
    List<String> temMultiFillList = new ArrayList<>();

    public List<String> getMultiFillValue() {
        temMultiFillList.clear();
        temMultiFillList.addAll(Arrays.asList(getSelected().getAnswer().split(StaticFields.THIRDDELIMITED)));
        return temMultiFillList;
    }

    public void setMultiFillValue(List<String> listValue) {
        temMultiFillList = listValue;
    }

    public boolean hasFigure() {
        return null != current.getFigure() && current.getFigure().trim().length() > 0;
    }

//每门课程都有对应的习题
    public HashMap<Subject, Set<Question>> getSubjectCorrectQuestion() {
        if (subjectQuestion.isEmpty()) {
            List<Subject> temSubjects = subjectController.getSubjectList(1);
            for (Subject subject : temSubjects) {
                subjectQuestion.put(subject, new HashSet<>());
            }
        }
        return subjectQuestion;
    }

    //Questions for this subject
    //To put these methods inot sessionController to avoid frequently exchange data with database
    //To put these methods into applicationController has its advantage that can deal for all the subject, but do not stabel, because will have to exchange data from database
    private HashMap<Integer, List<Question>> mappedQuestionRepository;

    public HashMap<Integer, List<Question>> getMappedQuestionRepository() {
        if (null == mappedQuestionRepository) {
            mappedQuestionRepository = new HashMap<>();
            applicationQuestionController.getQuestions4Subject(subjectController.getSelected()).stream().map((question) -> {
                if (null == mappedQuestionRepository.get(question.getScore())) {
                    mappedQuestionRepository.put(question.getScore(), new LinkedList<>());
                }
                return question;
            }).forEachOrdered((question) -> {
                mappedQuestionRepository.get(question.getScore()).add(question);
            });
        }
        return mappedQuestionRepository;
    }
    /*
    for getAGTP, one can refer to the paper "Auto-generated Test paper based on Knowledge Embedding"
     */
    Random random = new Random();

    private List<Question> getAGTP(float p, List<Question> questionRepository) {
        List<Question> questionsList = new LinkedList<>();
        questionRepository.forEach((_item) -> {
            if (random.nextFloat() < p) {
                questionsList.add(_item);
            }
        });
        return questionsList;
    }


    /*
    for coarse KEM, one can refer to the paper "Auto-generated Test paper based on Knowledge Embedding"
     */
    public List<Question>[] getTPwithCoarseKEM(int targetScore, List<Question> questionRepository, int size) {
        List<Question>[] myresult = new LinkedList[size];
        int sumscore = 0;
        sumscore = questionRepository.stream().map((question) -> question.getScore()).reduce(sumscore, Integer::sum);
        for (int i = 0; i < size; i++) {
            myresult[i] = getAGTP(targetScore / ((float) sumscore), questionRepository);
        }
        return myresult;
    }

    /*
    for fine KEM, one can refer to the paper "Auto-generated Test paper based on Knowledge Embedding"
     */
    public List<Question>[] getTPwithFineKEM(int targetScore, List<Question> questionRepository, int size) {
        List<Question>[] myresult = new LinkedList[size];
        HashMap<Integer, List<Question>> temMappedQR = getMappedQuestionRepository();

        int sumscore = 0;
        sumscore = questionRepository.stream().map((question) -> question.getScore()).reduce(sumscore, Integer::sum);
        for (int i = 0; i < size; i++) {
            myresult[i] = getAGTP(targetScore / ((float) sumscore), questionRepository);
        }
        return myresult;
    }

    public String deleteImage() {
        //删除掉服务器中的文件
        File file = new File(personalSessionSetup.getFilePath() + "/" + imageURL());
        if (file.exists()) {
            file.delete();
        }
        getSelected().setFigure("");
        //更新数据库的figure值
        applicationQuestionController.edit(getSelected());
        return null;
    }

    /*
                    表格中判断是什么题型：
                        A 简答题
                        B 填空
                        C 单选
                        D 判断
                        E 客观编程
                        F 多选
                        G 主观编程
                    数据库中判断：
                        1 简答
                        2 填空
                        3 单选
                        4 判断
                        5 客观编程
                        6 多选
                        7 主观编程
     */
    public String uploadQuestions() {
        InputStream in = null;
        Question q = null;
        try {
            if (questionExcel != null) {
                in = questionExcel.getInputStream();
                Workbook book = Workbook.getWorkbook(in);
                Sheet sheet = book.getSheet(0);
                List<Knowledge> knowledgeList = null;
                Knowledge knowledge = null;
                int sheetRows = sheet.getRows();
                for (int i = 1; i < sheetRows; i++) {
                    q = new Question();
                    //判断数据库是否有此知识点
                    knowledgeList = applicationKnowledgeController.getQueryResultList("select * from knowledge where name='" + sheet.getCell(3, i).getContents() + "'");
                    if (knowledgeList != null && !knowledgeList.isEmpty()) {
                        knowledge = (Knowledge) knowledgeList.get(0);
                    }
                    //存在的话
                    if (knowledge != null) {
                        //再判断数据库是否有此记录，根据题目内容和知识点id
                        //判断是选择题还是其他题型
                        String questionTypestr = sheet.getCell(5, i).getContents().trim();
                        if (questionTypestr.equals("C")) {
                            //去掉字符串中的换行
                            String questionContent = sheet.getCell(1, i).getContents().trim().replaceAll("\r|\n", "");
                            q.setValueinfo(questionContent.substring(0, convertToInt(questionContent, 'A')));
                            if (applicationQuestionController.getQueryResultList("select * from question where knowledge_id=" + knowledge.getId() + " and valueinfo='" + q.getValueinfo() + "'").isEmpty()) {
                                String answerStr = questionContent.substring(convertToInt(questionContent, 'A'));
                                //加2是因为"A."长度为2，且不需要此串
                                //此处没有使用StringBuffer,会占用更多的内存空间
                                q.setSecondcontent(answerStr.substring(convertToInt(answerStr, 'A') + 2, convertToInt(answerStr, 'B')).trim()
                                        + "$#" + answerStr.substring(convertToInt(answerStr, 'B') + 2, convertToInt(answerStr, 'C')).trim()
                                        + "$#" + answerStr.substring(convertToInt(answerStr, 'C') + 2, convertToInt(answerStr, 'D')).trim()
                                        + "$#" + answerStr.substring(convertToInt(answerStr, 'D') + 2).trim());
                            } else {
                                continue;
                            }

                        } else {
                            q.setValueinfo(sheet.getCell(1, i).getContents().trim());
                            if (!applicationQuestionController.getQueryResultList("select * from question where knowledge_id=" + knowledge.getId() + " and valueinfo='" + q.getValueinfo() + "'").isEmpty()) {
                                continue;
                            }
                        }
                        //都需要设置的属性
                        q.setAnswer(sheet.getCell(2, i).getContents());
                        //如果知识点已经导入数据库，则不会出错，否则则会报错
                        q.setKnowledgeId(knowledge);
                        q.setAnalysis(sheet.getCell(6, i).getContents());
                        //设置题目类型
                        q.setType(judgeQuestionType(questionTypestr));
                        //其他属性设置为默认值
                        q.setDegree(2);
                        q.setNeedtime(3);
                        q.setScore(5);
                        //写入数据库
                        applicationQuestionController.create(q);
                        reexaminationController.createReexamination(null, q);
                    }

                }
            }

        } catch (IOException | BiffException ex) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return null;
    }

    //为了表格中为A. || A． || A、取字符串索引出错
    public int convertToInt(String str, char ch) {
        int strIndex1 = str.indexOf(ch + ".");
        int strIndex2 = strIndex1 == -1 ? str.indexOf(ch + "．") : strIndex1;
        int strIndex3 = strIndex2 == -1 ? str.indexOf(ch + "、") : strIndex2;
        return strIndex3;
    }

    public Integer judgeQuestionType(String qStr) {
        Integer qTypeInteger = 0;
        switch (qStr) {
            case "A":
                qTypeInteger = 1;
                break;
            case "B":
                qTypeInteger = 2;
                break;
            case "C":
                qTypeInteger = 3;
                break;
            case "D":
                qTypeInteger = 4;
                break;
            case "E":
                qTypeInteger = 5;
                break;
            case "F":
                qTypeInteger = 6;
                break;
            case "G":
                qTypeInteger = 7;
                break;
        }

        return qTypeInteger;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public HashMap<String, Integer> getWrongQuestions() {
        HashMap<String, Integer> result = new HashMap<>();
        //applicationStudenttestpaperController.get.getWrongquestionList()
        //getSelected().getId()
        return result;
    }

    List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void startPractice(Knowledge k) {
        questions = new ArrayList<>();
        List<Question> allQuestions = new ArrayList<>();
        Set<Question> requiredQuestion = new HashSet<>();
        if (k != null) {
            allQuestions = applicationQuestionController.getQueryResultList("select * from question where knowledge_id=" + k.getId());
        }
        if (allQuestions.isEmpty() || allQuestions.size() <= 10) {
            allQuestions.forEach(e -> {
                questions.add((Question) e);
            });
        } else {
            for (int i = 0; i < 10; i++) {
                int randomQuestion = (int) Math.floor(Math.random() * allQuestions.size());
                requiredQuestion.add(applicationQuestionController.find(randomQuestion));
            }
            questions.addAll(requiredQuestion);
        }
    }

    public void getQuestionPage() {
        startPractice(knowledgeController.getSelected());
        mainXhtml.setPageName("studentInfo/collectAnswer4Questions");
    }

    Object[] allAnswers = new Object[10];

    public Object[] getAllAnswers() {
        return allAnswers;
    }

    public void setAllAnswers(Object[] allAnswers) {
        this.allAnswers = allAnswers;
    }

    //只能提交一次
    private boolean correctAnswerFlag = true;

    public boolean isCorrectAnswerFlag() {
        return correctAnswerFlag;
    }

    public void setCorrectAnswerFlag(boolean correctAnswerFlag) {
        this.correctAnswerFlag = correctAnswerFlag;
    }

    public String getPromptMsg() {
        return promptMsg;
    }

    private boolean btnFlag = false;
    private String promptMsg = "";
    private final double standardAccuracy = 0.8;

    Set<Question> correctQuestions = new HashSet<>();
    Set<Question> wrongQuestions = new HashSet<>();

    public String correctAnswer() {

        int correctAnswerCnt = 0;
        for (int i = 0; i < 10; i++) {
            if (allAnswers[i].equals(questions.get(i).getAnswer())) {
                correctQuestions.add(questions.get(i));
                correctAnswerCnt++;
            } else {
                wrongQuestions.add(questions.get(i));
            }
        }

        double myAccuracy = correctAnswerCnt / 10;
        if (myAccuracy > standardAccuracy) {
            //更新知识点前沿，直接取得当前知识点所有后继知识点，随机赋值给一个知识点？
            leadpointController.updateLeadPoint(correctQuestions, wrongQuestions);
            promptMsg = "此知识点的学习已完成";

        } else {
            //提示正确率不够，请继续努力
            promptMsg = "正确率不够，请继续努力";
        }
        correctAnswerFlag = false;
        allAnswers = null;
        selections = new String[4];
        cnt = 0;
        btnFlag = true;
        questions = new ArrayList<>();
        return null;
    }

    String[] selections = new String[]{"", "", "", ""};

    public String[] getSelections() {
        return selections;
    }

    public void setSelections(String[] selections) {
        this.selections = selections;
    }

    public List<String[]> getSelectionsList() {
        return selectionsList;
    }

    public void setSelectionsList(List<String[]> selectionsList) {
        this.selectionsList = selectionsList;
    }

    //
    List<String[]> selectionsList = new ArrayList<>();
    //$#分割
    int cnt = 0;

    //每一次循环完重新给ABCD选项复制
    public int loop() {
        String[] content = null;
        if (!questions.isEmpty()) {
            String qString = questions.get(cnt).getSecondcontent();
            if (qString != null) {
                content = qString.split("\\$#");
            }
        }

        if (content != null) {
            for (int i = 0; i < content.length; i++) {
                selections[i] = content[i];
            }
        } else {
            selections = new String[4];
        }
        selectionsList.add(selections);
        if (cnt != 9) {
            cnt += 1;
        } else {
            cnt = 0;
        }
        return 0;
    }

    public boolean isBtnFlag() {
        return btnFlag;
    }

    private int fileTypeFlag = -1;

    public void next() {
        if (fileTypeFlag != 3) {
            fileTypeFlag += 1;
        }
    }

    public void initFlag(int flag) {
        fileTypeFlag = flag;
        if (learningresourceController.getAIPfileNames(0, getSelected().getKnowledgeId()).length == 0) {
            fileTypeFlag = -1;
        }

    }

    public int getFileTypeFlag() {
        return fileTypeFlag;
    }

    public void getLearningResourcePage() {
        fileTypeFlag = -1;
        btnFlag = false;
        mainXhtml.setPageName("studentInfo/learningReaourceRecommand");
    }

    public boolean getQuestionTitle() {
        return questions.size() > 0;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
    //-------------------------------

    public Part getImagePart() {
        return imagePart;
    }

    public void setImagePart(Part imagePart) {
        this.imagePart = imagePart;
    }

    public void setSelected(Question question) {
        this.current = question;
        prepareSecondContent();
    }

    //在简答题的批改过程中，学生的答案与原答案在页面上完全一样，但仍然判错。
    //由于在前期收集答案时，采用了富文本，所以后台答案中包含\r\n等特殊。
    //解决思路：删除这些特殊字符
    //实现办法：用空字符串替换这些特殊字符。
    public void dealRichText() {
        //读取所有的question
        List<Question> allQuestions = applicationQuestionController.getQueryResultList("select * from question");
        //遍历answer,//替换，不用检查，因为检查会不存在，因为在contanins函数中把转义符给略掉了
        for (Question question : allQuestions) {
            String result = question.getAnswer().replace("\r\n", "");
            question.setAnswer(result);
            applicationQuestionController.edit(question);
        }
    }

    public void prepareQuestionList(Integer status) {
        User user = null;
        switch (commonSession.getUserType()) {
            case Student:
                user = studentController.getLogined();
                break;
            case Teacher:
            case Secretary:
            case Admin:
                user = teacherAdminController.getLogined();
                break;
            case Parent:
                user = parentController.getLogined();
                break;
        }
        List<Question> result = getQuestionList(user, status);
        pageOperation.refreshData(result);
        if (result.size() > 0) {
            setSelected(result.get(0));
        }
    }

    private List<Question> getQuestionList(User user, Integer status) {
        List<Question> result = new LinkedList<>();
        if (null != user && null != user.getId()) {
            List<Reexamination> reexaminations = reexaminationController.getReexaminationQuestionListRecorderBy(user, status);
            if (reexaminations.size() > 0) {
                StringBuilder sb = new StringBuilder();
                reexaminations.forEach(re -> {
                    sb.append(re.getQuestionid().getId()).append(",");
                });
                String ids = sb.toString();
                if (ids.length() > 0) {
                    ids = ids.endsWith(",") ? ids.substring(0, ids.length() - 1) : ids;
                    result = questionFacadeLocal.getQueryResultList("select * from Question where id in (" + ids + ")");
                }//result = getFacade().getKnowledge4Student(student);
            }
        }
        return result;
    }

    public void prepareMyExaminedQuestionList(Integer status, Integer secondStatus) {
        TeacherAdmin user = teacherAdminController.getLogined();
        List<Question> result = null;
        StringBuilder sb = new StringBuilder();
        List<Reexamination> reexaminations = reexaminationController.getReexaminationNeedToBeExamined(user, status, 1, secondStatus);
        reexaminations.forEach(re -> {
            sb.append(re.getQuestionid().getId()).append(",");
        });
        String ids = sb.toString();
        if (ids.length() > 0) {
            ids = ids.endsWith(",") ? ids.substring(0, ids.length() - 1) : ids;
            result = questionFacadeLocal.getQueryResultList("select * from question where id in (" + ids + ")");
        } else {
            result = new LinkedList<>();
        }
        pageOperation.refreshData(result);
        if (result.size() > 0) {
            setSelected(result.get(0));
        }
    }

    //从myPublishedQuestion来调用Edit和View页面
    private boolean fromPublished = false;

    //在prepareList中设置回false   
    public boolean isFromPublished() {
        return fromPublished;
    }

    public void setFromPublished(boolean fromPublished) {
        this.fromPublished = fromPublished;
    }
}
