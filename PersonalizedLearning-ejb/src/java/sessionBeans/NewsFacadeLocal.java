package sessionBeans;

import entities.News;
import entities.Roleinfo;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface NewsFacadeLocal {

    void create(News news);

    void edit(News news);

    void remove(News news);

    News find(Object id);

    List<News> findAll();

    List<News> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    List<News> getNews4Roleinfo(Roleinfo roleinfo);

     List<News> getNews4Teacher(TeacherAdmin teacher);
     
     News findByName(String newsTitle);
}