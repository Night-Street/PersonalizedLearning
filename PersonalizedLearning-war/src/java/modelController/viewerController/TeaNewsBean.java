package modelController.viewerController;

import entities.News;
import entities.TeacherAdmin;
import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import sessionBeans.NewsFacadeLocal;
import tools.StaticFields;

@Named
@SessionScoped
public class TeaNewsBean implements Serializable {

    @EJB
    private NewsFacadeLocal facade;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private modelController.applicationController.NewsController applicationNewsController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.TeacherAdminController teacherAdminController;
    private News news = new News();
    private List<News> recentNews;
    private LinkedHashMap<String, List<News>> recentNewsMap;
    private News directNews;

    @PostConstruct
    public void init() {
        StaticFields.TEA_NEWS_COUNT = applicationRoleinfoController.getTeacherRoleinfo().getNewsSet().size();
        StaticFields.STU_NEWS_COUNT = applicationRoleinfoController.getStudentRoleinfo().getNewsSet().size();
    }

    public String directToNews() {
        String newsId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(StaticFields.REQUEST_NEWS_ID);
        directNews = applicationNewsController.find(Integer.valueOf(newsId));
        return "news.xhtml";
    }

    public String addNews() {
        TeacherAdmin myUser =teacherAdminController.getLogined();
        //TeacherAdmin) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute(StaticFields.TEACHERREF);
        Calendar temCa = Calendar.getInstance();
        int month = temCa.get(Calendar.MONTH);
        String temMonth = month < 10 ? "0" + month : "" + month;
        String myData = temCa.get(Calendar.YEAR) + "-" + temMonth.trim() + "-" + temCa.get(Calendar.DAY_OF_MONTH);
        if (this.news.getContent().trim().length() >= 0) {
            news.setInputdate(temCa.getTime());
            news.setTeacherno(myUser);
            applicationNewsController.create(news);
            this.news = new News();
            //需要调整Map中的list
        } else {
            userMessagor.addMessage("您需要加入消息的内容，而不能为空");
        }
        return null;
    }

    public String delete(News news) {
        applicationNewsController.remove(news);
        return null;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public News getDirectNews() {
        return directNews;
    }

    public void setDirectNews(News directNews) {
        this.directNews = directNews;
    }

    public List<News> getRecentNews() {
        if (null == recentNews) {
            int begin = Math.max(StaticFields.TEA_NEWS_COUNT - StaticFields.NUM_NEWS_SHOWN, 0);
            recentNews = getFacade().getQueryResultList("select * from news where for_role=" + applicationRoleinfoController.getTeacherRoleinfo().getId() + " OFFSET " + begin + " ROWS");
        }
        return recentNews;
    }

    public void setRecentNews(List<News> recentNews) {
        this.recentNews = recentNews;
    }

    public NewsFacadeLocal getFacade() {
        return facade;
    }
}
