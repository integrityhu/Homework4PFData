package hu.infokristaly.homework.primefaces.databeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.primefaces.model.SortOrder;

import hu.infokristaly.homework.primefaces.domain.SystemUser;

@Stateless
@Named
public class StatelessDataBean implements Serializable {

    private static final long serialVersionUID = 1889487896935257529L;

    @Inject
    private EntityManager em;
    
    @SuppressWarnings("unchecked")
    public List<SystemUser> findRange(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Query q = em.createQuery("from SystemUser");
        q.setFirstResult(first);
        q.setMaxResults(pageSize);
        return q.getResultList();
    }

    public SystemUser find(SystemUser systemUser) {
        return em.find(SystemUser.class, systemUser.getUserid());
    }

    public int count(Map<String, Object> actualfilters) {
        Query q = em.createQuery("select count(*) from SystemUser");
        return ((Long)q.getSingleResult()).intValue();
    }

    public void disableUsers(SystemUser[] selectedUsers) {
        // TODO Auto-generated method stub
        
    }

    public void persist(SystemUser newSystemUser) {        
        em.persist(newSystemUser);
    }

    public void deleteUsers(SystemUser[] selectedUsers) {
        // TODO Auto-generated method stub
        
    }

	public List<SystemUser> findAll() {
		Query q =  em.createQuery("from SystemUser");
		return q.getResultList(); 
	}
    
}