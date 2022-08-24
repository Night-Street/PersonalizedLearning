/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.WrongReason;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface WrongReasonFacadeLocal {

    void create(WrongReason wrongReason);

    void edit(WrongReason wrongReason);

    void remove(WrongReason wrongReason);

    WrongReason find(Object id);

    List<WrongReason> findAll();

    List<WrongReason> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    WrongReason findByName(String name);
}
