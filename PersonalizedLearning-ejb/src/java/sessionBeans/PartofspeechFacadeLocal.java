/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Partofspeech;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface PartofspeechFacadeLocal {

    void create(Partofspeech partofspeech);

    void edit(Partofspeech partofspeech);

    void remove(Partofspeech partofspeech);

    Partofspeech find(Object id);

    List<Partofspeech> findAll();

    List<Partofspeech> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    Partofspeech findByName(String name);
}
