package sessionBeans;

import entities.PageColor;
import entities.Student;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author haogs
 */
@Local
public interface PageColorFacadeLocal {

    void create(PageColor pageColor);

    void edit(PageColor pageColor);

    void remove(PageColor pageColor);

    PageColor find(Object id);

    List<PageColor> findAll();

    List<PageColor> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    public List<PageColor> getPageColor4Student(Student student);

    public List<PageColor> getPageColor4Teacher(TeacherAdmin teacher);
}
