package sessionBeans;

import entities.Major;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface MajorFacadeLocal {

    void create(Major major);

    void edit(Major major);

    void remove(Major major);

    Major find(Object id);

    List<Major> findAll();

    List<Major> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    Major findByName(String name);
}
