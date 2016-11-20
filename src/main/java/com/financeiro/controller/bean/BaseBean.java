package com.financeiro.controller.bean;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

public class BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String filtro;
	
	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public void addInfoMessage(String summaryDetail) {
		this.getFacesContext().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, summaryDetail, summaryDetail));
	}
	
	public void addErrorMessage(Exception ex) {
		this.addErrorMessage(ex.getMessage());
	}
	
	public void addErrorMessage(String summaryDetail) {
		this.getFacesContext().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, summaryDetail, summaryDetail));
	}
	
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	public ExternalContext getExternalContext() {
		return this.getFacesContext().getExternalContext();
	}
	
	public HttpSession getSession() {
		return (HttpSession) this.getExternalContext().getSession(false);
	}

	public void clearSession() {
		HttpSession session = this.getSession();
		
		if (session != null) {
			session.invalidate();
		}			
	}
	
	public void clearAllAttributeSession() {
		Map<String, Object> map = this.getExternalContext().getSessionMap();
		map.clear();		
	}
	
	public void executeJavaScript(String command) {
		RequestContext.getCurrentInstance().execute(command);
	}
}
