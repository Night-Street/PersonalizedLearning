package modelController.viewerController;

import entities.Edgeamongknowledge;
import entities.Knowledge;
import entities.Partofspeech;
import entities.Question;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import tools.pagination.PaginationHelper;
import java.util.List;
import jxl.*;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import modelController.sessionController.CommonModelController;
import modelController.sessionController.MajorController;

@Named("englishdictionaryController")
@SessionScoped
public class EnglishdictionaryController extends CommonModelController implements Serializable {

    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    private PaginationHelper pagination;
    private final String tableName = "englishdictionary", listpage = "englishdictionary/List", editpage = "englishdictionary/Edit",
            viewpage = "englishdictionary/View", createpage = "englishdictionary/Create";
    @Inject
    private MainXhtml mainXhtml;

    @Inject
    private modelController.applicationController.PartofSpeechController applicationPartofSpeechController;
    @Inject
    private modelController.applicationController.EdgeamongknowledgeController applicationEdgeamongknowledgeController;
    @Inject
    private modelController.applicationController.QuestionController applicationQuestionController;
    @Inject
    private modelController.applicationController.SubjectController applicationSubjectController;
    @Inject
    private MajorController majorController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    private String searchName;

    public EnglishdictionaryController() {
    }
//Write words into knowledge from txt file

