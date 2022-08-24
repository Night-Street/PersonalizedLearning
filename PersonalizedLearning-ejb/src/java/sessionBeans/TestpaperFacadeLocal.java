package sessionBeans;

import entities.School;
import entities.Subject;
import entities.Testpaper;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface TestpaperFacadeLocal {

    void create(Testpaper testpaper);

    void edit(Testpaper testpaper);

    void remove(Testpaper testpaper);

    Testpaper find(Object id);

    List<Testpaper> findAll();

    List<Testpaper> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    public List<Testpaper> getTestpaper4School(School school);

    public List<Testpaper> getTestpaper4Subject(Subject subject); 
}
