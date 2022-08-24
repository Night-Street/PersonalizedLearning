package sessionBeans;

import entities.Knowledge;
import entities.Question;
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
public class QuestionFacade extends AbstractFacade<Question> implements QuestionFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QuestionFacade() {
        super(Question.class);
    }


    @Override
    public List<Question> getQuestion4Knowledge(Knowledge knowledge) {
        Query query = getEntityManager().createNamedQuery("Question.findByknowledgeId");
        query.setParameter("knowledgeId", knowledge);
        return query.getResultList();
    }

    @Override
    public Question findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Question.findByValueinfo");
        query.setParameter("valueinfo", name);
        if(query.getResultList().size()>0)
            return (Question) query.getResultList().get(0);
        else return  null;
    }

    @Override
    public List getGeneralQueryList(String string) {
       return getEntityManager().createNativeQuery(string).getResultList();
    }

}
