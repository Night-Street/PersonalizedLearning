package modelController.applicationController;

import entities.Knowledge;
import entities.Question;
import entities.Reexamination;
import entities.Student;
import entities.Studenttestpaper;
import entities.Subject;
import entities.TeacherAdmin;
import entities.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import sessionBeans.QuestionFacadeLocal;
import tools.StaticFields;

@Named("questionControllerA")
@ApplicationScoped
public class QuestionController extends ApplicationCommonController {

    @EJB
    private QuestionFacadeLocal ejbFacadelocal;
    @Inject
    private modelController.applicationController.ReexaminationController applicationReexaminationController;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.SubjectController applicationSubjectController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.sessionController.ReexaminationController reexaminationController;
    final String splitInputOutputTest = "====";
    final String folder = "subjective";

    private final String trueAnswer = "T", falseAnswer = "F";
    public final static String SCHOOLNAMESTUDENTNAME = "SCHOOLNAMESTUDENTNAME", STUDENTNAME = "STUDENTNAME";
    public final static int[] questionIndexs = {1, 2, 3, 4, 5, 6, 7, 8};

    public QuestionController() {
    }

    private QuestionFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public Question findByName(String name) {
        return getFacade().findByName(name);
    }
//=================处理学生试卷==============开始=======
    Studenttestpaper temStored = new Studenttestpaper();//用于检测是否重复查询
    ArrayList testQuestions = new ArrayList();

    public synchronized List<Question> getTestQuestion(Studenttestpaper temTestpaper) {
        if (temStored == null || null == temStored.getId() || !temStored.getId().equals(temTestpaper.getId())) {
            //还没开始查询，所以id为空，或者已经发生了改变，所以需要查
            temStored = temTestpaper;
            testQuestions.clear();
            //只有当不相等时，才去检索
            if (temTestpaper != null) {
                if (null != temTestpaper.getQuestionIds()) {
                    String[] questionIds = temTestpaper.getQuestionIds().split(",");
                    for (String questionId : questionIds) {
                        Question tem = getFacade().find(Integer.valueOf(questionId));
                        if (null != tem) {//由于删除某些题目，导致会出现空的情况
                            testQuestions.add(tem);
                        }
                    }
                }
            }
        }
        return testQuestions;
    }
//==========================处理学生试卷==============结束 =======

    public Set<Question> getQuestions4Subject(Subject subject) {
        Set<Question> result = new HashSet<>();
        List<Knowledge> knowledgesList = applicationKnowledgeController.getKnowledgeList4Subject(subject);
        knowledgesList.forEach((knowledge) -> {
            result.addAll(knowledge.getQuestionSet());
        });
        return result;
    }

