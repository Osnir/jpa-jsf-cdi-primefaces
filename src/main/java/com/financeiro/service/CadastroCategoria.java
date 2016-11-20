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

public class CadastroCategoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Categorias categorias;
	@Inject
	private Despesas despesas;
	
	@Transactional
	public Categoria Salvar(Categoria categoria) throws NegocioException, DataAccessException {
		if (this.CategoriaExistente(categoria)) {
			throw new NegocioException("Já existe uma categoria cadastrada com esse nome.");
		}
		
		return this.categorias.salvar(categoria);
	}
	
	@Transactional
	public void excluir(Categoria categoria) throws NegocioException, DataAccessException{
		categoria = this.categorias.porId(categoria.getId());
		
		if (this.CategoriaUtilizada(categoria)) {
			throw new NegocioException("Esta categoria está sendo utilizada por uma ou mais despesas.");
		}
		
		this.categorias.excluir(categoria);
	}
	
	private boolean CategoriaExistente(Categoria categoria) throws DataAccessException{
		Categoria existente = this.categorias.porNome(categoria.getNome());		
		return existente != null && existente.getId() != categoria.getId();
	}
	
	private boolean CategoriaUtilizada(Categoria categoria) throws DataAccessException {
		List<Despesa> usadas = this.despesas.porCategoria(categoria);
		return !usadas.isEmpty();
	}
}
