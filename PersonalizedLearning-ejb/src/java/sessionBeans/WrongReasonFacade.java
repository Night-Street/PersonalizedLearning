/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.WrongReason;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hgs
 */
@Stateless
public class WrongReasonFacade extends AbstractFacade<WrongReason> implements WrongReasonFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WrongReasonFacade() {
        super(WrongReason.class);
    }

    @Override
    public WrongReason findByName(String name) {
        Query query = getEntityManager().createNamedQuery("WrongReason.findByName");
        query.setParameter("name", name);
        List<WrongReason> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

}
