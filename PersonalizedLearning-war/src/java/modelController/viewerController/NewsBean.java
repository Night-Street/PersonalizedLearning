package modelController.viewerController;

import entities.News;
import java.util.LinkedHashMap;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asus
 */
@Named
@ApplicationScoped
public class NewsBean implements java.io.Serializable {

    @Inject
    private StuNewsBean stuNewsBean;
    @Inject
    private TeaNewsBean teaNewsBean;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private LinkedHashMap<String, List<News>> newsHashMap;

    /**
     * @return the newsHashMap
     */
    public LinkedHashMap<String, List<News>> getNewsHashMap() {
        if (null == newsHashMap) {
            String[] roleStrings = commonSession.getResourceBound().getString("RoleArray").split(",");
            newsHashMap = new LinkedHashMap<>();
            newsHashMap.put(roleStrings[0], stuNewsBean.getRecentNews());
            newsHashMap.put(roleStrings[1], teaNewsBean.getRecentNews());
        }
        return newsHashMap;
    }

    /**
     * @param newsHashMap the newsHashMap to set
     */
    public void setNewsHashMap(LinkedHashMap<String, List<News>> newsHashMap) {
        this.newsHashMap = newsHashMap;
    }

}
