package com.financeiro.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.financeiro.dao.exception.DataAccessException;
import com.financeiro.model.Categoria;
import com.financeiro.model.Despesa;
import com.financeiro.repository.Categorias;
import com.financeiro.repository.Despesas;
import com.financeiro.util.Transactional;
import com.financeiro.util.Util;

public class CategoriaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Categorias categorias;
	@Inject
	private Despesas despesas;
	
	@Transactional
	public Categoria Salvar(Categoria categoria) throws BusinessException, DataAccessException {
		if (Util.isEmptyOrNull(categoria.getNome())) {
			throw new BusinessException("Categoria deve ser informado.");
		}
		if (this.categoriaExistente(categoria)) {
			throw new BusinessException("Já existe uma categoria cadastrada com esse nome.");
		}
		
		return this.categorias.salvar(categoria);
	}
	
	@Transactional
	public void excluir(Categoria categoria) throws BusinessException, DataAccessException{
		categoria = this.categorias.porId(categoria.getId());
		
		if (this.categoriaUtilizada(categoria)) {
			throw new BusinessException("Esta categoria está sendo utilizada por uma ou mais despesas.");
		}
		
		this.categorias.excluir(categoria);
	}
	
	private boolean categoriaExistente(Categoria categoria) throws DataAccessException{
		Categoria existente = this.categorias.porNome(categoria.getNome());		
		return existente != null && existente.getId() != categoria.getId();
	}
	
	private boolean categoriaUtilizada(Categoria categoria) throws DataAccessException {
		List<Despesa> usadas = this.despesas.porCategoria(categoria);
		return !usadas.isEmpty();
	}
}
