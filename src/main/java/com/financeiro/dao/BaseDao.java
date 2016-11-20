package com.financeiro.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;

import org.hibernate.exception.ConstraintViolationException;

import com.financeiro.dao.exception.DataAccessException;

public abstract class BaseDao<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<T> persistentClass;

	protected EntityManager manager;

	@SuppressWarnings("unchecked")
	public BaseDao(EntityManager manager) {
		this.manager = manager;
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public Class<T> getPersistentClass() {
		return this.persistentClass;
	}

	public T create(T entity) throws DataAccessException {
		try {
			entity = this.manager.merge(entity);
			return entity;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	public T update(T entity) throws DataAccessException {
		try {
			entity = this.manager.merge(entity);
			return entity;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	public void delete(T entity) throws DataAccessException {
		try {
			entity = this.manager.merge(entity);
			this.manager.remove(entity);
		} catch (ConstraintViolationException e) {
			throw new DataAccessException("Registro não pode ser excluído, pois está sendo utilizado.", e);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	public T find(Serializable id) throws DataAccessException {
		try {
			return this.manager.find(persistentClass, id);
		} catch (EntityNotFoundException e) {
			return null;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	public List<T> findAll() throws DataAccessException {
		return this.executeHqlQuery("from " + this.persistentClass.getSimpleName());
	}

	public List<T> executeHqlQuery(String hql) throws DataAccessException {
		return this.executeHqlQuery(hql, null);
	}

	public List<T> executeHqlQuery(String hql, Map<String, Object> params) throws DataAccessException {
		return this.executeHqlQuery(hql, params, 0, 0);
	}

	public List<T> executeHqlQuery(String hql, Map<String, Object> params, int firstResult, int resultLimit)
			throws DataAccessException {
		try {
			TypedQuery<T> query = this.manager.createQuery(hql, this.persistentClass);

			this.setNavigation(query, firstResult, resultLimit);
			this.setParameters(query, params);

			return query.getResultList();
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	public void setParameters(TypedQuery<T> query, Map<String, Object> params) {
		if (((params != null) && !params.isEmpty())) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
	}

	public void setNavigation(TypedQuery<T> query, int firstResult, int resultLimit) {
		if (firstResult > 0) {
			query.setFirstResult(firstResult);
		}
		if (resultLimit > 0) {
			query.setMaxResults(resultLimit);
		}
	}
}
