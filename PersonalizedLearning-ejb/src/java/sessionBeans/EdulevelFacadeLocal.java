/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Edulevel;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface EdulevelFacadeLocal {

    void create(Edulevel edulevel);

    void edit(Edulevel edulevel);

    void remove(Edulevel edulevel);

    Edulevel find(Object id);

    List<Edulevel> findAll();

    List<Edulevel> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    Edulevel findByName(String name);
}
