package hu.infokristaly.homework.primefaces.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class SystemUser implements Serializable {

    private static final long serialVersionUID = 1899784687816530303L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userid;
    
    @Basic
    private String emailAddress;
    
    @Basic
    private boolean enabled;

    @Column(name="casemanager")
    @Basic
    private Boolean caseManager = false;

    @Basic
    private String userpassword;
    
    @Basic
    private String username;
    
    @Transient
    private boolean isAdmin = false;
    
    @Basic
    private String pinCode;
        
    public Long getUserid() {
        return userid;
    }
    
    public void setUserid(Long userid) {
        this.userid = userid;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }
    
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getUserpassword() {
        return userpassword;
    }
    
    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(Boolean caseManager) {
        this.caseManager = caseManager;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userid == null) ? 0 : userid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SystemUser))
            return false;
        SystemUser other = (SystemUser) obj;
        if (userid == null) {
            if (other.userid != null)
                return false;
        } else if (!userid.equals(other.userid))
            return false;
        return true;
    }

}
