package hu.infokristaly.homework.primefaces.databeans;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class IdleMonitorView {

    
        public void onIdle() {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                            "No activity.", "What are you doing over there?"));
        }
     
        public void onActive() {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                            "Welcome Back", "Well, that's a long coffee break!"));
        }
    

}
