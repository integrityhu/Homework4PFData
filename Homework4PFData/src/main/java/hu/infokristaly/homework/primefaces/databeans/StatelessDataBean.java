package hu.infokristaly.homework.primefaces.databeans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import org.primefaces.model.SortOrder;

import hu.infokristaly.homework.primefaces.domain.SystemUser;

@Stateless
@Named
public class StatelessDataBean implements Serializable {

	private static final long serialVersionUID = 1889487896935257529L;

	@Inject
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<SystemUser> findRange(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		StringBuffer qStr = new StringBuffer("from SystemUser");
		if ((filters != null) && filters.containsKey("username")) {			
			qStr.append(" where username like :username");
		}
		Query q = em.createQuery(qStr.toString());
		if ((filters != null) && filters.containsKey("username")) {
			String name = (String)filters.get("username");
			q.setParameter("username", name+"%");
		}
		q.setFirstResult(first);
		q.setMaxResults(pageSize);
		return q.getResultList();
	}

	public SystemUser find(SystemUser systemUser) {
		return em.find(SystemUser.class, systemUser.getUserid());
	}

	public int count(Map<String, Object> actualfilters) {
		StringBuffer qStr = new StringBuffer("select count(*) from SystemUser");
		
		if ((actualfilters != null) && actualfilters.containsKey("username")) {
			qStr.append(" where username like :username");
		}
		Query q = em.createQuery(qStr.toString());
		if ((actualfilters != null) && actualfilters.containsKey("username")) {
			String name = (String)actualfilters.get("username");			
			q.setParameter("username", name+"%");
			System.out.println("username : " + name);
		}
		return ((Long) q.getSingleResult()).intValue();
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
		Query q = em.createQuery("from SystemUser");
		return q.getResultList();
	}

    public StringBuffer getEntityConnectionInfo() {
    	StringBuffer info = new StringBuffer();
        Session session = (Session) em.getDelegate();
        SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();        
        org.hibernate.engine.spi.SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        Connection connection = sessionImp.connection();
        try {         
            String dataBaseUrl = connection.getMetaData().getURL();
            String dataBaseProductName = connection.getMetaData().getDatabaseProductName();
            String dataBaseProductVersion = connection.getMetaData().getDatabaseProductVersion();
            String driverName = connection.getMetaData().getDriverName();
            String driverVersion = connection.getMetaData().getDriverVersion();
            int major = connection.getMetaData().getDatabaseMajorVersion();
            int minor = connection.getMetaData().getDatabaseMinorVersion();
            
            info.append("BaseUrl: "+dataBaseUrl+"\n");            
            info.append("BaseProductName: "+dataBaseProductName + " " + dataBaseProductVersion + "("+ major + "." + minor+")\n");
            info.append("DriverName: "+driverName+"\n");
            info.append("DriverVersion: "+driverVersion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

}