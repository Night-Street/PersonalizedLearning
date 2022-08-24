/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelController.applicationController;

import entities.Qwquestionwrong;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import sessionBeans.QwquestionwrongFacadeLocal;

/**
 *
 * @author haogs
 */
@Named("QwquestionwrongControllerA")
@ApplicationScoped
public class QwquestionwrongController   extends ApplicationCommonController  {
    @EJB
    private QwquestionwrongFacadeLocal ejbFacade;
    
    public QwquestionwrongController() {
    }

    private QwquestionwrongFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Qwquestionwrong getLogs(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public void create(Qwquestionwrong logs) {
        getFacade().create(logs);
    }

    public void remove(Qwquestionwrong logs) {
        getFacade().remove(logs);
    }

    public void edit(Qwquestionwrong logs) {
        getFacade().edit(logs);
    }
   
    @FacesConverter(forClass = Qwquestionwrong.class)
    public static class LogsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LogsController controller = (LogsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "logsControllerA");
            return controller.getLogs(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Qwquestionwrong) {
                Qwquestionwrong o = (Qwquestionwrong) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Qwquestionwrong.class.getName());
            }
        }

    }

    public int count() {
        return getFacade().count();
    }   

    public List<Qwquestionwrong> findRange(int[] range) {
        return getFacade().findRange(range);
    }
    public Qwquestionwrong find(Integer id) {
        return getFacade().find(id);
    }
    public List<Qwquestionwrong> getQueryResultList(String sqlString){
        return getFacade().getQueryResultList(sqlString);
    }
}
