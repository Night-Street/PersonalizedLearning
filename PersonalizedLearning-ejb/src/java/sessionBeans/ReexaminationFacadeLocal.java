package sessionBeans;



import entities.Reexamination;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface ReexaminationFacadeLocal {

    void create(Reexamination reexamination);

    void edit(Reexamination reexamination);

    void remove(Reexamination reexamination);

    Reexamination find(Object id);

    List<Reexamination> findAll();

    List<Reexamination> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);
    int executUpdate(String sql);
}
