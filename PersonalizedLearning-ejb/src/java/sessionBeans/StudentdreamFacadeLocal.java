package sessionBeans;

import entities.Studentdream;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface StudentdreamFacadeLocal {

    void create(Studentdream student);

    void edit(Studentdream student);

    void remove(Studentdream student);

    Studentdream find(Object id);

    List<Studentdream> findAll();

    List<Studentdream> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
}
