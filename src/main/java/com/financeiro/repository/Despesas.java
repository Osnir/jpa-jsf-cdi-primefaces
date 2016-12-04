package com.financeiro.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.financeiro.dao.BaseDao;
import com.financeiro.dao.exception.DataAccessException;
import com.financeiro.model.Categoria;
import com.financeiro.model.Despesa;
import com.financeiro.model.Situacao;
import com.financeiro.util.Util;

public class Despesas extends BaseDao<Despesa> {

	private static final long serialVersionUID = 1L;

	@Inject
	public Despesas(EntityManager manager) {
		super(manager);
	}
	
	public Despesa salvar(Despesa despesa) throws DataAccessException {
		return this.create(despesa);
	}
	
	public void excluir(Despesa despesa) throws DataAccessException {
		this.delete(despesa);
	}
	
	public Despesa porId(Long id) throws DataAccessException  {
		return this.find(id);
	}
	
	public List<Despesa> todas() throws DataAccessException {
		String hql = "from Despesa d join fetch d.categoria";
		return this.executeHqlQuery(hql);
	}
	
	public Despesa porNome(String nome) throws DataAccessException {
		List<Despesa> lista = this.porParteNome(nome);

		if (!lista.isEmpty() && lista.size() == 1) {
			return lista.get(0);
		}

		return null;
	}

	public List<Despesa> porParteNome(String nome) throws DataAccessException {
		StringBuilder hql = new StringBuilder("from Despesa where upper(nome) like :nome and situacao = 'Ativo'");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nome", "%" + nome.toUpperCase() + "%");

		return this.executeHqlQuery(hql.toString(), params);
	}
	
	public List<Despesa> filtrar(String filtro) throws DataAccessException {
		StringBuilder hql = new StringBuilder("from Despesa d");
		Map<String, Object> params = new HashMap<String, Object>();

		if (!Util.isEmptyOrNull(filtro)) {
			filtro = filtro.toUpperCase();

			hql.append(" where upper(d.nome) like :nome");
			params.put("nome", "%" + filtro + "%");

			hql.append(" or upper(d.categoria.nome) like :categoria");
			params.put("categoria", "%" + filtro + "%");
			
			if (Util.isNumber(filtro)) {
				hql.append(" or d.id = :id");
				params.put("id", Long.parseLong(filtro));
			}

			if (filtro.equalsIgnoreCase("Ativo") || filtro.equalsIgnoreCase("Inativo")) {
				hql.append(" or d.situacao = :situacao");
				params.put("situacao", Situacao.valueOf(filtro));
			}
		}

		return this.executeHqlQuery(hql.toString(), params);
	}
	
	public List<Despesa> porCategoria(Categoria categoria) throws DataAccessException {
		String hql = "from Despesa d where d.categoria = :categoria";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("categoria", categoria);
		
		return this.executeHqlQuery(hql, params); 
	}
}
