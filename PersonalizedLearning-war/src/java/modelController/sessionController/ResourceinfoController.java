package modelController.sessionController;

import entities.Resourceinfo;
import entities.Roleinfo;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.applicationController.ResourceTree;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;
import tools.pagination.JsfUtil;

@Named("resourceinfoController")
@SessionScoped
public class ResourceinfoController extends CommonModelController<Resourceinfo> implements Serializable {

    @Inject
    private RoleinfoController roleinfoController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.ResourceinfoController applicationResourceinfoController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    // private List<Resourceinfo> resourceinfosList;
    //private HashMap<Roleinfo, List<Resourceinfo>> roleResouceMap = new HashMap<>();
    private final String tableName = "resourceinfo", listpage = "roleResource/List", editpage = "roleResource/Edit", viewpage = "roleResource/View", createpage = "roleResource/Create";
    protected Resourceinfo current;

    public Resourceinfo getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Resourceinfo();
        }
        return current;
    }
    public void setSelected(Resourceinfo resourceinfo) {
        current = resourceinfo;
    }

    public void setDataModelList() {
        pageOperation.setDataModelList(applicationResourceinfoController.getAllList());
    }

    public ResourceinfoController() {
    }

//For the update of resourceinfo for the selected role============
    private List<Resourceinfo> temResourceinfoList= new LinkedList<>();
    private List<String> result=new LinkedList<>();
    
    public List<String> getRoleResourceNames(){
        result.clear();
        for(Resourceinfo resourceinfo:getTemResourceinfoList()){
            result.add(resourceinfo.getName());
        }
        return result;
    }

    public List<Resourceinfo> getTemResourceinfoList() {
        temResourceinfoList.clear();
        if (null != roleinfoController.getSelected().getId()) {
            List<ResourceTree> temMap = applicationResourceinfoController.getRoleResouceMap().get(roleinfoController.getSelected());
            temMap.forEach((elem) -> {
                temResourceinfoList.addAll(elem.getChildren());
            });
        } 
        return temResourceinfoList;
    }

    public void setTemResourceinfoList(List<Resourceinfo> temResourceinfoList) {
        this.temResourceinfoList = temResourceinfoList;
        applicationResourceinfoController.resetResourceinfo();
    }

    public void editRoleResource() {
        StringBuilder sb = new StringBuilder();
        for (Resourceinfo resourceinfo : temResourceinfoList) {
            sb.append(",").append(resourceinfo.getId());
        }
        String resourceIds = sb.toString();
        if (resourceIds.trim().length() > 0) {
            resourceIds = resourceIds.substring(1);
        }
        roleinfoController.getSelected().setResources(resourceIds);
        roleinfoController.edit();
        applicationResourceinfoController.refresh();
        mainXhtml.setPageName(this.viewpage);
    }
//=================    

    public void create() {
        if (null == applicationResourceinfoController.findByName(current.getName().trim())) {
            applicationResourceinfoController.create(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            //2. Refresh the data
            //current = (Resourceinfo) (getFacade().getQueryResultList("select * from Resourceinfo where  name='" + current.getName().trim() + "'").get(0));
            applicationResourceinfoController.refresh();
            pageOperation.refreshData(applicationResourceinfoController.getAllList());
            this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
            prepareCreate();
        } else {
            userMessagor.addMessage(current.getName() + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
        }
    }

    public void prepareCreate() {
        roleinfoController.setSelected(new Roleinfo());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
    }

    public void destroy() {
        current = (Resourceinfo) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(this.listpage);
    }

    private void performDestroy() {
        try {
            applicationResourceinfoController.remove(current);
            applicationResourceinfoController.refresh();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            //resourceinfosList.remove(current);
            pageOperation.refreshData(applicationResourceinfoController.getAllList());
            this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller resource 2");
        }
    }

    private void updateCurrentItem() {
        int count = applicationResourceinfoController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( (Resourceinfo) applicationResourceinfoController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationResourceinfoController.getAllList(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationResourceinfoController.getAllList(), true);
    }
}
