/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Resourceinfo;
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
public class ResourceinfoFacade extends AbstractFacade<Resourceinfo> implements ResourceinfoFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResourceinfoFacade() {
        super(Resourceinfo.class);
    }

    @Override
    public Resourceinfo findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Resourceinfo.findByName");
        query.setParameter("name", name);
        List<Resourceinfo> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

}
