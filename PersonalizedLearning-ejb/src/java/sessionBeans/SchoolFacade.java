package sessionBeans;

import entities.Major;
import entities.School;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hgs
 */
@Stateless
public class SchoolFacade extends AbstractFacade<School> implements SchoolFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SchoolFacade() {
        super(School.class);
    }

    @Override
    public List<School> getSchool4School(School school) {
        Query query = getEntityManager().createNamedQuery("School.findByschoolId");
        query.setParameter("schoolId", school);
        return query.getResultList();
    }

    @Override
    public List<School> getSchool4Major(Major major) {
        Query query = getEntityManager().createNamedQuery("School.findBymajorid");
        query.setParameter("majorid", major);
        return query.getResultList();
    }

    @Override
    public School findByName(String name) {
        Query query = getEntityManager().createNamedQuery("School.findByName");
        query.setParameter("name", name);
        List<School> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }
}
