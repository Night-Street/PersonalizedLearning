/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Edulevel;
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
public class EdulevelFacade extends AbstractFacade<Edulevel> implements EdulevelFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EdulevelFacade() {
        super(Edulevel.class);
    }

    @Override
    public Edulevel findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Edulevel.findByName");
        query.setParameter("name", name);
        List<Edulevel> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

}
