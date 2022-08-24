package modelController.sessionController;

import entities.Studentaccupoints;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import tools.pagination.JsfUtil;
import tools.pagination.PaginationHelper;

@Named("studentaccupointsController")
@SessionScoped
public class StudentaccupointsController extends CommonModelController<Studentaccupoints> implements Serializable {

    private DataModel items = null;
    @Inject
    private modelController.applicationController.StudentaccupointsController applicationStudentaccupointsController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private PaginationHelper pagination;

    public StudentaccupointsController() {
    }
    protected Studentaccupoints current;

    public Studentaccupoints getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Studentaccupoints();
        }
        return current;
    }
     public void setSelected(Studentaccupoints studentaccupoints){
         current=studentaccupoints;
     }
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        setSelected( (Studentaccupoints) getItems().getRowData());
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        setSelected( new Studentaccupoints());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            applicationStudentaccupointsController.create(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("StudentaccupointsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        setSelected( (Studentaccupoints) getItems().getRowData());
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            applicationStudentaccupointsController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("StudentaccupointsUpdated"));
            return "View";
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        setSelected( (Studentaccupoints) getItems().getRowData());
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            applicationStudentaccupointsController.remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("StudentaccupointsDeleted"));
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = applicationStudentaccupointsController.getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( applicationStudentaccupointsController.getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationStudentaccupointsController.getFacade().findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationStudentaccupointsController.getFacade().findAll(), true);
    }

    public Studentaccupoints getStudentaccupoints(java.lang.Integer id) {
        return applicationStudentaccupointsController.getFacade().find(id);
    }

}
