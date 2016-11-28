package com.financeiro.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.financeiro.dao.exception.DataAccessException;
import com.financeiro.model.Despesa;
import com.financeiro.model.Lancamento;
import com.financeiro.repository.Despesas;
import com.financeiro.repository.Lancamentos;
import com.financeiro.util.Transactional;
import com.financeiro.util.Util;

public class DespesaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Despesas despesas;
	@Inject
	private Lancamentos lancamentos;
	
	@Transactional
	public Despesa Salvar(Despesa despesa) throws BusinessException, DataAccessException {
		if (Util.isEmptyOrNull(despesa.getNome())) {
			throw new BusinessException("Despesa deve ser informada.");
		}
		if (this.DespesaExistente(despesa)) {
			throw new BusinessException("Já existe uma despesa cadastrada com esse nome.");
		}
		
		return this.despesas.salvar(despesa);
	}
	
	@Transactional
	public void excluir(Despesa despesa) throws BusinessException, DataAccessException{
		despesa = this.despesas.porId(despesa.getId());
		
		if (this.DespesaUtilizada(despesa)) {
			throw new BusinessException("Esta despesa está sendo utilizada por um ou mais lançamentos.");
		}
		
		this.despesas.excluir(despesa);
	}
	
	private boolean DespesaExistente(Despesa despesa) throws DataAccessException{
		Despesa existente = this.despesas.porNome(despesa.getNome());		
		return existente != null && existente.getId() != despesa.getId();
	}
	
	private boolean DespesaUtilizada(Despesa despesa) throws DataAccessException {
		List<Lancamento> usadas = this.lancamentos.porDespesa(despesa);
		return !usadas.isEmpty();
	}
}