    public void readFile() {
        String pathname = "C:\\Users\\cmx\\Documents\\NetBeansProjects\\JavaApplication2\\t21.txt"; // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathname), "utf-8")); // 建立一个对象，它把文件内容转成计算机能读懂的语言

            String line;
            //网友推荐更加简洁的写法
            int i = 0;
            int beginindex1;
            int endindex1;
            int beginindex2;
            int endindex2;
            int beginindex3;
            int endindex3;
            int beginindex4;
            int endindex4;
            int beginindex5;
            int endindex5;
            String english = null;
            String pronunication = null;
            String partofspeech = null;
            String englishexpression = null;
            String chinese = null;
            String example = null;
            String meno = null;
            List<String> exList = new ArrayList();
            Knowledge ed = new Knowledge();
            Partofspeech pf = new Partofspeech();
            List<Partofspeech> partofspeechs = new ArrayList<>();

            //String chineseexpression = null;
            while ((line = br.readLine()) != null && i < 90000) {
                // 一次读入一行数据
                System.out.println(i + ":" + line);
                if (i % 2 == 0) {
                    english = line;
                } else {
                    beginindex1 = line.indexOf("/");
                    endindex1 = line.indexOf("/", beginindex1 + 1);
                    //System.out.println(beginindex+"  "+endindex);//输出为音标的前后位置

                    //此为存在音标的情况
                    if (beginindex1 > 0 && beginindex1 < 30) {
                        beginindex1 = beginindex1 + 1;
                        pronunication = line.substring(beginindex1, endindex1);
//                        System.out.println(pronunication);
                        //此为存在词性的情况
                        beginindex2 = line.indexOf(" ", endindex1 + 1);
                        endindex2 = line.indexOf(" ", beginindex2 + 1);
                        if (endindex2 == -1) {
                            endindex2 = line.length();
                        }
                        //System.out.println(beginindex2+"  "+endindex2);//输出为词性的前后位置
                        partofspeech = line.substring(beginindex2, endindex2);
                        if (partofspeech.equals(" n") || partofspeech.equals(" adj") || partofspeech.equals(" v") || partofspeech.equals(" prep") || partofspeech.equals(" adv")) {
                            beginindex2 = beginindex2 + 1;
                            partofspeech = line.substring(beginindex2, endindex2);
//                            System.out.println(partofspeech);
                        } else {
                            partofspeech = null;
//                            System.out.println(partofspeech);
                        }
                        //多个解释的情况

                        //有音标有词性多解
                        if (partofspeech != null) {

                            if (line.contains(" 1 ")) {
                                beginindex3 = line.indexOf(" 1 ");
                                if (line.substring(beginindex3).contains(" 2 ")) {
                                    endindex3 = line.indexOf(" 2 ", beginindex3);
                                    // System.out.println(line.substring(beginindex3, endindex3));
                                    exList.add(line.substring(beginindex3, endindex3));
                                    beginindex3 = endindex3;
                                    if (line.substring(beginindex3).contains(" 3 ")) {
                                        endindex3 = line.indexOf(" 3 ", beginindex3);
                                        //System.out.println(line.substring(beginindex3, endindex3));
                                        exList.add(line.substring(beginindex3, endindex3));
                                        beginindex3 = endindex3;
                                        if (line.substring(beginindex3).contains(" 4 ")) {
                                            endindex3 = line.indexOf(" 4 ", beginindex3);
                                            //System.out.println(line.substring(beginindex3, endindex3));
                                            exList.add(line.substring(beginindex3, endindex3));
                                            beginindex3 = endindex3;
                                            if (line.substring(beginindex3).contains(" 5 ")) {
                                                endindex3 = line.indexOf(" 5 ", beginindex3);
                                                //System.out.println(line.substring(beginindex3, endindex3));
                                                exList.add(line.substring(beginindex3, endindex3));
                                                beginindex3 = endindex3;
                                                if (line.substring(beginindex3).contains(" 6 ")) {
                                                    endindex3 = line.indexOf(" 6 ", beginindex3);
                                                    // System.out.println(line.substring(beginindex3, endindex3));
                                                    exList.add(line.substring(beginindex3, endindex3));
                                                    beginindex3 = endindex3;
                                                    if (line.substring(beginindex3).contains(" 7 ")) {
                                                        endindex3 = line.indexOf(" 7 ", beginindex3);
                                                        // System.out.println(line.substring(beginindex3, endindex3));
                                                        exList.add(line.substring(beginindex3, endindex3));
                                                        beginindex3 = endindex3;
                                                    } else {
                                                        endindex3 = line.length();
                                                        //System.out.println(line.substring(beginindex3, endindex3));
                                                        exList.add(line.substring(beginindex3, endindex3));
                                                    }
                                                } else {
                                                    endindex3 = line.length();
                                                    //System.out.println(line.substring(beginindex3, endindex3));
                                                    exList.add(line.substring(beginindex3, endindex3));
                                                }
                                            } else {
                                                endindex3 = line.length();
                                                // System.out.println(line.substring(beginindex3, endindex3));
                                                exList.add(line.substring(beginindex3, endindex3));
                                            }
                                        } else {
                                            endindex3 = line.length();
                                            //System.out.println(line.substring(beginindex3, endindex3));
                                            exList.add(line.substring(beginindex3, endindex3));
                                        }

                                    } else {
                                        endindex3 = line.length();
                                        //System.out.println(line.substring(beginindex3, endindex3));
                                        exList.add(line.substring(beginindex3, endindex3));
                                    }

                                } else {
                                    endindex3 = line.length();
                                    //System.out.println(line.substring(beginindex3, endindex3));
                                    exList.add(line.substring(beginindex3, endindex3));
                                }
                            } //有音标有词性一解
                            else {
                                beginindex3 = endindex2 + 1;
                                endindex3 = line.length();
                                //System.out.println(line.substring(beginindex3, endindex3));
                                exList.add(line.substring(beginindex3, endindex3));
                            }
                            //有音标无词性多解
                        } else {

                            if (line.contains(" 1 ")) {
                                beginindex3 = line.indexOf(" 1 ");
                                if (line.substring(beginindex3).contains(" 2 ")) {
                                    endindex3 = line.indexOf(" 2 ", beginindex3);
                                    // System.out.println(line.substring(beginindex3, endindex3));
                                    exList.add(line.substring(beginindex3, endindex3));
                                    beginindex3 = endindex3;
                                    if (line.substring(beginindex3).contains(" 3 ")) {
                                        endindex3 = line.indexOf(" 3 ", beginindex3);
                                        //System.out.println(line.substring(beginindex3, endindex3));
                                        exList.add(line.substring(beginindex3, endindex3));
                                        beginindex3 = endindex3;
                                        if (line.substring(beginindex3).contains(" 4 ")) {
                                            endindex3 = line.indexOf(" 4 ", beginindex3);
                                            //System.out.println(line.substring(beginindex3, endindex3));
                                            exList.add(line.substring(beginindex3, endindex3));
                                            beginindex3 = endindex3;
                                            if (line.substring(beginindex3).contains(" 5 ")) {
                                                endindex3 = line.indexOf(" 5 ", beginindex3);
                                                //System.out.println(line.substring(beginindex3, endindex3));
                                                exList.add(line.substring(beginindex3, endindex3));
                                                beginindex3 = endindex3;
                                                if (line.substring(beginindex3).contains(" 6 ")) {
                                                    endindex3 = line.indexOf(" 6 ", beginindex3);
                                                    // System.out.println(line.substring(beginindex3, endindex3));
                                                    exList.add(line.substring(beginindex3, endindex3));
                                                    beginindex3 = endindex3;
                                                    if (line.substring(beginindex3).contains(" 7 ")) {
                                                        endindex3 = line.indexOf(" 7 ", beginindex3);
                                                        // System.out.println(line.substring(beginindex3, endindex3));
                                                        exList.add(line.substring(beginindex3, endindex3));
                                                        beginindex3 = endindex3;
                                                    } else {
                                                        endindex3 = line.lastIndexOf(".");
                                                        //System.out.println(line.substring(beginindex3, endindex3));
                                                        exList.add(line.substring(beginindex3, endindex3));
                                                    }
                                                } else {
                                                    endindex3 = line.length();
                                                    // System.out.println(line.substring(beginindex3, endindex3));
                                                    exList.add(line.substring(beginindex3, endindex3));
                                                }
                                            } else {
                                                endindex3 = line.length();
                                                //System.out.println(line.substring(beginindex3, endindex3));
                                                exList.add(line.substring(beginindex3, endindex3));
                                            }
                                        } else {
                                            endindex3 = line.length();
                                            //System.out.println(line.substring(beginindex3, endindex3));
                                            exList.add(line.substring(beginindex3, endindex3));
                                        }

                                    } else {
                                        endindex3 = line.length();
                                        //System.out.println(line.substring(beginindex3, endindex3));
                                        exList.add(line.substring(beginindex3, endindex3));
                                    }

                                } else {
                                    endindex3 = line.length();
                                    // System.out.println(line.substring(beginindex3, endindex3));
                                    exList.add(line.substring(beginindex3, endindex3));
                                }
                            } //有音标无词性一解
                            else {
                                beginindex3 = endindex1 + 2;
                                endindex3 = line.length();
                                //System.out.println(line.substring(beginindex3, endindex3));
                                exList.add(line.substring(beginindex3, endindex3));
                            }
                        }

                    } //此为没有音标的情况
                    else {
                        pronunication = null;
                        beginindex2 = 2;
                        if (line.charAt(beginindex2) == ' ') {
                            beginindex2++;
                        }
                        endindex2 = line.indexOf(" ", beginindex2 + 1);
                        if (endindex2 == -1) {
                            endindex2 = line.length();
                        }
                        //System.out.println(beginindex2+"  "+endindex2); 
                        partofspeech = line.substring(beginindex2, endindex2);
                        if (partofspeech.equals("n") || partofspeech.equals("adj") || partofspeech.equals("v") || partofspeech.equals("prep") || partofspeech.equals("adv")) {
//                            System.out.println(partofspeech);
                            //System.out.println(partofspeech.length());
                        } else {
                            partofspeech = null;
//                            System.out.println(partofspeech);
                        }
                        //无音标有词性多解
                        if (partofspeech != null) {

                            if (line.contains(" 1 ")) {
                                beginindex3 = line.indexOf(" 1 ");
                                if (line.substring(beginindex3).contains(" 2 ")) {
                                    endindex3 = line.indexOf(" 2 ", beginindex3);
                                    // System.out.println(line.substring(beginindex3, endindex3));
                                    exList.add(line.substring(beginindex3, endindex3));
                                    beginindex3 = endindex3;
                                    if (line.substring(beginindex3).contains(" 3 ")) {
                                        endindex3 = line.indexOf(" 3 ", beginindex3);
                                        //System.out.println(line.substring(beginindex3, endindex3));
                                        exList.add(line.substring(beginindex3, endindex3));
                                        beginindex3 = endindex3;
                                        if (line.substring(beginindex3).contains(" 4 ")) {
                                            endindex3 = line.indexOf(" 4 ", beginindex3);
                                            //System.out.println(line.substring(beginindex3, endindex3));
                                            exList.add(line.substring(beginindex3, endindex3));
                                            beginindex3 = endindex3;
                                            if (line.substring(beginindex3).contains(" 5 ")) {
                                                endindex3 = line.indexOf(" 5 ", beginindex3);
                                                //System.out.println(line.substring(beginindex3, endindex3));
                                                exList.add(line.substring(beginindex3, endindex3));
                                                beginindex3 = endindex3;
                                                if (line.substring(beginindex3).contains(" 6 ")) {
                                                    endindex3 = line.indexOf(" 6 ", beginindex3);
                                                    // System.out.println(line.substring(beginindex3, endindex3));
                                                    exList.add(line.substring(beginindex3, endindex3));
                                                    beginindex3 = endindex3;
                                                    if (line.substring(beginindex3).contains(" 7 ")) {
                                                        endindex3 = line.indexOf(" 7 ", beginindex3);
                                                        // System.out.println(line.substring(beginindex3, endindex3));
                                                        exList.add(line.substring(beginindex3, endindex3));
                                                        beginindex3 = endindex3;
                                                    } else {
                                                        endindex3 = line.length();
                                                        //System.out.println(line.substring(beginindex3, endindex3));
                                                        exList.add(line.substring(beginindex3, endindex3));
                                                    }
                                                } else {
                                                    endindex3 = line.length();
                                                    //System.out.println(line.substring(beginindex3, endindex3));
                                                    exList.add(line.substring(beginindex3, endindex3));
                                                }
                                            } else {
                                                endindex3 = line.length();
                                                // System.out.println(line.substring(beginindex3, endindex3));
                                                exList.add(line.substring(beginindex3, endindex3));
                                            }
                                        } else {
                                            endindex3 = line.length();
                                            //System.out.println(line.substring(beginindex3, endindex3));
                                            exList.add(line.substring(beginindex3, endindex3));
                                        }

                                    } else {
                                        endindex3 = line.length();
                                        //System.out.println(line.substring(beginindex3, endindex3));
                                        exList.add(line.substring(beginindex3, endindex3));
                                    }

                                } else {
                                    endindex3 = line.length();
                                    // System.out.println(line.substring(beginindex3, endindex3));
                                    exList.add(line.substring(beginindex3, endindex3));
                                }
                            } //无音标有词性一解
                            else {
                                beginindex3 = endindex2 + 1;
                                endindex3 = line.length();
                                //System.out.println(line.substring(beginindex3, endindex3));
                                exList.add(line.substring(beginindex3, endindex3));
                            }
                        } //此为没有词性的情况
                        else {
                            //无音标无词性多解

                            if (line.contains(" 1 ")) {
                                beginindex3 = line.indexOf(" 1 ");
                                if (line.substring(beginindex3).contains(" 2 ")) {
                                    endindex3 = line.indexOf(" 2 ", beginindex3);
                                    // System.out.println(line.substring(beginindex3, endindex3));
                                    exList.add(line.substring(beginindex3, endindex3));
                                    beginindex3 = endindex3;
                                    if (line.substring(beginindex3).contains(" 3 ")) {
                                        endindex3 = line.indexOf(" 3 ", beginindex3);
                                        //System.out.println(line.substring(beginindex3, endindex3));
                                        exList.add(line.substring(beginindex3, endindex3));
                                        beginindex3 = endindex3;
                                        if (line.substring(beginindex3).contains(" 4 ")) {
                                            endindex3 = line.indexOf(" 4 ", beginindex3);
                                            //System.out.println(line.substring(beginindex3, endindex3));
                                            exList.add(line.substring(beginindex3, endindex3));
                                            beginindex3 = endindex3;
                                            if (line.substring(beginindex3).contains(" 5 ")) {
                                                endindex3 = line.indexOf(" 5 ", beginindex3);
                                                //System.out.println(line.substring(beginindex3, endindex3));
                                                exList.add(line.substring(beginindex3, endindex3));
                                                beginindex3 = endindex3;
                                                if (line.substring(beginindex3).contains(" 6 ")) {
                                                    endindex3 = line.indexOf(" 6 ", beginindex3);
                                                    // System.out.println(line.substring(beginindex3, endindex3));
                                                    exList.add(line.substring(beginindex3, endindex3));
                                                    beginindex3 = endindex3;
                                                    if (line.substring(beginindex3).contains(" 7 ")) {
                                                        endindex3 = line.indexOf(" 7 ", beginindex3);
                                                        // System.out.println(line.substring(beginindex3, endindex3));
                                                        exList.add(line.substring(beginindex3, endindex3));
                                                        beginindex3 = endindex3;
                                                    } else {
                                                        endindex3 = line.length();
                                                        // System.out.println(line.substring(beginindex3, endindex3));
                                                        exList.add(line.substring(beginindex3, endindex3));
                                                    }
                                                } else {
                                                    endindex3 = line.length();
                                                    //System.out.println(line.substring(beginindex3, endindex3));
                                                    exList.add(line.substring(beginindex3, endindex3));
                                                }
                                            } else {
                                                endindex3 = line.length();
                                                // System.out.println(line.substring(beginindex3, endindex3));
                                                exList.add(line.substring(beginindex3, endindex3));
                                            }
                                        } else {
                                            endindex3 = line.length();
                                            //System.out.println(line.substring(beginindex3, endindex3));
                                            exList.add(line.substring(beginindex3, endindex3));
                                        }

                                    } else {
                                        endindex3 = line.length();
                                        // System.out.println(line.substring(beginindex3, endindex3));
                                        exList.add(line.substring(beginindex3, endindex3));
                                    }

                                } else {
                                    endindex3 = line.length();
                                    //System.out.println(line.substring(beginindex3, endindex3));
                                    exList.add(line.substring(beginindex3, endindex3));
                                }
                            } //无音标无词性一解
                            else {
                                beginindex3 = 2;
                                endindex3 = line.length();
                                //System.out.println(line.substring(beginindex3, endindex3));
                                exList.add(line.substring(beginindex3, endindex3));

                            }
                        }

                    }

                }
                for (String s : exList) {
                    //System.out.println(s);
                    beginindex3 = 1;
                    endindex3 = beginindex3;
                    while (endindex3 < s.length()) {
                        if (s.charAt(endindex3) >= '\u4e00' && s.charAt(endindex3) <= '\u9fbf') {
                            //endindex3--;
                            break;
                        }
                        if (s.charAt(endindex3) == '(') {
                            endindex3 = s.indexOf(")", endindex3 + 1);
                        }
                        if (s.charAt(endindex3) == '[') {
                            endindex3 = s.indexOf("]", endindex3 + 1) + 1;
                        } else {
                            endindex3++;
                        }

                    }
                    //System.out.println(s.charAt(endindex3));
                    englishexpression = s.substring(beginindex3, endindex3);
//                    System.out.println(englishexpression);
                    beginindex4 = endindex3;
                    endindex4 = s.indexOf(":", beginindex4 + 1);
                    if (endindex4 == -1) {
                        endindex4 = s.length();
                    }
                    chinese = s.substring(beginindex4, endindex4);
//                    System.out.println(chinese);
                    if (endindex4 != -1) {
                        beginindex5 = endindex4;
                        endindex5 = beginindex5;
                        while (endindex5 < s.length()) {
                            if (s.charAt(endindex5) >= '\u4e00' && s.charAt(endindex5) <= '\u9fbf') {
                                //endindex5--;
                                break;
                            }
                            if (s.charAt(endindex5) == '(') {
                                endindex5 = s.indexOf(")", endindex5 + 1);
                            }
                            if (s.charAt(endindex5) == '[') {
                                endindex5 = s.indexOf("]", endindex5 + 1) + 1;
                            } else {
                                endindex5++;
                            }
                        }
                        example = s.substring(beginindex5, endindex5);
                        if (example.startsWith(":")) {
                            beginindex5 = beginindex5 + 1;
                            example = s.substring(beginindex5, endindex5);
                        }
//                        System.out.println(example);
                    } else {
                        example = null;
//                        System.out.println(example);
                    }
                    //  ed=new Knowledge();
                    ed.setC1(chinese);
                    ed.setName(english);
                    ed.setC2(example);
                    ed.setDetails(englishexpression);
                    ed.setC3(pronunication);
                    if (partofspeechs == null || partofspeechs.isEmpty()) {
                        partofspeechs = applicationPartofSpeechController.getFacade().getQueryResultList("select * from partofspeech");
                    }

                    for (Partofspeech p : partofspeechs) {
                        if (null == partofspeech) {
                            ed.setC4(applicationPartofSpeechController.getPartofspeech(6));
                            break;
                        }
                        if (partofspeech != null) {
                            if (partofspeech.equals(p.getName())) {
                                pf = p;
                                ed.setC4(pf);
                                break;
                            }
                        }
                    }

                    applicationKnowledgeController.getFacade().create(ed);
                }
                //sgetCon();

                // Chinesedictionary cd = new Chinesedictionary();
                //List<Partofspeech> partofspeechs = new ArrayList<>();
//               cd.setChinese(chinese);
//               cd.setEnglish(english);
//                cd.setExample(example);
//                cd.setPronunciation(pronunication);
//
//                //partofspeechs = applicationPartofSpeechController.getFacade().getQueryResultList("select * from partofspeech");
//                for (Partofspeech p : partofspeechs) {
//                    if (partofspeech != null) {
//                        if (partofspeech.equals(p.getName())) {
//                            pf = p;
//                            break;
//                        }
//                    }
//
//                }
//                cd.setPartofspeech(pf);
//                appChinesedictionaryController.create(cd);
                exList.clear();
                i++;
            }

        } catch (IOException e) {
        }
        //System.exit(0);

    }
