package sessionBeans;

import entities.Knowledge;
import entities.Subject;
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
public class KnowledgeFacade extends AbstractFacade<Knowledge> implements KnowledgeFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KnowledgeFacade() {
        super(Knowledge.class);
    }

    @Override
    public List<Knowledge> getKnowledge4Subject(Subject subject) {
        return  getResultList4Query("select * from knowledge where subject_id="+subject.getId());
    }
    
    private List<Knowledge> getResultList4Query(String query){
         return  getEntityManager().createNativeQuery(query, Knowledge.class).getResultList();
    }

    @Override
    public List<Knowledge> findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Knowledge.findByName");
        query.setParameter("name", name);
        if (query.getResultList().size() > 0) {
            return query.getResultList();
        } else {
            return null;
        }
    }
    @Override
    public List<Integer> getQueryResultIDsList(String sql) {//返回ID值，而不是其他组合对象
        return getEntityManager().createNativeQuery(sql).getResultList();
    }
}