    public String getAnswer(Question question) {
        //包装选择题，包括单项选择和多项选择题。把选择题的答案1，2转换为A，B
        String result = "";
        try {
            switch (question.getType()) {
                case StaticFields.SINGLESELECTION:
                    result = getCharacter4Number(question.getAnswer());
                    break;
                case StaticFields.MULTISELECTION:
                    String[] tem = question.getAnswer().split(",");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < tem.length - 1; i++) {
                        sb.append(getCharacter4Number(tem[i]));
                        sb.append(",");
                    }
                    sb.append(getCharacter4Number(tem[tem.length - 1]));
                    result = sb.toString();
                    break;
                case StaticFields.JUDGMENT:
                    if (question.getAnswer().trim().equals("T")) {
                        result = commonSession.getResourceBound().getString("True");
                    } else {
                        result = commonSession.getResourceBound().getString("False");
                    }
                    break;
                default:
                    result = question.getAnswer();
                    break;
            }
        } catch (Exception e) {
        }
        return result;
    }

    public List<Question> getQuestion4Student(Student student) {
        return getQuestions4User(student);
    }

    public List<Question> getQuestion4Teacher(TeacherAdmin teacher) {
        return getQuestions4User(teacher);
    }

    private List<Question> getQuestions4User(User user) {
        setDataChanged(false);
        List<Reexamination> reexaminations = reexaminationController.getReexaminationQuestionListRecorderBy(user, null);
        StringBuilder sb = new StringBuilder();
        reexaminations.forEach(reex -> {
            sb.append(reex.getQuestionid().getId());
        });
        return getFacade().getQueryResultList("select * from question where id in (" + sb.toString() + ")");
    }

    public List<Question> getQuestion4Knowledge(Knowledge knowledge) {
        setDataChanged(false);
        return getFacade().getQuestion4Knowledge(knowledge);
    }

    public Question getQuestion(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacadelocal.find(id);
    }

    public boolean isSimpleAnswer(Question question) {
        return question.getType().equals(StaticFields.SIMPLEANSWER);
    }

    public boolean isSingleFill(Question question) {
        return question.getType().equals(StaticFields.SINGLEFILL);
    }

    public boolean isSingleSelection(Question question) {
        return question.getType().equals(StaticFields.SINGLESELECTION);
    }

    public boolean isJudgment(Question question) {
        return question.getType().equals(StaticFields.JUDGMENT);
    }

    public boolean isObjectiveProgram(Question question) {
        return question.getType().equals(StaticFields.OBJECTIVEPROGRMA);
    }

    public boolean isMultiSelection(Question question) {
        return question.getType().equals(StaticFields.MULTISELECTION);
    }

    public boolean isSubjectiveProgram(Question question) {
        return question.getType().equals(StaticFields.SUBJECTIVEPROGRAM);
    }

    public boolean isMULTIFILL(Question question) {
        return question.getType().equals(StaticFields.MULTIFILL);
    }

    private void removeStyle(Question question) {
        if (question.getAnalysis().contains("<td")) {
            question.setAnalysis(StaticFields.replaceCommonStyle(question.getAnalysis()));
        }
        if (question.getAnswer().contains("<td")) {
            question.setAnswer(StaticFields.replaceCommonStyle(question.getAnswer()));
        }
        if (question.getValueinfo().contains("<td")) {
            question.setValueinfo(StaticFields.replaceCommonStyle(question.getValueinfo()));
        }
    }

    public void create(Question question) {
        removeStyle(question);
        getFacade().create(question);
        setDataChanged(true);
    }

    public void remove(Question question) {
        try {
            Set<Reexamination> questReexaminations = question.getReexaminationSet();
            for (Reexamination reexamination : questReexaminations) {
                applicationReexaminationController.remove(reexamination);
            }
            getFacade().remove(question);
            setDataChanged(true);
        } catch (Exception e) {
        }
    }

    public void edit(Question question) {
        removeStyle(question);
        getFacade().edit(question);
        setDataChanged(true);
    }

    public List<Question> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Question.class)
    public static class QuestionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            QuestionController controller = (QuestionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "questionControllerA");
            return controller.getQuestion(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Question) {
                Question o = (Question) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Question.class.getName());
            }
        }

    }

    public List<SelectItem> getQuestionTypeItem() {
        List<SelectItem> resulta = new LinkedList<>();
        for (int i : questionIndexs) {
            resulta.add(new SelectItem(i, commonSession.getResourceBound().getString("QuestionType" + i)));
        }
        return resulta;
    }
