package sessionBeans;

import entities.Roleinfo;
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
public class RoleinfoFacade extends AbstractFacade<Roleinfo> implements RoleinfoFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoleinfoFacade() {
        super(Roleinfo.class);
    }

    @Override
    public Roleinfo findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Roleinfo.findByRolename");
        query.setParameter("rolename", name);
        List<Roleinfo> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }
    
    @Override
     public List<Roleinfo> findAll() {
         return this.getQueryResultList("select * from roleinfo");
    }

}
