/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-23 下午12:55:09
 */
package com.absir.appserv.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.absir.appserv.crud.CrudUtils;
import com.absir.appserv.crud.ICrudSupply;
import com.absir.appserv.dyna.DynaBinderUtils;
import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.jdbc.JdbcPage;
import com.absir.appserv.system.bean.base.JbRecycleBase;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.proxy.JpRecycleBase;
import com.absir.appserv.system.bean.value.JaRecycle;
import com.absir.appserv.system.domain.DCondition;
import com.absir.appserv.system.service.utils.AccessServiceUtils;
import com.absir.appserv.system.service.utils.BeanServiceUtils;
import com.absir.appserv.system.service.utils.CrudServiceUtils;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.binder.BinderData;
import com.absir.binder.BinderResult;
import com.absir.binder.BinderUtils;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelLang.PropertyFilter;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.value.JoEntity;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Bean
public class EntityService {

	/**
	 * @param entityName
	 * @return
	 */
	public ICrudSupply getCrudSupply(String entityName) {
		// TODO Auto-generated method stub
		ICrudSupply crudSupply = CrudService.ME.getCrudSupply(entityName);
		if (crudSupply == null) {
			throw new ServerException(ServerStatus.NO_PARAM, entityName + " not exists!");
		}

		return crudSupply;
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param id
	 * @return
	 */
	public Object find(String entityName, ICrudSupply crudSupply, JiUserBase user, Object id) {
		return crudSupply.get(entityName, DynaBinder.to(AccessServiceUtils.selectCondition(entityName, user, null), crudSupply.getIdentifierType(entityName)),
				AccessServiceUtils.selectCondition(entityName, user, null));
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param condition
	 * @param ids
	 * @return
	 */
	public List list(String entityName, ICrudSupply crudSupply, JiUserBase user, DCondition condition, Object[] ids) {
		return crudSupply.list(entityName, AccessServiceUtils.selectCondition(entityName, user, condition, CrudServiceUtils.ids(entityName, ids, crudSupply, null)), null, 0, 0);
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param condition
	 * @param jdbcCondition
	 * @param queue
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List list(String entityName, ICrudSupply crudSupply, JiUserBase user, DCondition condition, JdbcCondition jdbcCondition, String queue, int firstResult, int maxResults) {
		return crudSupply.list(entityName, AccessServiceUtils.selectCondition(entityName, crudSupply.getEntityClass(entityName), user, condition, jdbcCondition), queue, firstResult, maxResults);
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param condition
	 * @param jdbcCondition
	 * @param queue
	 * @param jdbcPage
	 * @return
	 */
	public List list(String entityName, ICrudSupply crudSupply, JiUserBase user, DCondition condition, JdbcCondition jdbcCondition, String queue, JdbcPage jdbcPage) {
		return crudSupply.list(entityName, AccessServiceUtils.selectCondition(entityName, crudSupply.getEntityClass(entityName), user, condition, jdbcCondition), queue, jdbcPage);
	}

	/**
	 * @param entityName
	 * @param entityObject
	 * @param crudSupply
	 * @param user
	 * @param filter
	 * @return
	 */
	public Object persist(String entityName, Object entityObject, ICrudSupply crudSupply, JiUserBase user, PropertyFilter filter) {
		return merge(entityName, crudSupply.create(entityName), entityObject, crudSupply, true, user, filter);
	}

	/**
	 * @param entityName
	 * @param entity
	 * @param entityObject
	 * @param crudSupply
	 * @param create
	 * @param user
	 * @param filter
	 * @return
	 */
	public Object merge(String entityName, Object entity, Object entityObject, ICrudSupply crudSupply, boolean create, JiUserBase user, PropertyFilter filter) {
		if (entityObject == null || !(entityObject instanceof Map)) {
			if (entityObject.getClass() != crudSupply.getEntityClass(entityName)) {
				return null;
			}

			entityObject = BinderUtils.getEntityMap(entityObject);
		}

		Map<String, Object> crudRecord = create ? null : CrudUtils.crudRecord(new JoEntity(entityName, entity.getClass()), entity, filter);
		BinderData binderData = new BinderData();
		BinderResult binderResult = binderData.getBinderResult();
		binderResult.setValidation(true);
		binderResult.setPropertyFilter(filter);
		binderData.mapBind((Map) entityObject, entity);
		if (binderResult.hashErrors()) {
			throw new ServerException(ServerStatus.NO_PARAM, "bind entity " + entityName + " from " + entityObject + ", has errors = " + binderResult.getPropertyErrors());
		}

		CrudService.ME.merge(entityName, crudRecord, entity, crudSupply, create, user, filter);
		return entity;
	}

	/**
	 * @param entityName
	 * @param identifierName
	 * @param propertyMap
	 * @return
	 */
	private Object getIdentifier(String entityName, String identifierName, Map propertyMap) {
		Object id = propertyMap.get(identifierName);
		if (id == null) {
			throw new ServerException(ServerStatus.NO_PARAM, "can not find " + entityName + " from id = " + identifierName);
		}

		return id;
	}

	/**
	 * @param entityName
	 * @param id
	 * @param crudSupply
	 * @param jdbcCondition
	 * @return
	 */
	private Object find(String entityName, Object id, ICrudSupply crudSupply, JdbcCondition jdbcCondition) {
		Object entity = CrudServiceUtils.find(crudSupply, entityName, id, jdbcCondition);
		if (entity == null) {
			throw new ServerException(ServerStatus.NO_PARAM, "can not find " + entityName + " from id = " + id);
		}

		return entity;
	}

	/**
	 * @param entityName
	 * @param identifierName
	 * @param identifierType
	 * @param propertyMap
	 * @param crudSupply
	 * @param user
	 * @param filter
	 * @param jdbcCondition
	 * @return
	 */
	private Object update(String entityName, String identifierName, Class identifierType, Map propertyMap, ICrudSupply crudSupply, JiUserBase user, PropertyFilter filter, JdbcCondition jdbcCondition) {
		Object id = DynaBinderUtils.to(getIdentifier(entityName, identifierName, propertyMap), identifierType);
		if (id == null) {
			throw new ServerException(ServerStatus.NO_PARAM, "can not update " + entityName + " from id = " + identifierType);
		}

		Object entity = find(entityName, id, crudSupply, jdbcCondition);
		propertyMap.remove(identifierName);
		return merge(entityName, entity, propertyMap, crudSupply, false, user, filter);
	}

	/**
	 * @param entityName
	 * @param identifierName
	 * @param identifierType
	 * @param propertyMap
	 * @param crudSupply
	 * @param user
	 * @param filter
	 * @param jdbcCondition
	 * @return
	 */
	private Object merge(String entityName, String identifierName, Class identifierType, Map propertyMap, ICrudSupply crudSupply, JiUserBase user, PropertyFilter filter, JdbcCondition jdbcCondition) {
		try {
			Object id = DynaBinderUtils.to(getIdentifier(entityName, identifierName, propertyMap), identifierType);
			if (id != null) {
				Object entity = CrudServiceUtils.find(crudSupply, entityName, id, null);
				if (entity == null) {
					entity = BeanServiceUtils.similarEntity(entityName, propertyMap);
				}

				if (entity != null) {
					id = crudSupply.getIdentifier(entityName, entity);
					entity = CrudServiceUtils.find(crudSupply, entityName, id, jdbcCondition);
					if (entity != null) {
						return merge(entityName, entity, propertyMap, crudSupply, false, user, filter);
					}
				}

				return merge(entityName, crudSupply.create(entityName), propertyMap, crudSupply, false, user, filter);
			}

		} catch (ServerException e) {
			// TODO: handle exception
		}

		return persist(entityName, propertyMap, crudSupply, user, filter);
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param entityList
	 * @param filter
	 * @return
	 */
	public List insert(String entityName, ICrudSupply crudSupply, JiUserBase user, List<?> entityList, PropertyFilter filter) {
		List<Object> entities = new ArrayList<Object>();
		for (Object entityObject : entityList) {
			entities.add(persist(entityName, entityObject, crudSupply, user, filter));
		}

		return entities;
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param entityObject
	 * @param filter
	 * @return
	 */
	public Object update(String entityName, ICrudSupply crudSupply, JiUserBase user, Object entityObject, PropertyFilter filter) {
		if (entityObject == null || !(entityObject instanceof Map)) {
			if (entityObject.getClass() != crudSupply.getEntityClass(entityName)) {
				return null;
			}

			entityObject = BinderUtils.getEntityMap(entityObject);
		}

		return update(entityName, crudSupply.getIdentifierName(entityName), crudSupply.getIdentifierType(entityName), (Map) entityObject, crudSupply, user, filter,
				AccessServiceUtils.updateCondition(entityName, user, null));
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param entityList
	 * @param filter
	 * @return
	 */
	public List update(String entityName, ICrudSupply crudSupply, JiUserBase user, List<?> entityList, PropertyFilter filter) {
		Class entityClass = null;
		String identifierName = crudSupply.getIdentifierName(entityName);
		Class identifierType = crudSupply.getIdentifierType(entityName);
		JdbcCondition jdbcCondition = AccessServiceUtils.updateCondition(entityName, user, null);
		List<Object> conditions = jdbcCondition == null ? null : new ArrayList<Object>(jdbcCondition.getConditions());
		List<Object> entities = new ArrayList<Object>();
		for (Object entityObject : entityList) {
			if (entityObject == null || !(entityObject instanceof Map)) {
				if (entityClass == null) {
					entityClass = crudSupply.getEntityClass(entityName);
				}

				if (entityObject.getClass() != entityClass) {
					continue;
				}

				entityObject = BinderUtils.getEntityMap(entityObject);
			}

			entities.add(update(entityName, identifierName, identifierType, (Map) entityObject, crudSupply, user, filter, jdbcCondition));
			if (jdbcCondition != null) {
				jdbcCondition.setConditions(new ArrayList<Object>(conditions));
			}
		}

		return entities;
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param entityObject
	 * @param filter
	 * @return
	 */
	public Object merge(String entityName, ICrudSupply crudSupply, JiUserBase user, Object entityObject, PropertyFilter filter) {
		// TODO Auto-generated method stub
		if (entityObject == null || !(entityObject instanceof Map)) {
			if (entityObject.getClass() != crudSupply.getEntityClass(entityName)) {
				return null;
			}

			entityObject = BinderUtils.getEntityMap(entityObject);
		}

		return merge(entityName, crudSupply.getIdentifierName(entityName), crudSupply.getIdentifierType(entityName), (Map) entityObject, crudSupply, user, filter,
				AccessServiceUtils.updateCondition(entityName, user, null));
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param entityList
	 * @param filter
	 * @return
	 */
	public List merge(String entityName, ICrudSupply crudSupply, JiUserBase user, List<?> entityList, PropertyFilter filter) {
		Class entityClass = null;
		String identifierName = crudSupply.getIdentifierName(entityName);
		Class identifierType = crudSupply.getIdentifierType(entityName);
		JdbcCondition jdbcCondition = AccessServiceUtils.updateCondition(entityName, user, null);
		List<Object> conditions = jdbcCondition == null ? null : new ArrayList<Object>(jdbcCondition.getConditions());
		List<Object> entities = new ArrayList<Object>();
		for (Object entityObject : entityList) {
			if (entityObject == null || !(entityObject instanceof Map)) {
				if (entityClass == null) {
					entityClass = crudSupply.getEntityClass(entityName);
				}

				if (entityObject.getClass() != entityClass) {
					continue;
				}

				entityObject = BinderUtils.getEntityMap(entityObject);
			}

			entities.add(merge(entityName, identifierName, identifierType, (Map) entityObject, crudSupply, user, filter, jdbcCondition));
			if (jdbcCondition != null) {
				jdbcCondition.setConditions(new ArrayList<Object>(conditions));
			}
		}

		return entities;
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param id
	 * @return
	 */
	public Object delete(String entityName, ICrudSupply crudSupply, JiUserBase user, Object id) {
		return CrudServiceUtils.delete(crudSupply, entityName, id, AccessServiceUtils.deleteCondition(entityName, user, null), user);
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param ids
	 * @return
	 */
	public List delete(String entityName, ICrudSupply crudSupply, JiUserBase user, Object[] ids) {
		return CrudService.ME.delete(entityName, getCrudSupply(entityName), AccessServiceUtils.deleteCondition(entityName, user, CrudServiceUtils.ids(entityName, ids, crudSupply, null)), user);
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param jdbcCondition
	 * @return
	 */
	public List delete(String entityName, ICrudSupply crudSupply, JiUserBase user, JdbcCondition jdbcCondition) {
		return CrudService.ME.delete(entityName, crudSupply, AccessServiceUtils.deleteCondition(entityName, user, jdbcCondition), user);
	}

	/** entityNameMapRecycle */
	private Map<String, Boolean> entityNameMapRecycle = new HashMap<String, Boolean>();
	/** ME */
	public static final EntityService ME = BeanFactoryUtils.get(EntityService.class);

	/**
	 * @param entityName
	 * @return
	 */
	public boolean getEntityNameRecycle(String entityName) {
		Boolean recyle = entityNameMapRecycle.get(entityName);
		if (recyle == null) {
			synchronized (entityNameMapRecycle) {
				recyle = entityNameMapRecycle.get(entityName);
				if (recyle == null) {
					Class<?> recycleClass = SessionFactoryUtils.getEntityClass(entityName + JpRecycleBase.RECYCLE);
					if (recycleClass == null || JbRecycleBase.class.isAssignableFrom(JpRecycleBase.class)) {
						recyle = false;

					} else {
						Class<?> entityClass = SessionFactoryUtils.getEntityClass(entityName);
						JaRecycle jaRecycle = entityClass == null ? null : KernelClass.fetchAnnotation(entityClass, JaRecycle.class);
						recyle = !(jaRecycle == null || !jaRecycle.value());
					}

					entityNameMapRecycle.put(entityName, recyle);
				}
			}
		}

		return recyle;
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param condition
	 * @param modelMap
	 */
	public void changed(String entityName, ICrudSupply crudSupply, JiUserBase user, DCondition condition, Map<String, Object> modelMap) {
		if (!modelMap.containsKey("updateTime")) {
			modelMap.put("updateTime", System.currentTimeMillis());
		}

		// KernelString.capitalize(entityName)
		modelMap.put(entityName, list(entityName, crudSupply, user, condition, null, null, 0, 0));
		if (getEntityNameRecycle(entityName)) {
			entityName += JpRecycleBase.RECYCLE;
			List recycles = list(entityName, getCrudSupply(entityName), user, condition, null, null, 0, 0);
			if (recycles.size() > 0 && recycles.get(0).getClass().getSuperclass() != JbRecycleBase.class) {
				List<JbRecycleBase> recycleBases = recycles;
				recycles = new ArrayList<JbRecycleBase>();
				for (JbRecycleBase recycleBase : recycleBases) {
					JbRecycleBase recycle = new JbRecycleBase();
					recycle.setId(recycleBase.getId());
					recycle.setUpdateTime(recycle.getUpdateTime());
					recycles.add(recycle);
				}
			}

			modelMap.put(entityName, recycles);
		}
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param condition
	 * @param modelMap
	 * @param entityMap
	 * @param filter
	 */
	public void sync(String entityName, ICrudSupply crudSupply, JiUserBase user, DCondition condition, Map<String, Object> modelMap, Map<?, ?> entityMap, PropertyFilter filter) {
		Object entityList = entityMap.get("delete");
		if (entityList != null && entityList instanceof List) {
			delete(entityName, crudSupply, user, (List<?>) entityList);
		}

		entityList = entityMap.get("merge");
		if (entityList != null && entityList instanceof List) {
			merge(entityName, crudSupply, user, (List<?>) entityList, filter);
		}

		entityList = entityMap.get("insert");
		if (entityList != null && entityList instanceof List) {
			insert(entityName, crudSupply, user, (List<?>) entityList, filter);
		}

		entityList = entityMap.get("update");
		if (entityList != null && entityList instanceof List) {
			update(entityName, crudSupply, user, (List<?>) entityList, filter);
		}

		changed(entityName, crudSupply, user, condition, modelMap);
	}

	/**
	 * @param entityName
	 * @param crudSupply
	 * @param user
	 * @param modelMap
	 * @param entityList
	 * @param filter
	 */
	public void mirror(String entityName, ICrudSupply crudSupply, JiUserBase user, Map<String, Object> modelMap, List<?> entityList, PropertyFilter filter) {
		CrudService.ME.delete(entityName, crudSupply, AccessServiceUtils.deleteCondition(entityName, user, null), user);
		insert(entityName, crudSupply, user, entityList, filter);
	}
}