//选择题目的答案的viewer

    public List<SelectItem> secondContentOptions(Question question) {
        List<SelectItem> selectContnetOptions = new LinkedList<>();
        Character start = 'A';
        int i = 1;
        for (; i <= 4; i++, start++) {
            selectContnetOptions.add(new SelectItem(String.valueOf(i), String.valueOf(start)));
        }
        if (question.getType() == StaticFields.MULTISELECTION) {
            selectContnetOptions.add(new SelectItem(String.valueOf(i), String.valueOf(start)));
        }
        return selectContnetOptions;
    }

    List<SelectItem> trueOrFalsItems = new LinkedList<>();

    public List<SelectItem> getTrueOrFalsItems() {
        if (trueOrFalsItems.isEmpty()) {
            trueOrFalsItems.add(new SelectItem(trueAnswer, commonSession.getResourceBound().getString("Right")));
            trueOrFalsItems.add(new SelectItem(falseAnswer, commonSession.getResourceBound().getString("Wrong")));
        }
        return trueOrFalsItems;
    }

    public String getTypeString(Question question) {
        return getTypeString(question.getType());
    }

    public String getTypeString(int type) {
        for (int i : questionIndexs) {
            if (type == i) {
                return commonSession.getResourceBound().getString("QuestionType" + type);
            }
        }
        return "";
    }

    public int getNumber(Question question) {
        int numberResult = 0;
        switch (question.getType()) {
            case StaticFields.SIMPLEANSWER:
            case StaticFields.SINGLEFILL:
            case StaticFields.JUDGMENT:
                numberResult = 1;
                break;
            case StaticFields.SINGLESELECTION:
                numberResult = 4;
                break;
            case StaticFields.OBJECTIVEPROGRMA:
                numberResult = 1;
                break;
            case StaticFields.MULTISELECTION:
                numberResult = question.getSecondcontent().split("$#").length;
                break;
            case StaticFields.SUBJECTIVEPROGRAM:
                numberResult = 1;
                break;
            case StaticFields.MULTIFILL:
                numberResult = question.getAnswer().split(StaticFields.THIRDDELIMITED).length;
                break;
        }
        return numberResult;
    }
    //show the corresponding answer on the pages after practice
    String[] replacedAnser = new String[]{"A", "B", "C", "D", "E"};
    String result = "";
    List<SelectItem> questionDegree = new LinkedList<>();

    public String getShownQuestionAnswer(Question question) {
        if (null == question.getAnswer()) {//只查看试卷，学生尚未作答
            return "";
        }
        String answerString = "";
        answerString = question.getAnswer();
        if (isType(question, StaticFields.SINGLESELECTION) || isType(question, StaticFields.MULTISELECTION)) {
            String[] ans = answerString.split(",");
            result = "";
            for (String si : ans) {
                result += "," + replacedAnser[Integer.parseInt(si) - 1];
            }
            answerString = result.substring(1);
        }
        if (isType(question, StaticFields.JUDGMENT)) {
            if (answerString.equals("T")) {
                answerString = commonSession.getResourceBound().getString("True");
            }
            if (answerString.equals("F")) {
                answerString = commonSession.getResourceBound().getString("False");
            }
        }
        return answerString;
    }

    public boolean isFill(Question question) {
        return isType(question, StaticFields.SINGLEFILL) || isType(question, StaticFields.MULTIFILL);
    }

    public boolean isSelectionQuestion(Question question) {
        return isType(question, StaticFields.SINGLESELECTION) || isType(question, StaticFields.MULTISELECTION);
    }

    public boolean isTextare(Question question) {
        return question.getType().equals(StaticFields.SINGLEFILL) || question.getType().equals(StaticFields.SIMPLEANSWER);
    }

    public boolean isType(Question question, int i) {
        if (question.getType() == null) {
            return false;
        } else {
            return question.getType() == i;
        }
    }

    public String getCharacter4Number(String index) {
        return String.valueOf((char) ('@' + Integer.parseInt(index)));//@加1得到A
    }

    public List<SelectItem> getQuestionDegreeItem() {
        if (questionDegree.isEmpty()) {
            questionDegree = Arrays.asList(new SelectItem[]{
                new SelectItem(0, commonSession.getResourceBound().getString("D1Knowledge")),
                new SelectItem(1, commonSession.getResourceBound().getString("D2Comprehension")),
                new SelectItem(2, commonSession.getResourceBound().getString("D3Application")),
                new SelectItem(3, commonSession.getResourceBound().getString("D4Analysis")),
                new SelectItem(4, commonSession.getResourceBound().getString("D5Synthesis")),
                new SelectItem(5, commonSession.getResourceBound().getString("D6Evaluation"))
            }
            );
        }
        return questionDegree;
    }

    public HashMap<String, Integer> getQuestionTypeNumberMap(Subject subject) {
        HashMap<String, Integer> qtnm = new HashMap<>();
        List typeList = getFacade().getGeneralQueryList(
                "select count(*),type from question where knowledge_id in ("
                + applicationSubjectController.getKnowlegeIdsString(subject) + " group by(type))");
        for (Object object : typeList) {
            Integer value = (Integer) object;
            qtnm.put(commonSession.getResourceBound().getString("QuestionType" + value), value);
        }
        return qtnm;
//            switch (value) {
//                case 1:
//                    qtnm.put(commonSession.getResourceBound().getString("QuestionType1"), value);
//                    break;
//                case 2:
//                    qtnm.put(commonSession.getResourceBound().getString("QuestionType2"), value);
//                    break;
//                case 3:
//                    qtnm.put(commonSession.getResourceBound().getString("QuestionType3"), value);
//                    break;
//                case 4:
//                    qtnm.put(commonSession.getResourceBound().getString("QuestionType4"), value);
//                    break;
//                case 5:
//                    qtnm.put(commonSession.getResourceBound().getString("QuestionType5"), value);
//                    break;
//                case 6:
//                    qtnm.put(commonSession.getResourceBound().getString("QuestionType6"), value);
//                    break;
//                case 7:
//                    qtnm.put(commonSession.getResourceBound().getString("QuestionType7"), value);
//                    break;
//                case 8:
//                    qtnm.put(commonSession.getResourceBound().getString("QuestionType8"), value);
//                    break;
//            }

    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Question> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Question find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public String getQuestionSize4Knowledge(Knowledge knowledge) {
        if (knowledge != null && knowledge.getId() != null) {
            int number = 0;
            HashSet<String> questionType = new HashSet<>();
            List<Question> temQuestions = getQuestion4Knowledge(knowledge);
            if (!temQuestions.isEmpty()) {
                number = temQuestions.size();
            }
            temQuestions.forEach((question) -> {
                questionType.add(getTypeString(question));
            });
            StringBuilder sb = new StringBuilder();
            questionType.forEach(name -> {
                sb.append(",");
                sb.append(name);
            });
            return number+sb.toString();
        } else {
            return "0";
        }
    }
}
