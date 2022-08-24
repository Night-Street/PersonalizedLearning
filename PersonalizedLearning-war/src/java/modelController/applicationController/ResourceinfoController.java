package modelController.applicationController;

import entities.Resourceinfo;
import entities.Roleinfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import sessionBeans.ResourceinfoFacadeLocal;

@Named("resourceinfoControllerA")
@ApplicationScoped
public class ResourceinfoController   extends ApplicationCommonController  {

    @EJB
    private sessionBeans.ResourceinfoFacadeLocal ejbFacadelocal;
    @Inject
    modelController.applicationController.RoleinfoController applicationRoleinfoController;
    private HashMap<Roleinfo, List<ResourceTree>> roleResouceList = new HashMap<>();
    private List<Resourceinfo> allResourceinfos = new LinkedList<>();
    private HashMap<Resourceinfo, List<Resourceinfo>> hierachyResouceinfo = new HashMap<>();

    public ResourceinfoController() {
    }

    public Resourceinfo findByName(String name) {
        Resourceinfo result = null;
        for (Resourceinfo resourceinfo : getAllList()) {
            if (resourceinfo.getName().trim().equals(name.trim())) {
                result = resourceinfo;
            }
        }
        return result;
    }

    private void calcuRoleResourceList() {
        roleResouceList.clear();
        List<Roleinfo> allRoleinfos = applicationRoleinfoController.getAllList();
        allRoleinfos.forEach((role) -> {
            roleResouceList.put(role, new LinkedList<>());
        });
        for (Roleinfo role : allRoleinfos) {
            List<String> resoursIds = Arrays.asList((applicationRoleinfoController.getQueryResultList("select * from roleinfo where id="+role.getId()).get(0).getResources()).split(","));
            List<ResourceTree> tem = new LinkedList<>();
            for (Resourceinfo resourceinfo : firstLevelResoure()) {//获得父结点，
                ResourceTree childrenResource = new ResourceTree();
                childrenResource.setChildren(new LinkedList<>());
                for (Resourceinfo child : resourceinfo.getResourceinfoSet()) {//下面只对其子结点进行过滤
                    if (resoursIds.contains(child.getId().toString())) {//子结点中属于当用角色的功能，则放入
                        childrenResource.getChildren().add(child);
                    }
                }
                if (!childrenResource.getChildren().isEmpty()) {
                    childrenResource.setRoot(resourceinfo);
                    tem.add(childrenResource);
                }
            }
            tem.sort(new ResourceTree());
            tem.forEach((ele) -> {
                ele.getChildren().sort((Resourceinfo o1, Resourceinfo o2) -> o1.getMenuorder() - o2.getMenuorder());
            });
            roleResouceList.put(role, tem);
        }
    }
    List<Resourceinfo> firstLevel;

    private List<Resourceinfo> firstLevelResoure() {
        if (null == firstLevel) {
            firstLevel = getFacade().getQueryResultList("select * from resourceinfo where parentid is null order by menuorder asc");
        }
        return firstLevel;
    }

    public List<ResourceTree> getResMap(Roleinfo roleType) {
        return getRoleResouceMap().get(roleType);
    }

    public HashMap<Roleinfo, List<ResourceTree>> getRoleResouceMap() {
        if (roleResouceList.isEmpty()) {
            calcuRoleResourceList();
        }
        return roleResouceList;
    }

    public HashMap<Resourceinfo, List<Resourceinfo>> getHierachyResouceinfo() {
        if (hierachyResouceinfo.isEmpty()) {
            for (Resourceinfo resourceinfo : getAllList()) {
                if (resourceinfo.getParentid() == null) {
                    LinkedList<Resourceinfo> temList = new LinkedList();
                    temList.addAll(resourceinfo.getResourceinfoSet());
                    hierachyResouceinfo.put(resourceinfo, temList);
                }
            }
        }
        return hierachyResouceinfo;
    }

    public Set getAllSet() {
        return new HashSet(getAllList());
    }

    public List<Resourceinfo> getAllList() {//由于菜单几乎不变化，所以不再重新获取
        if (this.allResourceinfos.isEmpty()) {
            allResourceinfos = getFacade().findAll();
        }
        return allResourceinfos;
    }

    public void resetResourceinfo() {
        allResourceinfos.clear();
    }

    public void refresh() {
        roleResouceList.clear();
    }

    private ResourceinfoFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public Resourceinfo getResourceinfo(java.lang.Integer id) {
        return ejbFacadelocal.find(id);
    }

    public void create(Resourceinfo resourceinfo) {
        getFacade().create(resourceinfo);
    }

    public void remove(Resourceinfo resourceinfo) {
        getFacade().remove(resourceinfo);
    }

    public void edit(Resourceinfo resourceinfo) {
        getFacade().edit(resourceinfo);
    }

    public List<Resourceinfo> getQueryResultList(String sql) {
        return getFacade().getQueryResultList(sql);
    }

    public int count() {
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
    }

    public List<Resourceinfo> findRange(int[] range) {
        return getFacade().findRange(range);
    }

    public Resourceinfo find(Integer id) {
        return getFacade().find(id);
    }

    @FacesConverter(forClass = Resourceinfo.class)
    public static class ResourceinfoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ResourceinfoController controller = (ResourceinfoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "resourceinfoControllerA");
            return controller.getResourceinfo(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Resourceinfo) {
                Resourceinfo o = (Resourceinfo) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Resourceinfo.class.getName());
            }
        }
    }
}