package sessionBeans;

import entities.School;
import entities.Student;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface StudentFacadeLocal {

    void create(Student student);

    void edit(Student student);

    void remove(Student student);

    Student find(Object id);

    List<Student> findAll();

    List<Student> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);
    int executUpdate(String sql);

    List<Student> getStudent4School(School school);
    
    Student findByName(String studentIdInSchool);
}
