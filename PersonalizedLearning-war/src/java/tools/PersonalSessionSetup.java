/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.File;
import java.util.Locale;
import java.util.PriorityQueue;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author haogs
 */
@Named
@SessionScoped
public class PersonalSessionSetup implements java.io.Serializable {

    private String filePath;

    @PostConstruct
    public void init() {//For different operating system
        int osType = 0;//1 for mac;2 for windows, 3 for linux
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.contains("mac")) || (OS.contains("darwin"))) {
            osType = 1;
        } else if (OS.contains("win")) {
            // System.out.println("windows");
            osType = 2;
        } else if (OS.contains("nux") || (OS.contains("ubuntu"))) {
            //System.out.println("ubuntu");
            osType = 3;
        }
        filePath = this.getClass().getResource(File.separator).getPath();
        filePath = filePath.substring(0, filePath.lastIndexOf("/"));
        filePath = filePath.substring(0, filePath.lastIndexOf("/"));
        filePath = filePath.substring(0, filePath.lastIndexOf("/"));
        filePath = filePath.substring(0, filePath.lastIndexOf("/"));
        switch (osType) {
            case 1://For mac, Darwin
            case 3://For ubuntu
                //picturePath += "/domain1/applications/PersonalizedLearning/PersonalizedLearning-war_war/resources";
                filePath += "/domain1/applications/fileRepository";

                break;
            case 2://For windows
                filePath = filePath.substring(0, filePath.lastIndexOf("/"));
                filePath = filePath.substring(0, filePath.lastIndexOf("/"));
                filePath = filePath.substring(0, filePath.lastIndexOf("/"));
                filePath = filePath.substring(0, filePath.lastIndexOf("/"));
                filePath += "/domain1/applications/fileRepository";
                break;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    private int fileNmuberUpLimit = 10;
    PriorityQueue<Integer> fileNameQueue = new PriorityQueue<>();

    public String getFileName(String filePath, String preName, String suffix) {
        String result = "";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        int i = 0, j = 0;
        for (; i < fileNmuberUpLimit; i++) {
            if (!fileNameQueue.contains(i)) {
                fileNameQueue.add(i);
                j = i;
                break;
            }
        }
        if (i >= fileNmuberUpLimit) {//Get the head of the queue
            j = fileNameQueue.poll();
            fileNameQueue.add(j);
        }

        File file1 = new File(filePath + "/" + preName + j + "." + suffix);
        if (!file1.exists()) {
            result = file1.getAbsolutePath();
        } else {
            file1.delete();
            result = file1.getAbsolutePath();
        }

        return result;
    }
}
