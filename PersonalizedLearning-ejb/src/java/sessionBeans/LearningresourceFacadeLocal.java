package sessionBeans;

import entities.Knowledge;
import entities.Learningresource;
import entities.Student;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface LearningresourceFacadeLocal {

    void create(Learningresource learningresource);

    void edit(Learningresource learningresource);

    void remove(Learningresource learningresource);

    Learningresource find(Object id);

    List<Learningresource> findAll();

    List<Learningresource> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    List<Learningresource> getLearningResources4Subject(Knowledge knowledge);

    List<Learningresource> getLearningResources4Student(Student student);

    List<Learningresource> getLearningResources4Teacher(TeacherAdmin teacher);

    Learningresource findByName(String name);
}
