package forProgramJudge.testFiles.Judge;

import java.io.File;
import java.io.IOException;
import java.util.List;
import forProgramJudge.testFiles.tools.FileTree;
import forProgramJudge.testFiles.tools.Tools;

/**
 *
 * @author haogs
 */
public class JavaApplicationJudge implements Judge {

    Tools tools = new Tools();

    @Override
    public void judge(File file0, String stuNo, List tableNames, List sbscore, StringBuilder sbmemo) {
        //新建一个名为"score学号Manager"的Java应用程序。(5分) 
        sbscore.set(6, "5\t");
        //利用向导，基于数据库"s学号ABC"在包entities中新建3个实体类，要求类名与表名相同（大小写可以不同）。 (5分)
        //基于上述实体类在包controllers中生成对应的控制器类。(5分) 
        int a = 7;
        for (int i = 0; i < 2; i++) {
            //i==0时处理entities；i==1时处理controllers
            File fileEntity = tools.findFile(file0, ((FileTree) tableNames.get(i)).getRootString(), Tools.DIRTYPE);
            if (null != fileEntity) {
                //共3个表：student，subject，score，对应于get第0，1，2个字符串
                for (int j = 0; j < 3; j++) {
                    File file1 = tools.findFile(fileEntity, (String) ((FileTree) tableNames.get(i)).getChildStrings().get(j), Tools.FILETYPE);
                    if (null != file1) {
                        sbscore.set(a++, "2\t");
                        if (i == 0) {//controllers不用处理toString
                            //下拉列表框显示对应表中name的内容,toString()方法改写为
                            sbscore.set(a++, tools.getToStringValue(tools.read2ListString(file1)));
                             //sbscore.set(a++,"1\t");
                        }
                    } else {
                        if (j != 2 && i != 1) {//score不存在return name是对的
                            sbmemo.append(((FileTree) tableNames.get(i)).getChildStrings().get(j)).append("未找到\t");
                        }
                    }
                }
            }
        }
        //在包viewers中新建一个类，类名为："Frame学号"，该类负责显示窗体，在窗体上至少包含两个下拉列表框，分别供用户选择"student学号DE"中的内容和"subject学号FG"中的内容。 (5分)
        File file1 = tools.findFile(file0, ((FileTree) tableNames.get(2)).getRootString(), Tools.DIRTYPE);
        if (null != file1) {
            File file2 = tools.findFile(file1, (String) ((FileTree) tableNames.get(2)).getChildStrings().get(0), Tools.FILETYPE);
            if (null != file2) {
                //在其中查找字符串JComboBox<Student学号de>和JComboBox<Subject学号fg>及其对应的变量名，找到加2分
                List<String> comboString = tools.read2ListString(file2);
                int j = 0;
                while (j < comboString.size() && !comboString.get(j).contains("JComboBox")) {//import
                    j++;
                }
                if (j < comboString.size()) {//
                    j++;
                    while (j < comboString.size() && !comboString.get(j).contains("JComboBox")) {//实例化
                        j++;
                    }
                }
                if (j < comboString.size()) {
                    sbscore.set(a++, "2\t");
                    int location = comboString.get(j).indexOf("JComboBox");
                    if (location > 0) {
                        String comboObjectName = comboString.get(j).substring(location + ("JComboBox").length()).trim();
                        int endOfComboObjectName = comboObjectName.indexOf("=");//定义对象的同时实例化
                        endOfComboObjectName = endOfComboObjectName < 0 ? comboObjectName.indexOf(";") : endOfComboObjectName;
                        comboObjectName = comboObjectName.substring(0, endOfComboObjectName).trim();
                        sbscore.set(a++, "2\t");
                        //在其中查找JButton及其后边的button的变量名，然后再找对应的.addActionListener(new ActionListener()找到加2分
                        int k = 0;
                        while (!comboString.get(k).contains("JButton")) {//找到import 中的
                            k++;
                        }
                        if (k < comboString.size()) {
                            k++;
                            while (!comboString.get(k).contains("JButton")) {//找到变量命名中的
                                k++;
                            }
                            if (k < comboString.size()) {//找到了
                                int locationk = comboString.get(k).indexOf("JButton");
                                String buttonName = comboString.get(k).substring(locationk + ("JButton").length()).trim();
                                int endofButtonName = buttonName.indexOf("=");//定义对象的同时实例化
                                endofButtonName = endofButtonName < 0 ? buttonName.indexOf(";") : endofButtonName;
                                buttonName = buttonName.substring(0, endofButtonName);
                                while ((k < comboString.size()) && !(comboString.get(k).contains(buttonName + ".addActionListener"))) {
                                    k++;
                                }
                                if (k < comboString.size()) {
                                    sbscore.set(a++, "2\t");
                                    k++;
                                } else {
                                    sbmemo.append("不存在.addActionListener\t");
                                }
                                //在addActionListener(new ActionListener()后查找JComboBox<Student学号de>对应的变量名，如a和b，然后查找其后是否存在方法.getSelectedItem()，存在加1分
                                while ((k < comboString.size()) && !comboString.get(k).contains(comboObjectName)) {
                                    k++;
                                }
                                if (k < comboString.size()) {//找到了
                                    if (comboString.get(k).contains("getSelectedItem")) {
                                        sbscore.set(a++, "2\t");
                                    } else {
                                        sbmemo.append("不存在getSelectedItem\t");
                                    }
                                }
                                try {
                                    Runtime.getRuntime().exec("javac " + file1.getParentFile().getName() + "." + ((FileTree) tableNames.get(2)).getChildStrings().get(0), null,
                                            file1.getParentFile());
                                    sbscore.set(a++, "4\t");
                                } catch (IOException ex) {
                                    sbmemo.append(file1.getParentFile().getName()).append("编译出错\t");
                                }
                            }
                        }
                    }
                } else {
                    sbmemo.append("JComboBox不存在\t");
                }
            }
        }
        //同时，该窗体中还必须包含一个命令按钮和一个标签。当用户点击该命令按钮后，根据前述两个下拉列表框内被选择的值，在该标签上显示对应的"score学号HI"中的score列内容。（10分） 
    }

}
