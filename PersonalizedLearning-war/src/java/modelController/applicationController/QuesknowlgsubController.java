/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelController.applicationController;

import sessionBeans.QuesknowlgsubFacadeLocal;
import entities.Quesknowlgsub;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

/**
 *
 * @author haogs
 */
@Named("quesknowlgsubControllerA")
@ApplicationScoped
public class QuesknowlgsubController extends ApplicationCommonController {

    @EJB
    private sessionBeans.QuesknowlgsubFacadeLocal ejbFacade;

    public QuesknowlgsubController() {
    }

    public QuesknowlgsubFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void create(Quesknowlgsub quesknowlgsub) {
        getFacade().create(quesknowlgsub);
    }

    public void edit(Quesknowlgsub quesknowlgsub) {
        getFacade().edit(quesknowlgsub);
        setDataChanged(true);

    }

    public void remove(Quesknowlgsub Praise) {
        getFacade().remove(Praise);
        setDataChanged(true);
    }

    public Quesknowlgsub getPraise(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);
    }

    public List<Quesknowlgsub> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public List<Quesknowlgsub> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Quesknowlgsub find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    @FacesConverter(forClass = Quesknowlgsub.class)
    public static class PraiseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PraiseController controller = (PraiseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "quesknowlgsubController");
            return controller.getPraise(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Quesknowlgsub) {
                Quesknowlgsub o = (Quesknowlgsub) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Quesknowlgsub.class.getName());
            }
        }

    }

}
