package com.financeiro.controller.bean;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class LoginBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuario usuario;
	
	private String nomeUsuario;
	private String senha;
	
	public String login() {	
		if ("a".equalsIgnoreCase(this.nomeUsuario) && "".equals(senha)) {
			this.usuario.setNome("Osnir Gois");
			this.usuario.setDataLogin(new Date());
			
			return "/pages/interno/ConsultaLancamentos?faces-redirect=true";
		} else {
			addErrorMessage("Usuário/senha inválidos");
		}
		
		return null;		
	}
	
	public String logout() {
		clearSession();
		return "/Login?faces-redirect=true";
	}
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
