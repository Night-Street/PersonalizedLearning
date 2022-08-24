/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Qwquestionwrong;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author haogs
 */
@Stateless
public class QwquestionwrongFacade extends AbstractFacade<Qwquestionwrong> implements QwquestionwrongFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QwquestionwrongFacade() {
        super(Qwquestionwrong.class);
    }
    
}
