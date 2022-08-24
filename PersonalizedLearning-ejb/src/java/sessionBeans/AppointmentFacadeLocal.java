/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Appointment;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hadoop
 */
@Local
public interface AppointmentFacadeLocal{

    void create(Appointment contributors);

    void edit(Appointment contributors);

    void remove(Appointment contributors);

    Appointment find(Object id);

    List<Appointment> findAll();

    List<Appointment> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    List<Integer> getQueryIntList (String sql);
}
