/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Edgeamongknowledge;
import entities.Knowledge;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hgs
 */
@Stateless
public class EdgeamongknowledgeFacade extends AbstractFacade<Edgeamongknowledge> implements EdgeamongknowledgeFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EdgeamongknowledgeFacade() {
        super(Edgeamongknowledge.class);
    }

    @Override
    public List<Edgeamongknowledge> findPredecessor(Knowledge knowledge) {
            String sqlString = "select * from Edgeamongknowledge where successornode=" + knowledge.getId();
            return getEntityManager().createNativeQuery(sqlString, Edgeamongknowledge.class).getResultList();
        
    }

    @Override
    public List<Edgeamongknowledge> findSuccessor(Knowledge knowledge) {
        String sqlString = "select * from Edgeamongknowledge where predecessornode=" + knowledge.getId();
        return getEntityManager().createNativeQuery(sqlString, Edgeamongknowledge.class).getResultList();
    }
}
