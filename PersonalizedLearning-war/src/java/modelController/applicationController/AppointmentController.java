/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelController.applicationController;

import entities.Appointment;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sessionBeans.AppointmentFacadeLocal;
import tools.DateTimeConvertor;

/**
 *
 * @author Administrator
 */
@Named("appointmentControllerA")
@ApplicationScoped
public class AppointmentController implements java.io.Serializable {

    @Inject
    private AppointmentmessageController productBatchInfoController;
    @EJB
    private AppointmentFacadeLocal ejbFacadeLocal;
    

   public DateTimeConvertor getDateTimeConverter() {
        DateTimeConvertor dateTimeConverter = new DateTimeConvertor();
        return dateTimeConverter;
    }
   
   public List<Appointment> getAllList(){
       return ejbFacadeLocal.findAll();
   }
   
}
