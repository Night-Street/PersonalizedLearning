package sessionBeans;

import entities.Student;
import entities.Studentschedule;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface StudentscheduleFacadeLocal {

    void create(Studentschedule studentschedule);

    void edit(Studentschedule studentschedule);

    void remove(Studentschedule studentschedule);

    Studentschedule find(Object id);

    List<Studentschedule> findAll();

    List<Studentschedule> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
     public List<Studentschedule> getStudentschedule4Student(Student Student);
}
