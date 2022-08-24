package sessionBeans;

import entities.Student;
import entities.Studentaccupoints;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author haogs
 */
@Local
public interface StudentaccupointsFacadeLocal {

    void create(Studentaccupoints studentaccupoints);

    void edit(Studentaccupoints studentaccupoints);

    void remove(Studentaccupoints studentaccupoints);

    Studentaccupoints find(Object id);

    List<Studentaccupoints> findAll();

    List<Studentaccupoints> findRange(int[] range);

    int count();

    List<Studentaccupoints> getQueryResultList(String sql);

    public List<Studentaccupoints> getStudentaccupoints4Student(Student student);

    public List<Studentaccupoints> getStudentaccupoints4Teacher(TeacherAdmin teacher);
}
