package com.financeiro.controller.bean;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.financeiro.model.Lancamento;
import com.financeiro.repository.Lancamentos;
import com.financeiro.service.CadastroLancamentos;
import com.financeiro.service.NegocioException;

@Named
@javax.faces.view.ViewScoped
public class ConsultaLancamentosBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Inject
	private Lancamentos lancamentosRepository;
	@Inject
	private CadastroLancamentos cadastro;

	private Lancamento lancamentoSelecionado;
	private List<Lancamento> lancamentos;

	public void excluir() {
		try {
			this.cadastro.excluir(this.lancamentoSelecionado);
			this.consultar();
		} catch (NegocioException e) {
			this.addErrorMessage(e);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao excluir registro.");
		}
	}

	public void consultar() {
		try {
			this.lancamentos = lancamentosRepository.todos();
		} catch (Exception e) {
			this.addErrorMessage("Erro ao carregar registros.");
		}
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public Lancamento getLancamentoSelecionado() {
		return lancamentoSelecionado;
	}

	public void setLancamentoSelecionado(Lancamento lancamentoSelecionado) {
		this.lancamentoSelecionado = lancamentoSelecionado;
	}
}
