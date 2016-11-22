package com.financeiro.controller.bean;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.financeiro.model.Categoria;
import com.financeiro.model.Despesa;
import com.financeiro.repository.Categorias;
import com.financeiro.repository.Despesas;
import com.financeiro.service.DespesaService;
import com.financeiro.service.BusinessException;

@Named
@javax.faces.view.ViewScoped
public class DespesaBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Inject
	private Despesas repository;
	@Inject
	private Categorias categoriaRepository;
	@Inject
	private DespesaService service;

	private Despesa despesa;
	private List<Despesa> despesas;

	public String novaDespesa() {
		return "/pages/interno/CadastroDespesa?faces-redirect=true";
	}

	public void prepararCadastro() {
		if (this.despesa == null) {
			this.despesa = new Despesa();
		}
	}

	public void salvar() {
		try {
			this.despesa = this.service.Salvar(this.despesa);
			this.addInfoMessage("Despesa salva com sucesso!");
		} catch (BusinessException e) {
			this.addErrorMessage(e);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao salvar registro.");
		}
	}

	public void salvarNovo() {
		try {
			this.service.Salvar(despesa);
			this.despesa = new Despesa();
			this.addInfoMessage("Despesa salva com sucesso!");
		} catch (BusinessException ex) {
			this.addErrorMessage(ex);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao salvar registro.");
		}
	}

	public void excluir() {
		try {
			this.service.excluir(this.despesa);
			this.consultar();
			this.addInfoMessage("Despesa excluída com sucesso!");
		} catch (BusinessException ex) {
			this.addErrorMessage(ex);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao excluir registro.");
		}
	}

	public void consultar() {
		try {
			this.despesas = repository.todas();
		} catch (Exception e) {
			this.addErrorMessage("Erro ao carregar registros.");
		}
	}

	public void pesquisar() {
		try {
			this.despesas = repository.filtrar(this.getFiltro());
		} catch (Exception e) {
			this.addErrorMessage("Erro ao pesquisar registros.");
		}
	}

	public List<Categoria> pesquisarCategorias(String nome) {
		try {
			return this.categoriaRepository.porParteNome(nome);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao carregar categorias.");
			return null;
		}
	}
	
	public List<Despesa> getDespesas() {
		return despesas;
	}

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}
}
