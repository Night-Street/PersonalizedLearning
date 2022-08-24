package modelController.applicationController;

import entities.News;
import entities.Roleinfo;
import entities.TeacherAdmin;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import sessionBeans.NewsFacadeLocal;
import tools.StaticFields;

@Named("newsControllerA")
@ApplicationScoped
public class NewsController extends ApplicationCommonController {

    @EJB
    private sessionBeans.NewsFacadeLocal ejbFacadelocal;
    private List<News> newsList = null;
    private Set<News> newsesAllList = null;

    public NewsController() {
    }

    private NewsFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public News getLearningresource(java.lang.Integer id) {
        return ejbFacadelocal.find(id);
    }

    public List<News> getNewsList() {
        if (null == newsList || newsList.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, StaticFields.DAY_NEWS);
            this.newsList = getFacade().getQueryResultList("select * from news where inputdate<DATE('"
                    + calendar.get(Calendar.YEAR) + "-"
                    + calendar.get(Calendar.MONTH) + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH)
                    + "')order by inputedate");
        }
        return newsList;
    }

    public List<News> getNews4Roleinfo(Roleinfo roleinfo) {
        setDataChanged(false);
        return getFacade().getNews4Roleinfo(roleinfo);
    }

    public List<News> getNews4Teacher(TeacherAdmin teacher) {
        setDataChanged(false);
        return getFacade().getNews4Teacher(teacher);
    }

    public News getNews(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacadelocal.find(id);
    }

    public void create(News news) {
        getFacade().create(news);
        setDataChanged(true);
    }

    public void remove(News news) {
        getFacade().remove(news);
        setDataChanged(true);
    }

    public void edit(News news) {
        getFacade().edit(news);
        setDataChanged(true);
    }

    public List<News> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<News> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public News find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public News findByName(String newsTitle) {
        return getFacade().findByName(newsTitle);
    }

    @FacesConverter(forClass = News.class)
    public static class NewsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            NewsController controller = (NewsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "newsControllerA");
            return controller.getNews(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof News) {
                News o = (News) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + News.class.getName());
            }
        }
    }
}
