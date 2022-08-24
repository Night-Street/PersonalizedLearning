package forProgramJudge.testFiles;

import forProgramJudge.testFiles.Judge.DatabaseJudge;
import forProgramJudge.testFiles.Judge.JavaApplicationJudge;
import forProgramJudge.testFiles.Judge.JavaEEJudge;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import forProgramJudge.testFiles.tools.FileTree;
import forProgramJudge.testFiles.tools.Tools;

/**
 *
 * @author haogs
 */
public class AutoJudge {

    Tools tools = new Tools();

    public static void main(String[] args) {
        String grade = "17";//哪一级的学生
        String dbExtensionString = "BCD";//数据库名称的结尾字符串
        String[] tableExension = {"EF", "GH", "HJ"};
        String[] tableNameArray = {"student", "subject", "score"};
        AutoJudge autoJudge = new AutoJudge();
        //Prepareation
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException ex) {
        }
        String startFileString = "E:\\javaDB\\javadb";

        File rootDirection = new File(startFileString);
        File[] files = rootDirection.listFiles();
        DatabaseJudge dbj = new DatabaseJudge();
        JavaApplicationJudge javaApplicationJudge = new JavaApplicationJudge();
        JavaEEJudge javaEEJudge = new JavaEEJudge();

        List<String> tablesNames = new LinkedList<>();
        List<FileTree> entityControllerNames = new LinkedList<>();
        String stuNo = null;
        for (File file : files) {
             LinkedList<String> sbscore = new LinkedList<>();
            try {
                for (int i = 0; i < 34; i++) {
                    sbscore.add("0");
                }
                StringBuilder sbmemo = new StringBuilder();
                //Step 1: database judge

                File file0 = autoJudge.tools.findFile(file, "service.properties", Tools.FILETYPE);
                if (null != file0) {
                    file0 = file0.getParentFile();
                    while (!file0.getName().contains(grade)) {
                        file0 = file0.getParentFile();
                    }
                    stuNo = file0.getName().substring(1, 10);
                    sbscore.set(0, stuNo + "\t");
                    if (file0.getName().endsWith(dbExtensionString)) {
                        //按照要求建立的数据库，如果未按要求，则不判卷，不给分数
                        tablesNames.clear();
                        for (int i = 0; i < tableNameArray.length; i++) {
                            tablesNames.add(tableNameArray[i] + stuNo + tableExension[i]);
                        }
                        try {
                            dbj.judge(file0, stuNo, tablesNames, sbscore, sbmemo);
                        } catch (Exception e) {
                            sbmemo.append("数据库判卷过程失败\t");
                        }
                    }
                } else {//数据库不存在，Prepare for JavaApplication
                    file0 = autoJudge.tools.findFile(file, "nbproject", Tools.DIRTYPE);
                    if (null != file0) {
                        file0 = file0.getParentFile().getParentFile();//parent->src--parent-->score学号Manager
                        if (file0.getName().contains("build")) {
                            file0 = file0.getParentFile();
                        }
                        if (file0.getName().contains(grade)) {
                            int start = file0.getName().indexOf(grade);
                            stuNo = file0.getName().substring(start, start + 9);
                        } else {
                            stuNo = file0.getName().substring(5, 14);
                        }
                        //System.out.print(stuNo+"\t");
                        tablesNames.clear();
                        for (int i = 0; i < tableNameArray.length; i++) {
                            tablesNames.add(tableNameArray[i] + stuNo + tableExension[i]);
                        }
                    }
                }

                //Step 2: JavaApplication judge
                if (null != stuNo) {//存在JavaApplication
                    entityControllerNames.clear();
                    file0 = autoJudge.tools.findFile(file, "score" + stuNo + "Manager", Tools.DIRTYPE);
                    if (null == file0) {
                        file0 = autoJudge.tools.findFile(file, "score" + stuNo + "Mangager", Tools.DIRTYPE);//有一个学生Manager写错了
                    }
                    if (null != file0) {
                        //Delete build and dist files
                        autoJudge.tools.deleteBuildDist(file0, "build");
                        autoJudge.tools.deleteBuildDist(file0, "dist");
                        //在包entities中新建3个实体类，要求类名与表名相同
                        List<String> entitiesName = new LinkedList<>();
                        for (String tableName : tablesNames) {
                            entitiesName.add(tableName + ".java");
                        }
                        entityControllerNames.add(new FileTree("entities", entitiesName));
                        //基于上述实体类在包controllers中生成对应的控制器类 
                        List<String> controllersName = new LinkedList<>();
                        for (String tableName : tablesNames) {
                            controllersName.add(tableName + "JpaController.java");
                        }
                        entityControllerNames.add(new FileTree("controllers", controllersName));
                        //viewers中新建一个类，类名为："Frame学号"
                        List<String> frameName = new LinkedList<>();
                        frameName.add("Frame" + stuNo + ".java");
                        entityControllerNames.add(new FileTree("viewers", frameName));
                        try {
                            javaApplicationJudge.judge(file0, stuNo, entityControllerNames, sbscore, sbmemo);
                        } catch (Exception e) {
                            sbmemo.append("Application判卷过程失败\t");
                        }
                    } else {
                        sbmemo.append("score").append(stuNo).append("Manager不存在\t");
                    }
                }
                //Step 3: JavaEE judge
                /*
        o	查找"score学号ManagerEE"的文件夹（以下简称EE文件夹）。 (找到+5分) 
o	在EE目录下，查找“-ejb”文件夹，再查找“entities”文件夹，在其中查找3个与表名相同的实体类（大小写可以不同）。(找到+5分) 
o	在这3个实体类文件中的subject和student两个类中，查找toString()方法，查找return this.name（找到+2分）。
o	在EE目录下，查找“-ejb”文件夹，再查找entitiesControllers文件夹，进一步查找“Score学号hiFacade.java”和“Score学号hiFacadeLocal.java”两个文件。(找到+5分) 
o	在EE目录下，查找“-ejb”文件夹，再查找glassfish-resources.xml文件，读取其中内容，找到“jndi-name="jdbc/s学号"”（找到+2分），在同一文件中，继续查找“pool-name="s学号pool"”（找到+3分）。 
o	在EE目录下，查找“-war”文件夹，在web目录下获得所有的以“.xhmtl”结尾的文件，找到其中size最大的文件%targetFile%，读取其中的内容。读取“<h:selectOneMenu”、“value="#{student学号deController.selected}"”、“<f:selectItems value="#{student学号deController.%method%”的字符串，（找到+2分），读取“Student学号deController. %method%”的方法，在方法中查找“ejbFacade.findAll();” （找到+2分）。
o	在%targetFile%中查找“<h:commandButton”根据其后的“<h:outputLabel”，在其后的5行内的语句中查找value="#{*Controller.%method%}"，（找到+2分）据此找到对应的%controller%及其%method%，打开%controller%.java文件，找到%method%（找到+2分）

                 */
                if (null != stuNo) {//存在JavaEE
                    entityControllerNames.clear();
                    file0 = autoJudge.tools.findFile(file, "score" + stuNo + "ManagerEE", Tools.DIRTYPE);
                    if (null == file0) {
                        file0 = autoJudge.tools.findFile(file, "score" + stuNo + "MangagerEE", Tools.DIRTYPE);//有一个学生Manager写错了
                    }
                    if (null == file0) {
                        file0 = autoJudge.tools.findFile(file, "score" + stuNo + "MangerEE", Tools.DIRTYPE);//又有一个学生Manager写错了
                    }
                    if (null != file0) {//项目存在
                        //在EE目录下，查找“-ejb”文件夹，再查找glassfish-resources.xml文件，读取其中内容，找到“jndi-name="jdbc/s学号"”（找到+2分），在同一文件中，继续查找“pool-name="s学号pool"”（找到+3分）。 
                        //读取glassfish-resources.xml文件中的jndi和poolname，由于该文件位于dist和build目录中，所以预先读出来
                        File glassFishxml = autoJudge.tools.findFile(file0, "glassfish-resources.xml", Tools.FILETYPE);
                        if (null != glassFishxml) {
                            String jndiResult = autoJudge.tools.getJndiPoolNameValue(autoJudge.tools.read2ListString(glassFishxml), stuNo);
                            if (jndiResult.trim().length() == 0) {
                                sbmemo.append("jndi出错\t");
                            }
                            sbscore.add(jndiResult);
                        } else {
                            sbmemo.append("glassfish-resources.xml未找到\t");
                        }
                        //Delete build and dist files
                        autoJudge.tools.deleteBuildDist(file0, "build");
                        autoJudge.tools.deleteBuildDist(file0, "dist");
                        //reload the file to refresh the file
                        //file0 = autoJudge.tools.findFile(file, "score" + stuNo + "ManagerEE", Tools.DIRTYPE);
                        //在包entities中新建3个实体类，要求类名与表名相同
                        List<String> entitiesName = new LinkedList<>();
                        tablesNames.forEach((tableName) -> {
                            entitiesName.add(tableName + ".java");
                        });
                        entityControllerNames.add(new FileTree("entities", entitiesName));
                        //在EE目录下，查找“-ejb”文件夹，再查找entitiesControllers文件夹，进一步查找“Score学号hiFacade.java”和“Score学号hiFacadeLocal.java”两个文件 
                        List<String> controllersName = new LinkedList<>();
                        tablesNames.stream().map((tableName) -> {
                            controllersName.add(tableName + "Facade.java");
                            return tableName;
                        }).forEachOrdered((tableName) -> {
                            controllersName.add(tableName + "FacadeLocal.java");
                        });
                        entityControllerNames.add(new FileTree("entitiesControllers", controllersName));
                        //在EE目录下，查找“-war”文件夹，在web目录下获得所有的以“.xhmtl”结尾的文件
                        File file1 = autoJudge.tools.findFile(file0, "WEB-INF", Tools.DIRTYPE);//resources在web目录下
                        if (null != file1) {
                            List<File> realXmlFiles = new LinkedList<>();
                            autoJudge.tools.getFiles(file1.getParentFile()).stream().filter((file2) -> (file2.getName().endsWith(".xhtml"))).forEachOrdered((file2) -> {
                                realXmlFiles.add(file2);
                            });
                            entityControllerNames.add(new FileTree("score" + stuNo + "ManagerEE-war", realXmlFiles));
                        } else {
                            sbmemo.append("没找到WEB-INF文件\t");
                        }
                        try {
                            javaEEJudge.judge(file0, stuNo, entityControllerNames, sbscore, sbmemo);
                        } catch (Exception e) {
                            sbmemo.append("JavaEE判卷过程失败\t");
                        }

                    } else {
                        sbmemo.append("score").append(stuNo).append("ManagerEE不存在\t");
                    }
                }
            } catch (Exception e) {
                System.out.println(stuNo + "\t故障发生===========");
            }finally{
                float finalScore = 0;
                for (String score : sbscore) {
                    System.out.print(score+" " + "\t");
                    if (score.trim().length()>0 && Float.valueOf(score) < 20) {//学号也在这里边，用这个把学号滤除
                        finalScore += Float.valueOf(score);
                    }
                }
                System.out.println("final:====" + finalScore);
            }
        }

    }

}
