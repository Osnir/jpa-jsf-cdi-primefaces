package com.financeiro.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.financeiro.dao.exception.DataAccessException;
import com.financeiro.model.Lancamento;
import com.financeiro.repository.Lancamentos;
import com.financeiro.util.Transactional;

public class LancamentoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Lancamentos lancamentos;

	@Transactional
	public Lancamento Salvar(Lancamento lancamento) throws BusinessException, DataAccessException {
		if (!lancamento.dataPagamentoValida()) {
			throw new BusinessException("Data de pagamento não pode ser uma data futura.");
		}
		
		return this.lancamentos.salvar(lancamento);
	}
	
	@Transactional
	public void excluir(Lancamento lancamento) throws BusinessException, DataAccessException {
		lancamento = this.lancamentos.porId(lancamento.getId());
		
		if (lancamento.getDataPagamento() != null) {
			throw new BusinessException("Não é possível excluir um lançamento pago!");
		}
		
		this.lancamentos.excluir(lancamento);
	}
}
