/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Contributors;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hadoop
 */
@Stateless
public class ContributorsFacade extends AbstractFacade<Contributors> implements ContributorsFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ContributorsFacade() {
        super(Contributors.class);
    }

    @Override
    public Contributors findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Contributors.findByCName");
        query.setParameter("cName", name);
        List<Contributors> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

}
