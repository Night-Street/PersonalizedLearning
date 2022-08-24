package sessionBeans;

import entities.School;
import entities.Subject;
import entities.TeacherAdmin;
import entities.Teacschoolsubject;
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
public class TeacschoolsubjectFacade extends AbstractFacade<Teacschoolsubject> implements TeacschoolsubjectFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeacschoolsubjectFacade() {
        super(Teacschoolsubject.class);
    }

    @Override
    public List<Teacschoolsubject> getTeacschoolsubject4School(School school) {
        Query query = getEntityManager().createNamedQuery("Teacschoolsubject.findByschoolid");
        query.setParameter("schoolid", school);
        return query.getResultList();
    }

    @Override
    public List<Teacschoolsubject> getTeacschoolsubject4Subject(Subject subject) {
        Query query = getEntityManager().createNamedQuery("Teacschoolsubject.findBysubjectid");
        query.setParameter("subjectid", subject);
        return query.getResultList();
    }

    @Override
    public List<Teacschoolsubject> getTeacschoolsubject4Teacher(TeacherAdmin teacher) {
        Query query = getEntityManager().createNamedQuery("Teacschoolsubject.findByteacherid");
        query.setParameter("teacherid", teacher);
        return query.getResultList();
    }

}
