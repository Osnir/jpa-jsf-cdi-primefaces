package com.financeiro.controller.bean;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.financeiro.model.Categoria;
import com.financeiro.repository.Categorias;
import com.financeiro.service.CadastroCategoria;
import com.financeiro.service.NegocioException;

@Named
@javax.faces.view.ViewScoped
public class CategoriaBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Inject
	private Categorias repository;
	@Inject
	private CadastroCategoria service;
	
	private Categoria categoria;
	private List<Categoria> categorias;

	public String novaCategoria() {
		return "/pages/interno/CadastroCategoria?faces-redirect=true";
	}
	
	public void prepararCadastro() {
		if (this.categoria == null) {
			this.categoria = new Categoria();
		}
	}
	public void salvar() {
		try {
			this.categoria = this.service.Salvar(this.categoria);
			this.addInfoMessage("Categoria salva com sucesso!");
		} catch (NegocioException e) {
			this.addErrorMessage(e);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao salvar registro.");
		}
	}

	public void salvarNovo() {
		try {
			this.service.Salvar(categoria);
			this.categoria = new Categoria();
			this.addInfoMessage("Categoria salva com sucesso!");
		} catch (NegocioException ex) {
			this.addErrorMessage(ex);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao salvar registro.");
		}
	}

	public void excluir() {
		try {
			this.service.excluir(this.categoria);
			this.consultar();
			this.addInfoMessage("Categoria excluída com sucesso!");
		} catch (NegocioException ex) {
			this.addErrorMessage(ex);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao excluir registro.");
		}
	}
	
	public void consultar() {
		try {
			this.categorias = repository.todas();
		} catch (Exception e) {
			this.addErrorMessage("Erro ao carregar registros.");
		}		
	}
	
	public void pesquisar() {
		try {
			this.categorias = repository.filtrar(this.getFiltro());
		} catch (Exception e) {
			this.addErrorMessage("Erro ao pesquisar registros.");
		}
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}
