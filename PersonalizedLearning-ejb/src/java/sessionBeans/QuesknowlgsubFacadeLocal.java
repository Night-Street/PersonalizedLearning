package sessionBeans;

import entities.Quesknowlgsub;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author haogs
 */
@Local
public interface QuesknowlgsubFacadeLocal {

    void create(Quesknowlgsub quesknowlgsub);

    void edit(Quesknowlgsub quesknowlgsub);

    void remove(Quesknowlgsub quesknowlgsub);

    Quesknowlgsub find(Object id);

    List<Quesknowlgsub> findAll();

    List<Quesknowlgsub> findRange(int[] range);

    int count();
    
     List getQueryResultList(String sql);
}
