package modelController.sessionController;

import entities.Contributors;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import tools.pagination.JsfUtil;

@Named("contributorsController")
@SessionScoped
public class ContributorsController extends CommonModelController<Contributors> implements Serializable {

    private DataModel items = null;
    @Inject
    private modelController.applicationController.ContributorsController applicationContributorsController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
        protected Contributors current;

    public Contributors getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current=new Contributors();
        }
        return current;
    }

    public ContributorsController() {
    }

    public void setSelected(Contributors contributors) {
        current = contributors;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        setSelected( (Contributors) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        setSelected( new Contributors());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            if (null != applicationContributorsController.findByName(current.getCName())) {
                applicationContributorsController.getFacade().create(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("ContributorsCreated"));
                //JsfUtil.addSuccessMessage(StaticFields.MESSAGES_PROPTES.getString("ContributorsCreated"));
                return prepareCreate();
            } else {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Already") + commonSession.getResourceBound().getString("Exist"));
                //JsfUtil.addSuccessMessage(StaticFields.MESSAGES_PROPTES.getString("Already")+StaticFields.MESSAGES_PROPTES.getString("Exist"));
                return null;
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
            //JsfUtil.addErrorMessage(e, StaticFields.MESSAGES_PROPTES.getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        setSelected( (Contributors) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            applicationContributorsController.getFacade().edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("ContributorsUpdated"));
            //JsfUtil.addSuccessMessage(StaticFields.MESSAGES_PROPTES.getString("ContributorsUpdated"));
            return "View";
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
            //JsfUtil.addErrorMessage(e, StaticFields.MESSAGES_PROPTES.getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Contributors) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        setSelected(null);
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
            applicationContributorsController.getFacade().remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("ContributorsDeleted"));
            //JsfUtil.addSuccessMessage(StaticFields.MESSAGES_PROPTES.getString("ContributorsDeleted"));
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
            //JsfUtil.addErrorMessage(e, StaticFields.MESSAGES_PROPTES.getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = applicationContributorsController.getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( applicationContributorsController.getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
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
        pageOperation.setPagination(null);
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
        return JsfUtil.getSelectItems(applicationContributorsController.getFacade().findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationContributorsController.getFacade().findAll(), true);
    }

    public Contributors getContributors(java.lang.Integer id) {
        return applicationContributorsController.getFacade().find(id);
    }

    @FacesConverter(forClass = Contributors.class)
    public static class ContributorsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ContributorsController controller = (ContributorsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "contributorsController");
            return controller.getContributors(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Contributors) {
                Contributors o = (Contributors) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Contributors.class.getName());
            }
        }

    }

}
