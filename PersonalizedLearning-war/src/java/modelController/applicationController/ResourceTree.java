package modelController.applicationController;

import entities.Resourceinfo;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author haogs
 */
public class ResourceTree implements Comparator<ResourceTree> {

    private Resourceinfo root;
    private List<Resourceinfo> children;

    public ResourceTree() {

    }

    public ResourceTree(Resourceinfo root, List<Resourceinfo> children) {
        this.root = root;
        this.children = children;
    }

    public Resourceinfo getRoot() {
        return root;
    }

    public void setRoot(Resourceinfo root) {
        this.root = root;
    }

    public List<Resourceinfo> getChildren() {
        return children;
    }

    public void setChildren(List<Resourceinfo> children) {
        this.children = children;
    }

    @Override
    public int compare(ResourceTree o1, ResourceTree o2) {
        return o1.getRoot().getMenuorder()-o2.getRoot().getMenuorder();
    }

}
