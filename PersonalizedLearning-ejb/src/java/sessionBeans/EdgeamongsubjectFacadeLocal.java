/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Edgeamongknowledge;
import entities.Edgeamongsubject;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface EdgeamongsubjectFacadeLocal {

    void create(Edgeamongsubject edgeamongsubject);

    void edit(Edgeamongsubject edgeamongsubject);

    void remove(Edgeamongsubject edgeamongsubject);

    Edgeamongsubject find(Object id);

    List<Edgeamongsubject> findAll();

    List<Edgeamongsubject> findRange(int[] range);

    int count();
    
    List getQueryResultList(String sql);

    int executUpdate(String sql);


//    public void evict(Class entityClass, Object primarykey, Edgeamongsubject edgeamongsubject);
}
