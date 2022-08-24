/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forProgramJudge.testFiles.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author haogs
 */
public class Tools {

    public static final int FILETYPE = 0, DIRTYPE = 1;

    /*
    递归函数求取某一路径下的所有非路径的文件
     */
    public List<File> getFiles(File path) {
        List<File> result = new LinkedList<>();
        File[] path1 = path.listFiles();
        for (int i = 0; i < path1.length; i++) {
            if (path1[i].isDirectory()) {
                result.addAll(getFiles(path1[i]));
            } else {
                result.add(path1[i]);
            }
        }
        return result;
    }

    public List<File> getSpecifiedFiles(File path, String name) {
        name = name.toLowerCase();
        List<File> result = new LinkedList<>();
        File[] path1 = path.listFiles();
        for (File path11 : path1) {
            if (path11.getName().toLowerCase().equals(name)) {
                result.add(path11);
            }
            if (path11.isDirectory()) {//continue to find
                result.addAll(getSpecifiedFiles(path11, name));
            }
        }
        return result;
    }


    /*
    以file为起始根目录，在其中查找包含名称fileName的type类型的文件
    type==0表示找具体文件
    type==1表示找文件夹
     */
    public File findFile(File file, String fileName, int type) {
        fileName = fileName.toLowerCase();
        File result = null;
        if (type == FILETYPE && file.isFile()) {//递归type==FILETYPE的结束
            if (file.getName().toLowerCase().endsWith(fileName)) {
                return file;
            }
        }
        if (type == DIRTYPE && file.isDirectory()) {//递归type==DIRTYPE的结束 
            if (file.getName().toLowerCase().endsWith(fileName)) {
                return file;
            }
        }
        if (file.isDirectory() && result == null) {//没找到，还可以继续查找
            HashMap<Integer, List<File>> subFileTypeMap = getsubFileAccording2Type(file);
            if (null != subFileTypeMap) {
                if (null != subFileTypeMap.get(type)) {
                    for (File file1 : subFileTypeMap.get(type)) {
                        result = findFile(file1, fileName, type);
                        if (result != null) {//找到了
                            return result;
                        }
                    }
                }
                if (null == result) {//没找到
                    for (File file1 : subFileTypeMap.get(1 - type)) {
                        result = findFile(file1, fileName, type);
                        if (result != null) {//找到了
                            return result;
                        }
                    }
                }
            }

        }
        return result;
    }

    private HashMap<Integer, List<File>> getsubFileAccording2Type(File file) {
        HashMap<Integer, List<File>> result = new HashMap<>();
        File[] allFiles = file.listFiles();
        List<File> fileList = new LinkedList<>();
        List<File> dirList = new LinkedList<>();
        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].isFile()) {
                fileList.add(allFiles[i]);
            } else {
                dirList.add(allFiles[i]);
            }
        }
        result.put(0, fileList);
        result.put(1, dirList);
        return result;
    }

    public List<String> read2ListString(File file) {
        List<String> stringList = new LinkedList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s;
            while ((s = in.readLine()) != null) {
                stringList.add(s);
            }
            in.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        return stringList;
    }

    public String getToStringValue(List<String> stringList) {//toString()
        String result = "";
        String targetString = "publicStringtoString()".toLowerCase();
        int i = 0;
        for (; i < stringList.size(); i++) {
            if (stringList.get(i).replaceAll(" ", "").toLowerCase().contains(targetString)) {
                break;
            }
        }
        i++;
        if (i < stringList.size() && (stringList.get(i).replaceAll(" ", "").toLowerCase().equals("returngetName();".toLowerCase())
                || stringList.get(i).replaceAll(" ", "").toLowerCase().equals("returnname;".toLowerCase())
                || stringList.get(i).replaceAll(" ", "").toLowerCase().equals("returnthis.name;".toLowerCase()))) {
            result = "0.5\t";
        }
        return result;
    }

    public String getJndiPoolNameValue(List<String> stringList, String stuNo) {
        //读取其中内容，找到“jndi-name="jdbc/s学号"”（找到+2分），
        //在同一文件中，查找“pool-name="s学号pool"”（找到+3分）。
        String result = "";
        String targetString = "jndi-name";
        int i = stringList.size() - 1;
        for (; i >= 0; i--) {//因为jndi-name和poolname在最后一行
            if (stringList.get(i).replaceAll(" ", "").toLowerCase().contains(targetString)) {
                break;
            }
        }
        if (i > 0) {
            String currentString = stringList.get(i).replaceAll(" ", "");
            if (i >= 0) {
                if (currentString.contains(stuNo)) {
                    result = "5\t";
                }
            }
        }
        return result;
    }

    private void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                deleteFile(fileList[i]);
            }
            file.delete();
        }
    }

    public void deleteBuildDist(File file0, String pathName) {
        //Delete files from build and dist
        List<File> buildFiles = getSpecifiedFiles(file0, pathName);
        for (File buildFile : buildFiles) {
            deleteFile(buildFile);
        }
    }
}
