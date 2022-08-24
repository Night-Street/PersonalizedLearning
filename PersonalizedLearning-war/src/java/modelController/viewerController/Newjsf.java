/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelController.viewerController;

import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author hgs
 */
@Named
@SessionScoped
public class Newjsf implements Serializable {

    private HashMap<String, HashMap<String, String>> classNameAge;

    @PostConstruct
    public void init() {
        classNameAge = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            HashMap zhangsan = new HashMap();
            zhangsan.put("zhangsan" + i, "20" + i);
            classNameAge.put(String.valueOf(i), zhangsan);
        }
    }

    public HashMap<String, HashMap<String, String>> getClassNameAge() {
        return classNameAge;
    }

}
