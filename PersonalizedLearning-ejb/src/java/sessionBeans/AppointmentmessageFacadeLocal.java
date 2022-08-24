/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Appointmentmessage;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hadoop
 */
@Local
public interface AppointmentmessageFacadeLocal{

    void create(Appointmentmessage contributors);

    void edit(Appointmentmessage contributors);

    void remove(Appointmentmessage contributors);

    Appointmentmessage find(Object id);

    List<Appointmentmessage> findAll();

    List<Appointmentmessage> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    List<Integer> getQueryIntList (String sql);
}