//write english explain into txt file

    public void count() {
        String pathname = "C:\\Users\\cmx\\Desktop\\1.txt";
        int begin;
        int end;
        String key;
        String value;
        String result;
        int l = 0;
        int i;
        int j;
        int n;
        String s;
        List<Knowledge> knowledges = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathname), "utf-8"));
            String line;
            while ((line = br.readLine()) != null && l < 60000) {
                begin = 1;
                end = line.lastIndexOf(",");
                i = begin;
                while (i < end) {
                    if (line.charAt(i) >= 'A' && line.charAt(i) <= 'z') {
                        break;
                    } else {
                        i++;
                    }
                }
                j = i + 1;
                while (j < end) {
                    if (line.charAt(j) >= 'A' && line.charAt(j) <= 'z') {
                        j++;
                    } else if (line.charAt(j) == '-') {
                        j++;
                    } else if (line.charAt(j) == '\'') {
                        j++;
                    } else {
                        break;
                    }
                }
                key = line.substring(i, j);
                value = line.substring(end, line.length());
                // System.out.println(key);
                // System.out.println(value);
                //System.out.println(key);
                if (knowledges == null || knowledges.isEmpty()) {
                    knowledges = applicationKnowledgeController.getFacade().getQueryResultList("select * from knowledge");

                }
                for (Knowledge k : knowledges) {
                    n = k.getName().lastIndexOf(" ") + 1;
                    s = k.getName().substring(n, k.getName().length());
                    if (s.equals(key)) {
                        result = "(" + key + value;
                        // System.out.println(result);
                        String filename = "C:\\Users\\cmx\\Desktop\\2.txt";
                        File file = new File(filename);
                        PrintWriter pw = null;
                        BufferedWriter bw = null;
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                                pw = new PrintWriter(file);
                                pw.print(result);
                            } catch (Exception ex) {
                            }

                        } else {
                            try {

                                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                                bw.write(result);
                                bw.flush();
                                bw.close();

                            } catch (Exception ex) {
                            }
                        }
                        break;
                    }

                }

                l++;
            }

        } catch (IOException e) {
        }

    }

    public Map<String, String> readTxtFile() {
        Map<String, String> map = new HashMap<>();

        String filePath = "C:\\Users\\cmx\\Desktop\\2.txt";
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] split = lineTxt.split(",");
                    map.put(split[0], split[1]);

                }

                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (IOException e) {
            System.out.println("读取文件内容出错");
        }
        List<Entry<String, String>> lists = new ArrayList<Entry<String, String>>(map.entrySet());
        Collections.sort(lists, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                int value1 = Integer.parseInt(o1.getValue());
                int value2 = Integer.parseInt(o2.getValue());
                int p = value2 - value1;
                if (p > 0) {
                    return 1;
                } else if (p == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
//        for (Map.Entry<String, String> set : lists) {
//            System.out.println(set.getKey() + " " + set.getValue());
//        }
        List<String> a = new ArrayList<String>();
        int k;
        int i, j;
        for (k = 0; k < lists.size(); k++) {
            a.add(lists.get(k).getValue());
        }
        // System.out.println(a);
        Set set = new HashSet();
        List newList = new ArrayList();
        for (String cd : a) {
            if (set.add(cd)) {
                newList.add(cd);
            }
        }
        //System.out.println("去重后的集合： " + newList);
        List list = new ArrayList();
        for (i = 0; i < newList.size(); i++) {
            for (j = 0; j < lists.size(); j++) {
                if (lists.get(j).getValue().equals(newList.get(i))) {
                    list.add(lists.get(j).getKey());
                }

            }

            //temp = stemp;
        }

        //System.out.println("只有单词的集合");
        System.out.println(list);
        return map;

    }
//Update edge among knowledge

    public String knowledgesUpload() {
        File f = new File("C:\\Users\\cmx\\Desktop\\list.xls");
        List<Knowledge> eds = new ArrayList<>();
        //List<NewOldID> newoldIds = new ArrayList<>();
        List<Knowledge> lk = new ArrayList<>();
        Edgeamongknowledge edgeamongknowledge = new Edgeamongknowledge();
        int j;
        try {
            Workbook book = Workbook.getWorkbook(f);// 
            Sheet sheet = book.getSheet(0); //获得第一个工作表对象
            String s = new String();
            String c = new String();
            // System.out.println(sheet.getCell(0, 0).getContents());
            for (int i = 0; i < sheet.getRows(); i++) {
                Knowledge createdNewId = null;
                s = "　　" + sheet.getCell(0, i).getContents().trim();
                eds = applicationKnowledgeController.getFacade().getQueryResultList("select * from knowledge where name='" + s + "'");
                s = c;
                if (!eds.isEmpty() && eds != null) {
                    createdNewId = eds.get(0);
                    lk.add(createdNewId);

                }

            }
            System.out.println(lk);
            for (j = 0; j < lk.size(); j++) {
                if (j == 0) {
                    Knowledge startKnowledge = applicationKnowledgeController.getKnowledge(70703);
                    edgeamongknowledge.setPredecessornode(startKnowledge);
                    edgeamongknowledge.setSuccessornode(lk.get(j + 1));
                } else if (j == lk.size() - 1) {
                    Knowledge endKnowledge = applicationKnowledgeController.getKnowledge(70803);
                    edgeamongknowledge.setPredecessornode(lk.get(j - 1));
                    edgeamongknowledge.setSuccessornode(endKnowledge);
                } else {
                    edgeamongknowledge.setPredecessornode(lk.get(j - 1));
                    edgeamongknowledge.setSuccessornode(lk.get(j + 1));
                }
                if (applicationEdgeamongknowledgeController.getFacade().getQueryResultList("select * from EDGEAMONGKNOWLEDGE where successornode=" + edgeamongknowledge.getSuccessornode().getId() + " and predecessornode=" + edgeamongknowledge.getPredecessornode().getId()).isEmpty()) {
                    applicationEdgeamongknowledgeController.create(edgeamongknowledge);
                }
            }

        } catch (BiffException e) {
            // TODO Auto-generated catch block

        } catch (IOException e) {
            // TODO Auto-generated catch block

        }
        return null;
    }
//Generate single blank question
    public void beingProcess(){
    }
    public void writeQuestion() {
        List<Knowledge> knowledges = new ArrayList<>();
        knowledges = applicationKnowledgeController.getFacade().getQueryResultList("select * from knowledge");
        Question question = new Question();
        int n;
        String name = new String();
        for (Knowledge k : knowledges) {
            name = k.getName();
            name = k.getName().replaceAll("[　*| *| *|//s*]*", "");
            if (k.getC2() != null) {
                n = k.getC2().indexOf(name);
                if (n != -1) {
                    String s = k.getC2().replace(name, "____");
                    System.out.println(s);
                    question.setAnswer(name);
                    question.setKnowledgeId(k);
                    question.setValueinfo(s);
                    question.setScore(2);
                    question.setType(2);
                    question.setDegree(2);
                    question.setAnalysis(k.getC1());
                    applicationQuestionController.create(question);
                }
            }

        }
    }

}
