
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
@Named
@SessionScoped
public class Apples implements java.io.Serializable{
    private HashMap<String, String> apples=new HashMap<>();
    private List<String> selecteApple;

    /**
     * @return the apples
     */
    public HashMap<String, String> getApples() {
        if(apples.isEmpty()){
            for (int i = 0; i < 10; i++) {
                apples.put("apple"+i,String.valueOf(i));
            }
        }
        return apples;
    }

    /**
     * @param apples the apples to set
     */
    public void setApples(HashMap<String, String> apples) {
        this.apples = apples;
    }

    /**
     * @return the selecteApple
     */
    public List<String> getSelecteApple() {
        return selecteApple;
    }

    /**
     * @param selecteApple the selecteApple to set
     */
    public void setSelecteApple(List<String> selecteApple) {
        this.selecteApple = selecteApple;
    }
    
}
