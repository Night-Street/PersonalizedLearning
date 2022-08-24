/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Registeruser;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
public class RegisteruserFacade extends AbstractFacade<Registeruser> implements RegisteruserFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegisteruserFacade() {
        super(Registeruser.class);
    }

    @Override
    public Registeruser findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Registeruser.findByName");
        query.setParameter("name", name);
        List<Registeruser> resultList = query.getResultList();
        if (resultList.size() > 0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }
}
