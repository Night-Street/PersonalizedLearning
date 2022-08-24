package sessionBeans;

import entities.Resourceinfo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface ResourceinfoFacadeLocal {

    void create(Resourceinfo resourceinfo);

    void edit(Resourceinfo resourceinfo);

    void remove(Resourceinfo resourceinfo);

    Resourceinfo find(Object id);

    List<Resourceinfo> findAll();

    List<Resourceinfo> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    Resourceinfo findByName(String name);
}
