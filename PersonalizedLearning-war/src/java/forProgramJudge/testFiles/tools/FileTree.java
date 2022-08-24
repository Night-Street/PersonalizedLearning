/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forProgramJudge.testFiles.tools;

import java.util.List;

/**
 *
 * @author haogs
 * 该类描述的是：在规定的文件夹（包）rootString中包含规定的文件childStrings
 */
public class FileTree {

    private String rootString;
    private List childStrings;

    public FileTree(String rootString, List childStrings) {
        this.rootString = rootString;
        this.childStrings = childStrings;
    }

    /**
     * @return the rootString
     */
    public String getRootString() {
        return rootString;
    }

    /**
     * @param rootString the rootString to set
     */
    public void setRootString(String rootString) {
        this.rootString = rootString;
    }

    /**
     * @return the childStrings
     */
    public List getChildStrings() {
        return childStrings;
    }

    /**
     * @param childStrings the childStrings to set
     */
    public void setChildStrings(List childStrings) {
        this.childStrings = childStrings;
    }
}
