package com.financeiro.repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.financeiro.dao.BaseDao;
import com.financeiro.dao.exception.DataAccessException;
import com.financeiro.model.Despesa;
import com.financeiro.model.Lancamento;
import com.financeiro.model.TipoLancamento;
import com.financeiro.util.Util;

public class Lancamentos extends BaseDao<Lancamento> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	public Lancamentos(EntityManager manager) {
		super(manager);
	}
	
	public Lancamento salvar(Lancamento lancamento) throws DataAccessException {
		return this.create(lancamento);	
	}
	
	public void excluir(Lancamento lancamento) throws DataAccessException {
		this.delete(lancamento);	
	}

	public Lancamento porId(Long id) throws DataAccessException {
		return this.find(id);
	}

	public List<Lancamento> todos() throws DataAccessException {
		String hql = "from Lancamento l join fetch l.despesa";
		return this.executeHqlQuery(hql);
	}
	
	public List<Lancamento> filtrar(String filtro) throws DataAccessException, ParseException {
		StringBuilder hql = new StringBuilder("from Lancamento l");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if (!Util.isEmptyOrNull(filtro)) {
			filtro = filtro.toUpperCase();

			hql.append(" where upper(l.despesa.nome) like :despesa");
			params.put("despesa", "%" + filtro + "%");
		
			if (Util.isNumber(filtro)) {
				hql.append(" or l.id = :id");
				params.put("id", Long.parseLong(filtro));
			}
			
			if (Util.isDecimal(filtro)) {
				hql.append(" or l.valor = :valor");
				params.put("valor", new BigDecimal(filtro.replace(',', '.')));				
			}
			
			if (Util.isDate(filtro)) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date data = format.parse(filtro);			
				
				hql.append(" or l.dataVencimento = :dataVencimento");
				params.put("dataVencimento", data);

				hql.append(" or l.dataPagamento = :dataPagamento");
				params.put("dataPagamento", data);
			}

			if (filtro.equalsIgnoreCase("Despesa") || filtro.equalsIgnoreCase("Receita")) {
				hql.append(" or l.tipo = :tipo");
				params.put("tipo", TipoLancamento.valueOf(filtro));
			}
		}

		return this.executeHqlQuery(hql.toString(), params);
	}
	
	public List<Lancamento> porDespesa(Despesa despesa) throws DataAccessException {
		String hql = "from Lancamento d where d.despesa = :despesa";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("despesa", despesa);
		
		return this.executeHqlQuery(hql, params); 
	}
}
