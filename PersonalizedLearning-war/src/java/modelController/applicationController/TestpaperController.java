package modelController.applicationController;

import entities.School;
import entities.Subject;
import entities.Testpaper;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import sessionBeans.TestpaperFacadeLocal;

@Named("testpaperControllerA")
@ApplicationScoped
public class TestpaperController extends ApplicationCommonController {

    @EJB
    private sessionBeans.TestpaperFacadeLocal ejbFacade;
    @Inject
    private modelController.sessionController.CommonSession commonSession;

    private TestpaperFacadeLocal getFacade() {
        return ejbFacade;
    }

    public TestpaperController() {
    }

    public Testpaper getTestpaper(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);
    }

    public void specifiedIntervalValidator(FacesContext ctx, UIComponent comp, Object value) {
        if (0 >= (int) value || (int) value > 500) {
            throw new ValidatorException(new FacesMessage(commonSession.getResourceBound().getString("Interval") + "==0"));
        }
    }

    public void create(Testpaper testpaper) {
        testpaper.setCreateDate(Calendar.getInstance().getTime());
        getFacade().create(testpaper);
        setDataChanged(true);
    }

    public void remove(Testpaper testpaper) {
        getFacade().remove(testpaper);
        setDataChanged(true);
    }

    public void edit(Testpaper testpaper) {
        getFacade().edit(testpaper);
        setDataChanged(true);
    }

    public List<Testpaper> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Testpaper.class)
    public static class TestpaperControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TestpaperController controller = (TestpaperController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "testpaperControllerA");
            return controller.getTestpaper(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Testpaper) {
                Testpaper o = (Testpaper) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Testpaper.class.getName());
            }
        }
    }

    public List<Testpaper> getTestpaper4SchoolSubject(School school, Subject subject, boolean isTestpaper) {
        setDataChanged(false);
        return getFacade().getQueryResultList("select * from testpaper where schoolid=" + school.getId()
                + " and istest=" + isTestpaper
                + " and (locate('" + subject.getId() + ",',subjectids)>0 "
                + "or locate('," + subject.getId() + "',subjectids)>0 "
                + "or ( locate('" + subject.getId() + "',subjectids)>0 "
                + " and locate(',',subjectids)<=0) ) ");
    }

    public List<Testpaper> getTestpaper4School(School school) {
        setDataChanged(false);
        return getFacade().getTestpaper4School(school);
    }

    public List<Testpaper> getTestpaper4Subject(Subject subject) {
        setDataChanged(false);
        return getFacade().getTestpaper4Subject(subject);
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        setDataChanged(true);
        getFacade().executUpdate(updateString);
    }

    public List<Testpaper> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Testpaper find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    //============以下内容为临时为考试做的内容================
    String testpaperString = "";

    public void setTemString(String inputString) {
        testpaperString = inputString;
    }

    public String getTemString() {
        return testpaperString;
    }
}
