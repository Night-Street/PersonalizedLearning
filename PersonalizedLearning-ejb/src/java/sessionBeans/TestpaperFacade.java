package sessionBeans;

import entities.School;
import entities.Subject;
import entities.Testpaper;
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
public class TestpaperFacade extends AbstractFacade<Testpaper> implements TestpaperFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TestpaperFacade() {
        super(Testpaper.class);
    }

    @Override
    public List<Testpaper> getTestpaper4School(School school) {
        Query query = getEntityManager().createNamedQuery("Testpaper.findByschoolid");
        query.setParameter("schoolid", school);
        return query.getResultList(); }

    @Override
    public List<Testpaper> getTestpaper4Subject(Subject subject) {
        Query query = getEntityManager().createNamedQuery("Testpaper.findBysubjectids");
        query.setParameter("subjectids", subject);
        return query.getResultList(); }

}
