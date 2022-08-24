package modelController.viewerController;

import entities.News;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.sessionController.RoleinfoController;
import sessionBeans.NewsFacadeLocal;
import tools.StaticFields;

@Named
@SessionScoped
public class StuNewsBean implements Serializable {

    @EJB
    private NewsFacadeLocal facade;
    @Inject
    private modelController.applicationController.NewsController applicationNewsController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    private News news = new News();
    private List<News> recentNews;
    private News directNews;

    public String directToNews() {
        String newsId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(StaticFields.REQUEST_NEWS_ID);
        if (null == newsId) {
            userMessagor.addMessage(
                    commonSession.getResourceBound().getString("No")
                    + commonSession.getResourceBound().getString("News")
            );
        } else {
            directNews = applicationNewsController.find(Integer.valueOf(newsId));
        }
        return "news.xhtml";
    }

    /**
     * @return the news
     */
    public News getNews() {
        return news;
    }

    /**
     * @param news the news to set
     */
    public void setNews(News news) {
        this.news = news;
    }

    /**
     * @return the directNews
     */
    public News getDirectNews() {
        return directNews;
    }

    /**
     * @param directNews the directNews to set
     */
    public void setDirectNews(News directNews) {
        this.directNews = directNews;
    }

    /**
     * @return the recentNews
     */
    public List<News> getRecentNews() {
        if (null == recentNews) {
            int begin = (StaticFields.STU_NEWS_COUNT - StaticFields.NUM_NEWS_SHOWN) < 0 ? 0 : (StaticFields.STU_NEWS_COUNT - StaticFields.NUM_NEWS_SHOWN);
            recentNews = getFacade().getQueryResultList("select * from news where for_role=" + applicationRoleinfoController.getStudentRoleinfo().getId() + " OFFSET " + begin + " ROWS");
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
