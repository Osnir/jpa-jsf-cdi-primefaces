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

public class CadastroDespesa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Despesas despesas;
	@Inject
	private Lancamentos lancamentos;
	
	@Transactional
	public Despesa Salvar(Despesa despesa) throws NegocioException, DataAccessException {
		if (this.DespesaExistente(despesa)) {
			throw new NegocioException("Já existe uma despesa cadastrada com esse nome.");
		}
		
		return this.despesas.salvar(despesa);
	}
	
	@Transactional
	public void excluir(Despesa despesa) throws NegocioException, DataAccessException{
		despesa = this.despesas.porId(despesa.getId());
		
		if (this.DespesaUtilizada(despesa)) {
			throw new NegocioException("Esta despesa está sendo utilizada por um ou mais lançamentos.");
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
