/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-10 下午5:52:43
 */
package com.absir.appserv.crud;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.jdbc.JdbcPage;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.helper.HelperQuery;
import com.absir.core.base.IBase;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.FilterTemplate;
import com.absir.core.kernel.KernelList;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class CrudSupply implements ICrudSupply {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#getTransactionName()
	 */
	@Override
	public String getTransactionName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudSupply#support(com.absir.appserv.system.bean
	 * .value.JaCrud.Crud)
	 */
	@Override
	public boolean support(Crud crud) {
		// TODO Auto-generated method stub
		return crud == Crud.UPDATE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudSupply#getIdentifierName(java.lang.String)
	 */
	@Override
	public String getIdentifierName(String entityName) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudSupply#getIdentifierType(java.lang.String)
	 */
	@Override
	public Class getIdentifierType(String entityName) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#getIdentifier(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public Object getIdentifier(String entityName, Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#find(java.lang.String,
	 * java.lang.Object, com.absir.appserv.jdbc.JdbcCondition)
	 */
	@Override
	public Object get(String entityName, Serializable id, JdbcCondition jdbcCondition) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param entityName
	 * @return
	 */
	public Collection findAll(String entityName) {
		return null;
	}

	/**
	 * @param entityName
	 * @param jdbcCondition
	 * @param queue
	 * @return
	 */
	private List list(String entityName, JdbcCondition jdbcCondition, String queue) {
		// TODO Auto-generated method stub
		Collection entities = findAll(entityName);
		if (entities == null) {
			return null;
		}

		Class<?> entityClass = getEntityClass(entityName);
		FilterTemplate<Object> filterTemplate = null;
		if (jdbcCondition != null) {
			List<Object> conditions = jdbcCondition.getConditionList();
			int size = conditions.size();
			if (size > 0) {
				final FilterTemplate filterQuery = HelperQuery.getConditionFilter(entityClass, conditions);
				filterTemplate = new FilterTemplate<Object>() {

					@Override
					public boolean doWith(Object template) throws BreakException {
						// TODO Auto-generated method stub
						return template != null && ((IBase) template).getId() != null && filterQuery.doWith(template);
					}
				};
			}
		}

		return KernelList.getFilterSortList(entities, filterTemplate, HelperQuery.getComparator(entityClass, queue));
	}

	/**
	 * @param xlsBases
	 * @param firstResult
	 * @param maxResults
	 */
	private List list(List xlsBases, int firstResult, int maxResults) {
		int size = xlsBases.size();
		if (firstResult < 0 || firstResult >= size) {
			firstResult = 0;
		}

		maxResults += firstResult;
		if (maxResults < 0 || maxResults > size) {
			maxResults = size;
		}

		return firstResult == 0 && maxResults == 0 ? xlsBases : xlsBases.subList(firstResult, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#list(java.lang.String,
	 * com.absir.appserv.jdbc.JdbcCondition, java.lang.String, int, int)
	 */
	@Override
	public List list(String entityName, JdbcCondition jdbcCondition, String queue, int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return list(list(entityName, jdbcCondition, queue), firstResult, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#list(java.lang.String,
	 * com.absir.appserv.jdbc.JdbcCondition, java.lang.String,
	 * com.absir.appserv.jdbc.JdbcPage)
	 */
	@Override
	public List list(String entityName, JdbcCondition jdbcCondition, String queue, JdbcPage jdbcPage) {
		// TODO Auto-generated method stub
		List entities = list(entityName, jdbcCondition, queue);
		if (entities == null) {
			return null;
		}

		jdbcPage.setTotalCount(entities.size());
		return list(entities, jdbcPage.getFirstResult(), jdbcPage.getPageSize());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#deleteEntity(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void deleteEntity(String entityName, Object entity) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#evict(java.lang.Object)
	 */
	@Override
	public void evict(Object entity) {
		// TODO Auto-generated method stub
	}
}
