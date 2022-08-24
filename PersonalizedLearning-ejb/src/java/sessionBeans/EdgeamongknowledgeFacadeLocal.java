/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Edgeamongknowledge;
import entities.Knowledge;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface EdgeamongknowledgeFacadeLocal {

    void create(Edgeamongknowledge edgeamongknowledge);

    void edit(Edgeamongknowledge edgeamongknowledge);

    void remove(Edgeamongknowledge edgeamongknowledge);

    Edgeamongknowledge find(Object id);

    List<Edgeamongknowledge> findAll();

    List<Edgeamongknowledge> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    List<Edgeamongknowledge> findPredecessor(Knowledge knowledge);
    
    List<Edgeamongknowledge> findSuccessor(Knowledge knowledge);
}
