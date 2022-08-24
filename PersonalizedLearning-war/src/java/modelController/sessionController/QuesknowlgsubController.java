/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelController.sessionController;

import entities.Quesknowlgsub;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author haogs
 */
@Named("quesknowlgsubController")
@SessionScoped
public class QuesknowlgsubController  extends CommonModelController<Quesknowlgsub> implements Serializable {

    public QuesknowlgsubController() {
    }

}
