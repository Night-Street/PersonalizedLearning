package sessionBeans;

import entities.Leadpoint;
import entities.Student;
import entities.Studenttestpaper;
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
public class StudenttestpaperFacade extends AbstractFacade<Studenttestpaper> implements StudenttestpaperFacadelocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudenttestpaperFacade() {
        super(Studenttestpaper.class);
    }

    @Override
    public List<Studenttestpaper> getStudenttestpaper4Leadpoint(Leadpoint leadpoint) {
        Query query = getEntityManager().createNamedQuery("Studenttestpaper.findByleadpointId");
        query.setParameter("leadpointId", leadpoint);
        return query.getResultList();
    }

    @Override
    public List<Studenttestpaper> getStudenttestpaper4Testpaper(Testpaper testpaper) {
        Query query = getEntityManager().createNamedQuery("Studenttestpaper.findBytestpaperId");
        query.setParameter("testpaperId", testpaper);
        return query.getResultList();
    }

    @Override
    public List<Studenttestpaper> getStudenttestpaper4Student(Student student) {
        Query query = getEntityManager().createNamedQuery("Studenttestpaper.findBystudentId");
        query.setParameter("studentId", student);
        return query.getResultList();
    }

}
