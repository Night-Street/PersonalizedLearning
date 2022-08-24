package modelController.sessionController;

import entities.Partofspeech;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;
import tools.pagination.JsfUtil;

@Named("partofspeechController")
@SessionScoped
public class PartofspeechController extends CommonModelController<Partofspeech> implements Serializable {

    private final String tableName = "partofspeech", listpage = "partofspeech/List", editpage = "partofspeech/Edit",
            viewpage = "partofspeech/View", createpage = "partofspeech/Create";
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.PartofSpeechController applicationPartofSpeechController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;

    public PartofspeechController() {
    }
    protected Partofspeech current;

    public Partofspeech getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Partofspeech();
        }
        return current;
    }
    public void setSelected(Partofspeech partofspeech) {
        current = partofspeech;
    }

    public void prepareList() {
        pageOperation.refreshData(applicationPartofSpeechController.getAllList());
        mainXhtml.setPageName(this.listpage);
    }

    public void prepareView() {
        setSelected( (Partofspeech) getItems().getRowData());
        selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.viewpage);
    }

    public void prepareCreate() {
        setSelected( new Partofspeech());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
    }

    public void create() {
        try {
            applicationPartofSpeechController.create(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("PartofspeechCreated"));
            prepareCreate();
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
        }
    }

    public void prepareEdit() {
        setSelected( (Partofspeech) getItems().getRowData());
        selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.editpage);
    }

    public void update() {
        try {
            applicationPartofSpeechController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("PartofspeechUpdated"));
            mainXhtml.setPageName(this.viewpage);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
        }
    }

    public void destroy() {
        current = (Partofspeech) getItems().getRowData();
        selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(listpage);
    }

    private void performDestroy() {
        try {
            applicationPartofSpeechController.remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            // applicationPartofSpeechController.refreshPartofspeechList();
            pageOperation.refreshData(applicationPartofSpeechController.getAllList());
            this.logs(current.getName(), tableName, StaticFields.OPERATIONDELETE);

        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "controll part of speech");
        }
    }

    private void updateCurrentItem() {
        int count = applicationPartofSpeechController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( (Partofspeech) applicationPartofSpeechController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationPartofSpeechController.getAllList(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationPartofSpeechController.getAllList(), true);
    }

    public Partofspeech getPartofspeech(java.lang.Integer id) {
        return (Partofspeech) applicationPartofSpeechController.find(id);
    }

}
