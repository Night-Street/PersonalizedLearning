/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Statusofresources;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface StatusofresourcesFacadeLocal {

    void create(Statusofresources statusofresources);

    void edit(Statusofresources statusofresources);

    void remove(Statusofresources statusofresources);

    Statusofresources find(Object id);

    List<Statusofresources> findAll();

    List<Statusofresources> findRange(int[] range);

    int count();
    
}
