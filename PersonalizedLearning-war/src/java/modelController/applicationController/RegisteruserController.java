/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelController.applicationController;

import entities.Registeruser;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import sessionBeans.RegisteruserFacadeLocal;

/**
 *
 * @author Administrator
 */
@Named("registeruserControllerA")
@ApplicationScoped
public class RegisteruserController extends ApplicationCommonController {

    @EJB
    private sessionBeans.RegisteruserFacadeLocal ejbFacadelocal;
    private List<Registeruser> allRegisterusers=new LinkedList<>();

    public RegisteruserController() {
    }

    public RegisteruserFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public void create(Registeruser reexamination) {
        getFacade().create(reexamination);
    }

    public void remove(Registeruser reexamination) {
        getFacade().remove(reexamination);
    }

    public void edit(Registeruser reexamination) {
        getFacade().edit(reexamination);
    }

    public List<Registeruser> getQueryResultList(String sql) {
        return getFacade().getQueryResultList(sql);
    }
    
    public int countExisted(String sqlString){
        return getFacade().getQueryResultList(sqlString).size();
    }
    
    public void refresh(){
        this.allRegisterusers.clear();
    }

    //=====================================================
    //------------------------Converter-----------------------
    @FacesConverter(forClass = Registeruser.class)
    public static class RegisteruserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RegisteruserController controller = (RegisteruserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "registeruserControllerA");
            return controller.find(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Registeruser) {
                Registeruser o = (Registeruser) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Registeruser.class.getName());
            }
        }

    }

    public int count() {
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Registeruser> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Registeruser find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public List<Registeruser> getAllList() {
        if(allRegisterusers.isEmpty()){
            allRegisterusers=getFacade().findAll();
        }
        return allRegisterusers;
    }

}
