/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.List;
import javax.faces.model.DataModel;
import tools.pagination.PaginationHelper;

/**
 *
 * @author hgs
 */
public class PageOperation implements java.io.Serializable {

    private PaginationHelper pagination;
    private DataModel items;
    //这里的dataList里边保存的是pagination中的数据；
    //这个数据的变更主要来源于两个方面：一个是在表单上的查找结果；另一个是所有的结果；
    //因此，凡是涉及到查找结果变更的方法，以及更和删的地方都要对dataList进行更新，即调用setDataModelList(List dataList)
    private List dataList;

    public void refreshData(List dataList) {
        this.dataList = dataList;
        this.refreshData();
    }

    public void refreshData() {
        recreatePagination();
        recreateModel();
    }

    public DataModel getItems() {
        if (items == null) {
            if (null == getPagination()) {
                return null;
            }
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    public void recreateModel() {
        items = null;
    }

    public void recreatePagination() {
        setPagination(null);
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return null;
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return null;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataModelList(List dataList) {
        this.dataList = dataList;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = RepeatPaginator.getPaginationData(dataList, StaticFields.PAGESIZE);
        }
        return pagination;
    }

    public void setPagination(PaginationHelper pagination) {
        this.pagination = pagination;
    }
}
