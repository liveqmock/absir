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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.jdbc.JdbcPage;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaName;
import com.absir.appserv.system.helper.HelperQuery;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.config.IBeanDefineSupply;
import com.absir.bean.core.BeanFactoryImpl;
import com.absir.core.base.IBase;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelLang;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.FilterTemplate;
import com.absir.core.kernel.KernelList;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.value.JaEntity;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class CrudSupply<T> implements ICrudSupply, IBeanDefineSupply {

	/** entityNameMapClass */
	protected Map<String, Class<? extends T>> entityNameMapClass = new HashMap<String, Class<? extends T>>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#getEntityNameMapClass()
	 */
	@Override
	public Set<Entry<String, Class<?>>> getEntityNameMapClass() {
		// TODO Auto-generated method stub
		return (Set<Entry<String, Class<?>>>) (Object) entityNameMapClass.entrySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 32;
	}

	/**
	 * @param type
	 * @param beanType
	 */
	protected void put(Class<?> type, Class<?> beanType) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.config.IBeanDefineSupply#getBeanDefines(com.absir.bean
	 * .core.BeanFactoryImpl, java.lang.Class)
	 */
	@Override
	public List<BeanDefine> getBeanDefines(BeanFactoryImpl beanFactory, Class<?> beanType) {
		// TODO Auto-generated method stub
		Class<?> supplyClass = KernelClass.componentClass(getClass());
		if (supplyClass.isAssignableFrom(beanType)) {
			JaEntity jaEntity = beanType.getAnnotation(JaEntity.class);
			if (jaEntity != null || beanType.getAnnotation(MaEntity.class) != null) {
				JaName jaName = beanType.getAnnotation(JaName.class);
				String entityName = jaName == null ? beanType.getSimpleName() : jaName.value();
				Class<?> type = entityNameMapClass.get(entityName);
				if (type == null || beanType.isAssignableFrom(type)) {
					entityNameMapClass.put(entityName, (Class<? extends T>) beanType);
					if (type != null) {
						put(type, beanType);
					}

				} else if (type.isAssignableFrom(beanType)) {
					put(beanType, type);
					if (SessionFactoryUtils.get().getNameMapPermissions().get(entityName) != null) {
						jaEntity = null;
					}

				} else {
					jaEntity = null;
				}

				if (jaEntity != null && jaEntity.permissions().length > 0) {
					SessionFactoryUtils.get().getNameMapPermissions().put(entityName, jaEntity.permissions());
				}

				return KernelLang.NULL_LIST_SET;
			}
		}

		return null;
	}

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
		return crud != Crud.COMPLETE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#getEntityClass(java.lang.String)
	 */
	@Override
	public Class<? extends T> getEntityClass(String entityName) {
		// TODO Auto-generated method stub
		return entityNameMapClass.get(entityName);
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
