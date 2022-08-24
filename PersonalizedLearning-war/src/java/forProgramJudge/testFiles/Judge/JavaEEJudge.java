/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forProgramJudge.testFiles.Judge;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import forProgramJudge.testFiles.tools.FileTree;
import forProgramJudge.testFiles.tools.Tools;

/**
 *
 * @author haogs
 */
public class JavaEEJudge implements Judge {

    Tools tools = new Tools();

    @Override
    public void judge(File file0, String stuNo, List tableNames, List sbscore, StringBuilder sbmemo) {
        /*
        o	查找"score学号ManagerEE"的文件夹（以下简称EE文件夹）。 (找到+5分) 
o	在EE目录下，查找“-ejb”文件夹，再查找“entities”文件夹，在其中查找3个与表名相同的实体类（大小写可以不同）。(找到+5分) 
o	在这3个实体类文件中的subject和student两个类中，查找toString()方法，查找return this.name（找到+2分）。
o	在EE目录下，查找“-ejb”文件夹，再查找entitiesControllers文件夹，进一步查找“Score学号hiFacade.java”和“Score学号hiFacadeLocal.java”两个文件。(找到+5分) 
o	在EE目录下，查找“-ejb”文件夹，再查找glassfish-resources.xml文件，读取其中内容，找到“jndi-name="jdbc/s学号"”（找到+2分），在同一文件中，继续查找“pool-name="s学号pool"”（找到+3分）。 
o	在EE目录下，查找“-war”文件夹，在web目录下获得所有的以“.xhmtl”结尾的文件，遍历这些文件，从中读取其中的内容。读取“<h:selectOneMenu”、“value="#{student学号deController.selected}"”、“<f:selectItems value="#{student学号deController.%method%”的字符串，（找到+2分），读取“Student学号deController. %method%”的方法，在方法中查找“ejbFacade.findAll();” （找到+2分）。
o	在%targetFile%中查找“<h:commandButton”根据其后的“<h:outputLabel”，在其后的5行内的语句中查找value="#{*Controller.%method%}"，（找到+2分）据此找到对应的%controller%及其%method%，打开%controller%.java文件，找到%method%（找到+2分）

         */
//
//在EE目录下，查找“-war”文件夹，在web目录下获得所有的以“.xhmtl”结尾的文件，找到其中size最大的文件%targetFile%，读取其中的内容。读取“<h:selectOneMenu”、“value="#{student学号deController.selected}"”、“<f:selectItems value="#{student学号deController.%method%”的字符串，（找到+2分），读取“Student学号deController. %method%”的方法，在方法中查找“ejbFacade.findAll();” （找到+2分）。
//在%targetFile%中查找“<h:commandButton”根据其后的“<h:outputLabel”，在其后的5行内的语句中查找value="#{*Controller.%method%}"，（找到+2分）据此找到对应的%controller%及其%method%，打开%controller%.java文件，找到%method%（找到+2分）
        //查找"score学号ManagerEE"的文件夹（以下简称EE文件夹）。 (找到+5分) 
        int s=22;
        sbscore.set(s++,"5\t");
        
        //在EE目录下，查找“-ejb”文件夹，再查找“entities”文件夹，在其中查找3个与表名相同的实体类（大小写可以不同）。(找到+5分) 
        //在这3个实体类文件中的subject和student两个类中，查找toString()方法，查找return this.name（找到+2分）。
        //在EE目录下，查找“-ejb”文件夹，再查找entitiesControllers文件夹
        //进一步查找“Score学号hiFacade.java”和“Score学号hiFacadeLocal.java”两个文件。(找到+5分) 
        for (int i = 0; i < 2; i++) {//这个地方总分13分，包括entity的：3次循环*2分/循环+2次循环*0.5分toString/循环；包括controller的3次循环*2分/循环
            //i==0查找“entities”文件夹
            //i==1查找entitiesControllers文件夹
            File fileEntity = tools.findFile(file0, ((FileTree) tableNames.get(i)).getRootString(), Tools.DIRTYPE);
            if (null != fileEntity) {
                //共3个表：student，subject，score，对应于get第0，1，2个字符串
                for (int j = 0; j < 3; j++) {
                    File file1 = tools.findFile(fileEntity, (String) ((FileTree) tableNames.get(i)).getChildStrings().get(j), Tools.FILETYPE);
                    if (null != file1) {
                        sbscore.set(s++,"2\t");
                        //下拉列表框显示对应表中name的内容,toString()方法改写为
                        if (i == 0) {//entitiesControllers不用处理
                            sbscore.set(s++,tools.getToStringValue(tools.read2ListString(file1)));
                        }
                    } else {
                        if (i != 2) {//score不存在return name是对的
                            sbmemo.append(((FileTree) tableNames.get(i)).getChildStrings().get(j)).append("未找到\t");
                        }
                    }
                }
            }
        }
        //切换到war
        
         file0 = tools.findFile(file0, ((FileTree) tableNames.get(2)).getRootString(), Tools.DIRTYPE);
        String controllerFileName = null, methodName = null;
        File xmlFileName = null;List<String>  xmlFileString=new LinkedList<>();
//在EE目录下，查找“-war”文件夹，在web目录下获得所有的以“.xhmtl”结尾的文件，遍历这些文件，从中读取其中的内容。
//读取“<h:selectOneMenu”、“value="#{student学号deController.selected}"”、“<f:selectItems value="#{student学号deController.%method%”的字符串，（找到+2分），
//读取“Student学号deController. %method%”的方法，在方法中查找“ejbFacade.findAll();” （找到+2分）。
//在%targetFile%中查找“<h:commandButton”根据其后的“<h:outputLabel”，在其后的5行内的语句中查找value="#{*Controller.%method%}"，（找到+2分）据此找到对应的%controller%及其%method%，打开%controller%.java文件，找到%method%（找到+2分）
        //读取xml其中的内容。
        List<File> xmlFiles = (List<File>) (((FileTree) tableNames.get(2)).getChildStrings());
//读取“<h:selectOneMenu”、“value="#{student学号deController.selected}"”（找到+2分）。
        int i = 0;
        boolean found = false;
        for (; i < xmlFiles.size() && !found; i++) {//found为真，意味着找到了对应的xhtml文件，其他xhtml文件就不用再找了
            List<String> stringList = tools.read2ListString(xmlFiles.get(i));
            int j = 0;
            for (; j < stringList.size(); j++) {
                if (stringList.get(j).replaceAll(" ", "").contains("<h:selectOneMenu")) {
                    found = true;
                    if (stringList.get(j).contains("student" + stuNo + "deController.selected")
                            ||stringList.get(j).contains("score" + stuNo + "hiController.selected.studentid")) {
                        sbscore.set(s++,"2\t");
                        xmlFileName = xmlFiles.get(i);
                        xmlFileString=stringList;
                        break;
                    }
                }
            }
            //“<f:selectItems value="#{student学号deController.%method%”的字符串，（找到+2分）
            if (j < stringList.size()) {
                int k = j;
                for (; k < stringList.size(); k++) {
                    String temString = stringList.get(k).replaceAll(" ", "");
                    if (temString.contains("<f:selectItems")&&temString.contains("value=\"#{")) {//有的同学把please select也用成了selectItems
                        sbmemo.append("找到了<f:selectItems\t");
                        sbscore.set(s++,"2\t");
                        //读取“Student学号deController. %method%”的方法，在方法中查找“ejbFacade.findAll();” 
                        int startLocationOfController = temString.indexOf("value=\"#{");
                        if (startLocationOfController > 0) {
                            startLocationOfController = startLocationOfController + "value=\"#{".length();
                        }
                        int endLocationOfController = temString.indexOf("Controller");
                        if (endLocationOfController > 0) {
                            endLocationOfController = endLocationOfController + "Controller".length();
                        }
                        controllerFileName = temString.substring(startLocationOfController, endLocationOfController);
                        break;
                    }
                }
                //读取“Student学号deController. %method%”的方法，在方法中查找“ejbFacade.findAll();” 
                File controllerFilesFolder = tools.findFile(file0, "java", Tools.DIRTYPE);
                if (null != controllerFilesFolder) {
                    File controllerFile = tools.findFile(controllerFilesFolder, controllerFileName + ".java", Tools.FILETYPE);
                    if (null != controllerFile) {
                        List<String> stringList1 = tools.read2ListString(controllerFile);
                        for (String string : stringList1) {
                            if (string.replace(" ", "").contains("findAll()")) {
                                sbmemo.append("找到了findAll()\t");
                                sbscore.set(s++,"2\t");
                                break;
                            }
                        }

                    }
                }
                //在%targetFile%中查找“<h:commandButton”根据其后的“<h:outputLabel”，在其后的5行内的语句中查找value="#{*Controller.%method%}"，（找到+2分）
                //据此找到对应的%controller%及其%method%，打开%controller%.java文件，找到%method%（找到+2分）
                if (null != xmlFileName) {
                    int m = 0;
                    for (; m < xmlFileString.size(); m++) {//找到<h:commandButton
                        if (xmlFileString.get(m).replaceAll(" ", "").indexOf("<h:commandButton") > 0) {
                            break;
                        }
                    }
                    if (m < xmlFileString.size()) {
                        for (; m < xmlFileString.size(); m++) {//找到<h:commandButton
                            if (xmlFileString.get(m).replaceAll(" ", "").indexOf("<h:outputLabel") > 0) {
                                break;
                            }
                        }
                        if (m < xmlFileString.size()) {
                            if (xmlFileString.get(m).replaceAll(" ", "").contains("value=")) {
                                sbscore.set(s++,"6\t");//获取分数的值，即可以写在h:commandButton中，也可以写在这里的value=中，所以，直接给分，不再区分
                                sbmemo.append("找到了h:outputLabel\t");
                            }
                        }
                    }
                }
            }
        }
    }
}
