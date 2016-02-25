package hu.infokristaly.homework.primefaces.databeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import hu.infokristaly.homework.primefaces.domain.SystemUser;

@SessionScoped
@Named
public class SessionEventBean implements Serializable {

    private static final long serialVersionUID = 1889487896935257529L;

    @Inject
    private StatelessDataBean dataBean;

    private LazyDataModel<SystemUser> lazyDataModel;

    private SystemUser[] selectedUsers = {};
    
    private SystemUser newSystemUser;

    public LazyDataModel<SystemUser> getLazyDataModel() {
        if (lazyDataModel == null) {
            lazyDataModel = new LazyDataModel<SystemUser>() {

                private static final long serialVersionUID = 1678907483750487431L;

                private Map<String, Object> actualfilters;

                private String actualOrderField;

                private SortOrder actualSortOrder;

                @Override
                public SystemUser getRowData(String rowKey) {
                    SystemUser systemUser = new SystemUser();
                    systemUser.setUserid(Long.valueOf(rowKey));
                    return dataBean.find(systemUser);
                }

                @Override
                public Object getRowKey(SystemUser systemUser) {
                    return systemUser.getUserid();
                }

                @Override
                public List<SystemUser> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    this.setPageSize(pageSize);
                    this.actualfilters = filters;
                    if (sortField != null) {
                        this.actualOrderField = sortField;
                    }
                    if (sortOrder != null) {
                        this.actualSortOrder = sortOrder;
                    }

                    List<SystemUser> result = (List<SystemUser>) dataBean.findRange(first, pageSize, actualOrderField, actualSortOrder, filters);
                    return result;
                }

                @Override
                public int getRowCount() {
                    int result = dataBean.count(actualfilters);
                    return result;
                }

            };
        }
        return lazyDataModel;
    }
    
    public List<SystemUser> findAll() {
    	return dataBean.findAll();
    }

    public void createNewSystemUser() {
        setNewSystemUser(new SystemUser());
    }
    
    public void onEdit(RowEditEvent event) {
        SystemUser systemUser = (SystemUser) event.getObject();
        FacesMessage msg = new FacesMessage("Kliens adatai átszerkesztve", systemUser.getUsername());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Kliens módosítása visszavonva", ((SystemUser) event.getObject()).getUsername());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public SystemUser[] getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(SystemUser[] selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public SystemUser getNewSystemUser() {
        return newSystemUser;
    }

    public void setNewSystemUser(SystemUser newSystemUser) {
        this.newSystemUser = newSystemUser;
    }
    
    public void disableUsers() {
        dataBean.disableUsers(selectedUsers);
    }
    
    public void persistCurrent() {
        dataBean.persist(newSystemUser);
    }
    
    public void deleteUsers() {
        dataBean.deleteUsers(selectedUsers);
    }
}
