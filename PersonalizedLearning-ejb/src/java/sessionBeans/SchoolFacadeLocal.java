package sessionBeans;

import entities.Major;
import entities.School;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface SchoolFacadeLocal {

    void create(School school);

    void edit(School school);

    void remove(School school);

    School find(Object id);

    List<School> findAll();

    List<School> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    List<School> getSchool4School(School school);

    List<School> getSchool4Major(Major major);

    School findByName(String name);
}
