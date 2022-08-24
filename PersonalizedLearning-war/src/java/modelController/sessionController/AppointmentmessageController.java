package modelController.sessionController;

import entities.Appointmentmessage;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import sessionBeans.AppointmentmessageFacadeLocal;

/**
 *
 * @author Hao Guo-Sheng
 */
@Named("appointmentmessageController")
@SessionScoped
public class AppointmentmessageController extends CommonModelController<Appointmentmessage> implements java.io.Serializable {

    private Appointmentmessage current;
    private List<Appointmentmessage> productBathinfoList;

    private final String createPage = "appointment/teacher/Create";
    @EJB
    private AppointmentmessageFacadeLocal appointmentmessageFacadeLocal;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private AppointmentController appointmentController;

    public Appointmentmessage getSelected() {
        if (null == current) {
            current = new Appointmentmessage();
        }
        return current;
    }

    public void setSelected(Appointmentmessage current) {
        this.current = current;
    }

    public List<Appointmentmessage> getProductBathinfoList() {
        return productBathinfoList;
    }

    public void setProductBathinfoList(List<Appointmentmessage> productBathinfoList) {
        this.productBathinfoList = productBathinfoList;
    }

    public void prepareCreate() {
        setSelected(new Appointmentmessage());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createPage);
    }

    public void create() {
        current.setTeacherid(teacherAdminController.getLogined());
        appointmentmessageFacadeLocal.create(current);
    }
/*
    携带身份证；
	不满18周岁，需要家长陪同，同时带户口本；
	体温正常，且最近14天没去过疫情风险地区，未与疑似患者密切接触。
    */
}
