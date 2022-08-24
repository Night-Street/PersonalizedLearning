package sessionBeans;



import entities.Parent;
import entities.Student;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface ParentFacadeLocal {

    void create(Parent parent);

    void edit(Parent parent);

    void remove(Parent parent);

    Parent find(Object id);

    List<Parent> findAll();

    List<Parent> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);
    int executUpdate(String sql);
    
    Parent findByName(String name);
}
