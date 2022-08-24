/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Quesknowlgsub;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author haogs
 */
@Stateless
public class QuesknowlgsubFacade extends AbstractFacade<Quesknowlgsub> implements QuesknowlgsubFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QuesknowlgsubFacade() {
        super(Quesknowlgsub.class);
    }

}
