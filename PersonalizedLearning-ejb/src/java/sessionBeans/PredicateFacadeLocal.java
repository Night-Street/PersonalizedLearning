/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Predicate;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs07
 */
@Local
public interface PredicateFacadeLocal {

    void create(Predicate predicate);

    void edit(Predicate predicate);

    void remove(Predicate predicate);

    Predicate find(Object id);

    List<Predicate> findAll();

    List<Predicate> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    Predicate findByName(String name);
}
