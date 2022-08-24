package tools;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * @author Idea
 */
public class StaticFields {

    public static final short FONT_COLOR = 1, TITLE_COLOR = 2, BACKGROUND_COLOR = 3, ASIDE_BACKROUND_COLOR = 4, CONTENT_BACKGROUND_COLOR = 5;
    public static final int MAJORREF = 4, SUBJECTREF = 5, CLASSREF = 6, SUCCESSOR = 1, PREDCESSOR = 0;
    public static final int NUM_NEWS_SHOWN = 10, PAGESIZE = 100, COLUMNNUM5 = 5, COLUMNNUM3 = 3;
    public static final int QUESTIONANSWERINTERVAL = 5;
    public static final double DISTHRESHOLD = 0.5;
    public static final String[] CATE_TYPE = new String[]{"文科", "理科", "工科"};
    public static final String LONGIN_PATH = "/noLogin/login?faces-redirect=true";
    public static final String REGISTER_PATH = "/noLogin/register?faces-redirect=true";
    public static final String SESSION_MYUSER = "myUser";
    public static final String REQUEST_NEWS_ID = "newsId";
    public static final String SESSION_SCHOOL_ID = "schoolID";//当导入某个学校的学生信息时，先把该学校加入，并获得ID值，从而在从Excel中导入学生信息时，把该ID对应的学校对象作为其内容，填到数据库中。 
    public static int STU_NEWS_COUNT = 0, TEA_NEWS_COUNT = 0;//非final，所以当有新闻加入时，其值可以发生改变。 
    //public static final ResourceBundle MESSAGES_PROPTES = ResourceBundle.getBundle("messages");
    public static final String LOGIN_ROLE = "login_role";
    public static final String MAIN_PAGE = "/operation/main?faces-redirect=true";
    public static final String NOLOGIN_MAIN_PAGE = "/noLogin/main?faces-redirect=true", RELOGINWARN = "./noLogin/reloginWarning?faces-redirect=true";
    //public static final String STUDENTREF = "Student";
    public static final String TEACHERREF = "Teacher";
    //一级分隔符，用在将用户试卷写入数据库时,FIRSTDELIMITED把题目答案分开；SECONDDELIMITED用于填空题目时，有多个候选答案的分隔；THIRDDELIMITED用于多项填空题目的答案分开
    public static final String FIRSTDELIMITED = "#&&#", SECONDDELIMITED = "###", THIRDDELIMITED = "---";
    public static final double SIMPLEANSWERCORECTTHRESHOLD = 0.7;
    public static final int IMAGETYPE = 0, VIDEOTYPE = 1, AUDIOTYPE = 2, PDFTYPE = 3;
    public static final String IMAGE = "image", VIDEO = "video", AUDIO = "audio", PDF = "pdf";
    public static final Integer QUESTIONNUMBER = 200;
    public static final String KNOWLEDGEINNERSPLITBEGIN = "zsdBG", KNOWLEDGEINNERSPLITEND = "zsdED";
    //splitInString用于在数据库中保存时分隔选择题的选项，splictOutString用于从数据库中取出来后再分隔呈现在界面上时用
    public static final String splitInString = "$#", splictOutString = "\\$#";
    //题目类型
    public final static int SIMPLEANSWER = 1, SINGLEFILL = 2, SINGLESELECTION = 3, JUDGMENT = 4, OBJECTIVEPROGRMA = 5, MULTISELECTION = 6, SUBJECTIVEPROGRAM = 7, MULTIFILL = 8;
    /*
    对于多项填空题，学生对多个空的答案之间用FIRSTDELIMITED分割
                 参考答案之间也用FIRSTDELIMITED分割
                 参考答案的多个候选项之间用SECONDDELIMITED分割
                 因此，多项填空题中空的个数为FIRSTDELIMITED＋1
     */
    public static final int DAY_NEWS = -10;
    public static final String OPERATIONINSERT = "I", OPERATIONUPDATE = "U", OPERATIONDELETE = "D", OPERATIONSEARCH = "S";
    public static String DATESTRING = "";
    public static final String APPNAME = "/PersonalizedLearning-war", WEBINFO = "WEB-INF";
    public static final String IPADDRESS = "IPADDRESS";
    public static final String PROJECT_NAME = "PersonalizedLearning";
    public static int MAXDEPTH = 9;
    //采用富文本，下面这个style出现在<td>中,占用空间太多，因此在进入数据库前删除
    public static final String commonStyle1 = "border-bottom:1px solid #000000; border-left:1px solid #000000; border-right:1px solid #000000; border-top:1px solid #000000; vertical-align:middle; white-space:nowrap";

    public static String replaceCommonStyle(String tdString) {
        String a = tdString.replace(commonStyle1, "border-style:solid;");
        return a;
    }

    public static String getToday() {
        boolean recalcute = false;
        if (DATESTRING.contains("-")) {
            String temString = DATESTRING.substring(DATESTRING.lastIndexOf("-"));
            if (Integer.parseInt(temString) != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                recalcute = true;
            }
        } else {
            recalcute = true;
        }
        if (recalcute) {
            Calendar cu = Calendar.getInstance();
            DATESTRING = cu.get(Calendar.YEAR) + "-" + cu.get(Calendar.MONTH) + "-" + cu.get(Calendar.DAY_OF_MONTH);
        }
        return DATESTRING;
    }

    public static String replaceSpacewithOne(String input) {
        //把字符串的多个空格变为一个空格
        String regex = "\\s+";
        Pattern result = Pattern.compile(regex);
        return input.replaceAll(result.pattern(), " ");
    }

    public static synchronized String encrypt(String password) {
        return new StrongPasswordEncryptor().encryptPassword(password);
        //Then put encryptedPassword into database.
    }
    //搜索引擎，获取多页搜索结果
    static int time = 4;

    public static List<String> searchDangdang(String sentence) {
        List<String> result = new LinkedList<>();
        for (int i = 1; i < time; i++) {
            result.add("http://search.dangdang.com/?key=" + sentence + "&act=input&page_index=" + i);
        }
        return result;
    }

    public static List<String> searchCsdn(String sentence) {
        List<String> result = new LinkedList<>();
        for (int i = 1; i < time; i++) {
            result.add("https://so.csdn.net/so/search/s.do?p=" + i + "&q=" + sentence + "&t=&u=");
        }
        return result;
    }

}
