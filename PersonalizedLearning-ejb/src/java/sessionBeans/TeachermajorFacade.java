package sessionBeans;

import entities.Major;
import entities.TeacherAdmin;
import entities.Teachermajor;
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
public class TeachermajorFacade extends AbstractFacade<Teachermajor> implements TeachermajorFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeachermajorFacade() {
        super(Teachermajor.class);
    }

    @Override
    public List<Teachermajor> getTeachermajor4Teacher(TeacherAdmin teacher) {
        Query query = getEntityManager().createNamedQuery("Teachermajor.findByteacherid");
        query.setParameter("teacherid", teacher);
        return query.getResultList();
    }

    @Override
    public List<Teachermajor> getTeachermajor4Major(Major major) {
        Query query = getEntityManager().createNamedQuery("Teachermajor.findBymajorid");
        query.setParameter("majorid", major);
        return query.getResultList();
    }

}
