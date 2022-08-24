/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Partofspeech;
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
public class PartofspeechFacade extends AbstractFacade<Partofspeech> implements PartofspeechFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PartofspeechFacade() {
        super(Partofspeech.class);
    }

    @Override
    public Partofspeech findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Partofspeech.findByName");
        query.setParameter("name", name);
        List<Partofspeech> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

}
