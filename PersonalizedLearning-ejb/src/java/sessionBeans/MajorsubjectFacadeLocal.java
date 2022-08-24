package sessionBeans;

import entities.Major;
import entities.Majorsubject;
import entities.Student;
import entities.Subject;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface MajorsubjectFacadeLocal {

    void create(Majorsubject majorsubject);

    void edit(Majorsubject majorsubject);

    void remove(Majorsubject majorsubject);

    Majorsubject find(Object id);

    List<Majorsubject> findAll();

    List<Majorsubject> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    public List<Majorsubject> getMajorsubject4Major(Major major);

    public List<Majorsubject> getMajorsubject4Subject(Subject subject);
}
