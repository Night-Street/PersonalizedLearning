package sessionBeans;

import entities.Questionstudentcosttime;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author haogs
 */
@Stateless
public class QuestionstudentcosttimeFacade extends AbstractFacade<Questionstudentcosttime> implements QuestionstudentcosttimeFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QuestionstudentcosttimeFacade() {
        super(Questionstudentcosttime.class);
    }
    @Override
        public void create(Questionstudentcosttime entity) {
        getEntityManager().persist(entity);
        edit(entity);//及时更新
    }

    @Override
    public void edit(Questionstudentcosttime entity) {
        getEntityManager().merge(entity);
    }
}
