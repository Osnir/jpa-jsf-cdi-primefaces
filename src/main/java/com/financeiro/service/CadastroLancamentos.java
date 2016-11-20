package com.financeiro.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.financeiro.dao.exception.DataAccessException;
import com.financeiro.model.Lancamento;
import com.financeiro.repository.Lancamentos;
import com.financeiro.util.Transactional;

public class CadastroLancamentos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Lancamentos lancamentos;

	@Transactional
	public Lancamento Salvar(Lancamento lancamento) throws NegocioException, DataAccessException {
		if (lancamento.getDataPagamento() != null && lancamento.getDataPagamento().after(new Date())) {
			throw new NegocioException("Data de pagamento não pode ser uma data futura.");
		}
		
		return this.lancamentos.salvar(lancamento);
	}
	
	@Transactional
	public void excluir(Lancamento lancamento) throws NegocioException, DataAccessException {
		lancamento = this.lancamentos.porId(lancamento.getId());
		
		if (lancamento.getDataPagamento() != null) {
			throw new NegocioException("Não é possível excluir um lançamento pago!");
		}
		
		this.lancamentos.excluir(lancamento);
	}
}
