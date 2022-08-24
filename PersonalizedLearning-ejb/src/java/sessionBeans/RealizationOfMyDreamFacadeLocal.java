package sessionBeans;

import entities.Realizationofmydream;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface RealizationOfMyDreamFacadeLocal {

    void create(Realizationofmydream realizationofmydream);

    void edit(Realizationofmydream realizationofmydream);

    void remove(Realizationofmydream realizationofmydream);

    Realizationofmydream find(Object id);

    List<Realizationofmydream> findAll();

    List<Realizationofmydream> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
}
