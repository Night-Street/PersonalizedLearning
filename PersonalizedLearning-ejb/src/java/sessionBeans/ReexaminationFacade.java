package sessionBeans;

import entities.Reexamination;
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
public class ReexaminationFacade extends AbstractFacade<Reexamination> implements ReexaminationFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReexaminationFacade() {
        super(Reexamination.class);
    }
}
