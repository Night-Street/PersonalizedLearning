/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Predicate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hgs07
 */
@javax.ejb.Stateless
public class PredicateFacade extends AbstractFacade<Predicate> implements PredicateFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PredicateFacade() {
        super(Predicate.class);
    }

    @Override
    public Predicate findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Predicate.findByPname");
        query.setParameter("pname", name);
        List<Predicate> resultList = query.getResultList();
        if (resultList.size() > 0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Predicate> findAll() {
        Query query = getEntityManager().createNamedQuery("Predicate.findAll");
        List<Predicate> resultList = query.getResultList();
        return resultList;
    }
}
