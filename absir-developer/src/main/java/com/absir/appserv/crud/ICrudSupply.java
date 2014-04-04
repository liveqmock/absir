/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-6 下午3:27:50
 */
package com.absir.appserv.crud;

import java.io.Serializable;
import java.util.List;

import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.jdbc.JdbcPage;
import com.absir.appserv.system.bean.value.JaCrud.Crud;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public interface ICrudSupply {

	/**
	 * @return
	 */
	public String getTransactionName();

	/**
	 * @param crud
	 * @return
	 */
	public boolean support(Crud crud);

	/**
	 * @param entityName
	 */
	public Class<?> getEntityClass(String entityName);

	/**
	 * @param entityName
	 * @return
	 */
	public String getIdentifierName(String entityName);

	/**
	 * @param entityName
	 * @return
	 */
	public Class<? extends Serializable> getIdentifierType(String entityName);

	/**
	 * @param entityName
	 * @param entity
	 * @return
	 */
	public Object getIdentifier(String entityName, Object entity);

	/**
	 * @param entityName
	 * @param id
	 * @return
	 */
	public Object get(String entityName, Serializable id, JdbcCondition jdbcCondition);

	/**
	 * @param entityName
	 * @param jdbcCondition
	 * @param queue
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List list(String entityName, JdbcCondition jdbcCondition, String queue, int firstResult, int maxResults);

	/**
	 * @param entityName
	 * @param jdbcCondition
	 * @param queue
	 * @param jdbcPage
	 * @return
	 */
	public List list(String entityName, JdbcCondition jdbcCondition, String queue, JdbcPage jdbcPage);

	/**
	 * @param entityName
	 * @return
	 */
	public Object create(String entityName);

	/**
	 * @param entityName
	 * @param entity
	 * @param create
	 */
	public void mergeEntity(String entityName, Object entity, boolean create);

	/**
	 * @param entityName
	 * @param entityObject
	 * @return
	 */
	public void deleteEntity(String entityName, Object entity);

	/**
	 * @param entity
	 */
	public void evict(Object entity);
}
