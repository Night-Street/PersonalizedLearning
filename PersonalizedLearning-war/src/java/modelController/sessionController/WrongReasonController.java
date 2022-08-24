package modelController.sessionController;

import entities.WrongReason;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;
import tools.pagination.JsfUtil;

@Named("wrongReasonController")
@SessionScoped
public class WrongReasonController extends CommonModelController<WrongReason> implements Serializable {

    private final String tableName = "wrongReason", listpage = "wrongReason/List", editpage = "wrongReason/Edit",
            viewpage = "wrongReason/View", createpage = "wrongReason/Create";
    @Inject
    private modelController.applicationController.WrongReasonController applicationWrongReasonController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private tools.UserMessagor userMessagor;

    public WrongReasonController() {
    }
    protected WrongReason current;

    public WrongReason getSelected() {
        if (current == null) {
            current = new WrongReason();
            //current.setId(0);//判断真实的大于0，比判断为空更安全，因为判断为null时，出现“><”的结果
            selectedItemIndex = -1;
        }
        return current;
    }

    public void setSelected(WrongReason wrongReason) {
        current = wrongReason;
    }

    public void create() {
        try {
            if (null == applicationWrongReasonController.findByName(current.getName())) {
                applicationWrongReasonController.create(current);
                applicationWrongReasonController.getAllSet().add(current);
                //current = (WrongReason) (getFacade().getQueryResultList("select * from WrongReason where  WrongReason='" + current.getName().trim() + "'").get(0));
                pageOperation.refreshData(applicationWrongReasonController.getAllList());
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getName() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller question wrong 1");
        }
    }

    public void prepareCreate() {
        setSelected(new WrongReason());
        selectedItemIndex = -1;
        mainXhtml.setPageName(createpage);
    }

    public void destroy() {
        setSelected((WrongReason) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        mainXhtml.setPageName(listpage);
    }

    private void performDestroy() {
        applicationWrongReasonController.remove(current);
        userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        updateCurrentItem();
        pageOperation.refreshData(applicationWrongReasonController.getAllList());
        this.logs(current.getName(), tableName, StaticFields.OPERATIONDELETE);
        try {
            applicationWrongReasonController.remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            setSelected(null);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller question wrong 2");
        }        
    }

    public void prepareEdit() {
        setSelected((WrongReason) getItems().getRowData());
        selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(editpage);
    }

    public void update() {
        try {
            applicationWrongReasonController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(current.getName(), tableName, StaticFields.OPERATIONUPDATE);
            mainXhtml.setPageName(viewpage);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller question wrong 3");
        }
    }

    private void updateCurrentItem() {
        int count = applicationWrongReasonController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((WrongReason) applicationWrongReasonController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationWrongReasonController.getAllList(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationWrongReasonController.getAllList(), true);
    }

    public WrongReason getWrongReason(java.lang.Integer id) {
        return (WrongReason) applicationWrongReasonController.find(id);
    }

}
