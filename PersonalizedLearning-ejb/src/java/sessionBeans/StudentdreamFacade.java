package sessionBeans;

import entities.Studentdream;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hgs，郝国生
 */
@Stateless
public class StudentdreamFacade extends AbstractFacade<Studentdream> implements StudentdreamFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentdreamFacade() {
        super(Studentdream.class);
    }
   
}
