package sessionBeans;



import entities.Parent;
import entities.Student;
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
public class ParentFacade extends AbstractFacade<Parent> implements ParentFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParentFacade() {
        super(Parent.class);
    }

    @Override
   public Parent findByName(String name){
        Query query = getEntityManager().createNamedQuery("Parent.findByName");
        query.setParameter("name", name);
        List<Parent> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
   }
}
