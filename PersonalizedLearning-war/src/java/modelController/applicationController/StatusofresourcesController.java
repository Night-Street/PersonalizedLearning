package modelController.applicationController;

import entities.Statusofresources;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import sessionBeans.StatusofresourcesFacadeLocal;

/**
 *
 * @author hgs
 */
@Named("statusofresourcesControllerA")
@ApplicationScoped
public class StatusofresourcesController extends ApplicationCommonController {

    @EJB
    StatusofresourcesFacadeLocal ejbFacadelocal;
    List<Statusofresources> statusofresourceseList;

    public List<Statusofresources> getStatusofresourceList() {
        if (null == statusofresourceseList || statusofresourceseList.isEmpty()) {
            statusofresourceseList = getFacade().findAll();
        }
        return statusofresourceseList;
    }

    private StatusofresourcesFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    //------------------------Converter-----------------------
    @FacesConverter(forClass = Statusofresources.class)
    public static class StatusofresourcesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StatusofresourcesController controller = (StatusofresourcesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "statusofresourcesControllerA");
            return controller.getStatusofresources(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Statusofresources) {
                Statusofresources o = (Statusofresources) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Statusofresources.class.getName());
            }
        }
    }

 
    public Statusofresources getStatusofresources(String name) {
        for (Statusofresources sf : getStatusofresourceList()) {
            if(sf.getMeaning().equals(name)){
                return sf;
            }
        }
        return null;
    }
    public Statusofresources getStatusofresources(Integer id) {
        for (Statusofresources sf : getStatusofresourceList()) {
            if(sf.getId().equals(id)){
                return sf;
            }
        }
        return null;
    }
    public Statusofresources getReexaming(){
        return getStatus(0);
    }
    public Statusofresources getPassed(){
        return getStatus(1);
    }
    
      public Statusofresources getPassedFaild(){
        return getStatus(2);
    }
      public Statusofresources getSaved(){
          return getStatus(3);
      }
      
      private  Statusofresources getStatus(int id){
        Statusofresources result=null;
        for(Statusofresources statusofresource:getStatusofresourceList()) {
            if(statusofresource.getId()==id){
                result=statusofresource;
                break;
            }
        }
        return result;
    }
}
