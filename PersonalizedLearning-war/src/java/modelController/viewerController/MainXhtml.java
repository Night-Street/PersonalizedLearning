package modelController.viewerController;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.applicationController.ResourceTree;
import modelController.sessionController.CommonSession;
import tools.PublicFields;

/**
 *
 * @author hgs This class if for the left menu operation and is used in
 * main.xhmtl
 */
@SessionScoped
@Named
public class MainXhtml implements Serializable {

    @Inject
    PublicFields publicFields;
    @Inject
    CommonSession commonSession;
    @Inject
    private modelController.applicationController.ResourceinfoController applicationResourceinfoControll;
    private String pageName;

    public List<ResourceTree> getResouceMap() {
        return applicationResourceinfoControll.getResMap(commonSession.getRoleinfo());
    }

    public String getPageName() {
        if (null == pageName) {
            pageName = "news";
        }
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

}
