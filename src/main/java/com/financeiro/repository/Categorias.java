package com.financeiro.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.financeiro.dao.BaseDao;
import com.financeiro.dao.exception.DataAccessException;
import com.financeiro.model.Categoria;
import com.financeiro.model.Situacao;
import com.financeiro.util.Util;

public class Categorias extends BaseDao<Categoria> {

	private static final long serialVersionUID = 1L;

	@Inject
	public Categorias(EntityManager manager) {
		super(manager);
	}

	public Categoria salvar(Categoria categoria) throws DataAccessException {
		return this.create(categoria);
	}

	public void excluir(Categoria categoria) throws DataAccessException{
		this.delete(categoria);
	}

	public Categoria porId(Long id) throws DataAccessException {
		return this.find(id);
	}
	
	public List<Categoria> todas() throws DataAccessException {
		return this.findAll();
	}
	
	public Categoria porNome(String nome) throws DataAccessException {
		List<Categoria> lista = this.porParteNome(nome);

		if (!lista.isEmpty() && lista.size() == 1) {
			return lista.get(0);
		}

		return null;
	}
	
	public List<Categoria> porParteNome(String nome) throws DataAccessException {
		StringBuilder hql = new StringBuilder("from Categoria where upper(nome) like :nome");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nome", "%" + nome.toUpperCase() + "%");

		return this.executeHqlQuery(hql.toString(), params);
	}

	public List<Categoria> filtrar(String filtro) throws DataAccessException {
		StringBuilder hql = new StringBuilder("from Categoria");
		Map<String, Object> params = new HashMap<String, Object>();

		if (!Util.isEmptyOrNull(filtro)) {
			filtro = filtro.toUpperCase();

			hql.append(" where upper(nome) like :nome");
			params.put("nome", "%" + filtro + "%");

			if (Util.isNumber(filtro)) {
				hql.append(" or id = :id");
				params.put("id", Long.parseLong(filtro));
			}

			if (filtro.equalsIgnoreCase("Ativo") || filtro.equalsIgnoreCase("Inativo")) {
				hql.append(" or situacao = :situacao");
				params.put("situacao", Situacao.valueOf(filtro));
			}
		}

		return this.executeHqlQuery(hql.toString(), params);
	}
}
