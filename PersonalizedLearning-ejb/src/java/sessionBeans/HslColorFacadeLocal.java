/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.HslColor;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author haogs
 */
@Local
public interface HslColorFacadeLocal {

    void create(HslColor hslColor);

    void edit(HslColor hslColor);

    void remove(HslColor hslColor);

    HslColor find(Object id);

    List<HslColor> findAll();

    List<HslColor> findRange(int[] range);

    int count();
       List getQueryResultList(String sql) ; 
int executUpdate(String sql) ;
}
