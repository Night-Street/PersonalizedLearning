/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Edgeamongsubject;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hgs
 */
@Stateless
public class EdgeamongsubjectFacade extends AbstractFacade<Edgeamongsubject> implements EdgeamongsubjectFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EdgeamongsubjectFacade() {
        super(Edgeamongsubject.class);
    }
    
}
