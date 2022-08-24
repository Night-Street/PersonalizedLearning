package sessionBeans;

import entities.Roleinfo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface RoleinfoFacadeLocal {

    void create(Roleinfo roleinfo);

    void edit(Roleinfo roleinfo);

    void remove(Roleinfo roleinfo);

    Roleinfo find(Object id);

    List<Roleinfo> findAll();

    List<Roleinfo> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    Roleinfo findByName(String name);
}
