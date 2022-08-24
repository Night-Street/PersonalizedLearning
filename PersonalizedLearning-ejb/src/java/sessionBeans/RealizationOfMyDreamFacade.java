package sessionBeans;

import entities.Realizationofmydream;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hgs，郝国生
 */
@Stateless
public class RealizationOfMyDreamFacade extends AbstractFacade<Realizationofmydream> implements RealizationOfMyDreamFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RealizationOfMyDreamFacade() {
        super(Realizationofmydream.class);
    }
   
}
