package sessionBeans;

import entities.Scheduleclass;
import entities.TeacherAdmin;
import entities.Teacherschedule;
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
public class TeacherscheduleFacade extends AbstractFacade<Teacherschedule> implements TeacherscheduleFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeacherscheduleFacade() {
        super(Teacherschedule.class);
    }

    @Override
    public List<Teacherschedule> getTeacherschedule4Teacher(TeacherAdmin teacher) {
        Query query = getEntityManager().createNamedQuery("Teacherschedule.findByteacher");
        query.setParameter("teacher", teacher);
        return query.getResultList();
    }

    @Override
    public List<Teacherschedule> getTeacherschedule4Scheduleclass(Scheduleclass scheduleclass) {
        Query query = getEntityManager().createNamedQuery("Teacherschedule.findByclassid");
        query.setParameter("classid", scheduleclass);
        return query.getResultList();
    }

}
