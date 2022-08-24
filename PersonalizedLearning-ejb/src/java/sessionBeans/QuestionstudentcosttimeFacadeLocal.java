package sessionBeans;

import entities.Questionstudentcosttime;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author haogs
 */
@Local
public interface QuestionstudentcosttimeFacadeLocal {

    void create(Questionstudentcosttime questionstudentcosttime);

    void edit(Questionstudentcosttime questionstudentcosttime);

    void remove(Questionstudentcosttime questionstudentcosttime);

    Questionstudentcosttime find(Object id);

    List<Questionstudentcosttime> findAll();

    List<Questionstudentcosttime> findRange(int[] range);

    int count();
    
}
