package hu.infokristaly.homework.primefaces.databeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.ListModel;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.Operations;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
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

				private Integer count;

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
					this.count = null;
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
					if (count == null) {
						count = dataBean.count(actualfilters);
					}
					return count;
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

	public void phaseListener(PhaseEvent e) {
		if (e.getPhaseId().equals(PhaseId.RENDER_RESPONSE)) {
			FacesContext fc = FacesContext.getCurrentInstance();
			UIViewRoot root = fc.getViewRoot();
			DataTable table = (DataTable) root.findComponent(":lazyForm:lazyTable");
			if (table != null) {
				table.getChildren().forEach(c -> {
					if ((c instanceof Column) && (c.getChildren() != null) && (c.getChildren().size() > 0)) {
						UIComponent content = c.getChildren().get(0);
						if (content instanceof HtmlOutputText) {
							System.out.println(content.getValueExpression("value"));
						}
					}
				});
			}
		}
	}

	public void getDataConnectionInfo() {
		StringBuffer info = dataBean.getEntityConnectionInfo();
		FacesMessage msg = new FacesMessage("Database info", info.toString());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void getAppServerInfo() throws Exception {
		try (ModelControllerClient client = ModelControllerClient.Factory.create("localhost", 9990)) {
			ModelNode op = Operations.createOperation("product-info", new ModelNode().setEmptyList());			  
			ModelNode result = client.execute(op);
			boolean found = Operations.isSuccessfulOutcome(result);
			if (!found) {
				op = Operations.createReadResourceOperation(new ModelNode().setEmptyList());
				result = client.execute(op);
				found = Operations.isSuccessfulOutcome(result); 
			}
			if (found) {
				final ModelNode model = Operations.readResult(result);
				String productName = null;
				String productVersion = null;
				if (model.getType().equals(ModelType.LIST)) {
					final ModelNode model0 = model.get(0);
					if (model0.getType().equals(ModelType.OBJECT)) {
						final ModelNode summary = model0.get("summary");
						
						if (summary.hasDefined("product-name")) {
							productName = summary.get("product-name").asString();
						} else {
							productName = "WildFly";
						}

						
						if (summary.hasDefined("product-version")) {
							productVersion = summary.get("product-version").asString();
						}
						
					}
				} else {
					if (model.hasDefined("product-name")) {
						productName = model.get("product-name").asString();
					}
					if (model.hasDefined("product-version")) {
						productVersion = model.get("product-version").asString();
					}				
				}
				StringBuffer info = new StringBuffer();
				info.append("Product: " + productName + "\n");
				info.append("Product Version: " + productVersion);
				
				FacesMessage msg = new FacesMessage("AppServer info", info.toString());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				System.out.println(result);
				System.out.println(Operations.getFailureDescription(result));
			}
		}catch(

	Exception ex)

	{
		System.out.println(ex);
	}
}}
