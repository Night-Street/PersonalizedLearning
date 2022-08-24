package modelController.sessionController;

import entities.Appointment;
import entities.Appointmentmessage;
import entities.TeacherAdmin;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import sessionBeans.AppointmentFacadeLocal;
import tools.DateTimeConvertor;

/**
 *
 * @author Administrator
 */
@Named("appointmentController")
@SessionScoped
public class AppointmentController extends CommonModelController<Appointment> implements java.io.Serializable {

    private Appointment current;
    private List<Appointment> productList = new LinkedList<>();
    private int interval;
    private int parallelNumber;
    private Calendar startTime, endTime;
    private TeacherAdmin teacherAdmin;
    private Date selectedDate;
    @Inject
    private AppointmentmessageController productBatchInfoController;
    String createPage = "appointment/teacher/Create",
            listpage = "appointment/teacher/List";
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private modelController.applicationController.AppointmentController applicationAppointmentController;
    @EJB
    private AppointmentFacadeLocal appointmentFacadeLocal;

    private final String tableName = "Appointmentmessage";

    public void addProduct() {
        int productNumber = (int) (getEndTime().getTimeInMillis() - getStartTime().getTimeInMillis()) / 1000 / 60;//分钟
        productBatchInfoController.create();
        for (int i = 0; i < productNumber; i++) {
            for (int j = 0; j < interval; j++) {
                Appointment temProduct = new Appointment();
                temProduct.setAppointmentmessage(productBatchInfoController.getSelected());
                Calendar ctem = (Calendar) getStartTime().clone();
                // new Date2Calendar().getCalendar4Date(ctem, getStartTime());
                ctem.add(Calendar.MINUTE, interval * (i + 1));//在current.getStarttime()的分钟上加上相应的时间
                temProduct.setStarttime(ctem.getTime());
                Calendar cEnd = (Calendar) getStartTime().clone();
                cEnd.add(Calendar.MINUTE, interval * (i + 2));
                temProduct.setEndtime(cEnd.getTime());
                getProductList().add(temProduct);
                appointmentFacadeLocal.create(temProduct);
            }
        }
        mainXhtml.setPageName(this.createPage);
    }

    public void view() {
        this.addProduct();
        mainXhtml.setPageName(this.listpage);
    }

    public String destroy(Appointment product) {
        this.getProductList().remove(product);
        appointmentFacadeLocal.remove(product);
        return null;
    }

    public List<Appointment> getProductList() {
        return productList;
    }

    public void initProductList() {
        productList.clear();
        Set<Appointmentmessage> appointmentmessageSet = new HashSet<>();
        if (null != teacherAdminController.getLogined() && applicationRoleinfoController.isTeahcer(teacherAdminController.getLogined().getRoleId())) {
            appointmentmessageSet = teacherAdminController.getLogined().getAppointmentmessageSet();
            for (Appointmentmessage apm : appointmentmessageSet) {
                productList.addAll(apm.getAppointmentSet());
            }
        } else if (null != studentController.getLogined()) {
            productList.addAll(studentController.getLogined().getAppointmentSet());
        } else if (null != teacherAdminController.getLogined() && applicationRoleinfoController.isSecreatary(teacherAdminController.getLogined().getRoleId())) {
            productList.addAll(applicationAppointmentController.getAllList());
        }
        pageOperation.refreshData(productList);
        mainXhtml.setPageName(this.listpage);
    }

    public void setProductList(List<Appointment> productList) {
        this.productList = productList;
    }

    public int getInterval() {
        if (interval == 0) {
            interval = 1;
        }
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Appointment getSelected() {
        if (null == current) {
            selectedItemIndex = -1;
            current = new Appointment();
        }
        return current;
    }

    public void setSelected(Appointment current) {
        this.current = current;
    }

    /**
     * @return the parallelNumber
     */
    public int getParallelNumber() {
        if (parallelNumber == 0) {
            parallelNumber = 1;
        }
        return parallelNumber;
    }

    /**
     * @param parallelNumber the parallelNumber to set
     */
    public void setParallelNumber(int parallelNumber) {
        this.parallelNumber = parallelNumber;
    }

    public DateTimeConvertor getDateTimeConverter() {
        DateTimeConvertor dateTimeConverter = new DateTimeConvertor();
        return dateTimeConverter;
    }

    public void setDataModelList() {
        pageOperation.setDataModelList(productList);
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public boolean canSearch() {
        return null == selectedDate || null == teacherAdmin;
    }

    public TeacherAdmin getTeacherAdmin() {
        if (teacherAdmin == null) {
            teacherAdmin = teacherAdminController.getLogined();
        }
        return teacherAdmin;
    }

    public void setTeacherAdmin(TeacherAdmin teacherAdmin) {
        this.teacherAdmin = teacherAdmin;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void search() {
        productList.clear();
        Set<Appointmentmessage> apmtSet = teacherAdmin.getAppointmentmessageSet();
        for (Appointmentmessage apm : apmtSet) {
            Set<Appointment> appointmentSet = apm.getAppointmentSet();
            for (Appointment apt : appointmentSet) {
                if (apt.getStarttime().after(selectedDate)) {
                    productList.add(apt);
                }
            }
        }
        userMessagor.addMessage(commonSession.getResourceBound().getString("Finished"));
    }
}
