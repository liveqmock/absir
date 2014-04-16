/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-11 下午3:38:20
 */
package com.absir.appserv.system.service.statics;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

import com.absir.appserv.dyna.DynaBinderUtils;
import com.absir.appserv.support.Developer;
import com.absir.appserv.system.crud.UploadCrudFactory;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.service.utils.AccessServiceUtils;
import com.absir.appserv.system.service.utils.CrudServiceUtils;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelCharset;
import com.absir.core.kernel.KernelCollection;
import com.absir.core.util.UtilRuntime;
import com.absir.server.in.IAttributes;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EntityStatics {

	/**
	 * @param entity
	 * @param property
	 * @return
	 */
	public static String getPrimary(Object entity, String primary) {
		return DynaBinderUtils.to(BeanDao.getIdentifier(entity), String.class);
	}

	/**
	 * @param entityName
	 * @param entity
	 * @param primary
	 * @return
	 */
	public static String urlPrimary(String entityName, Object entity, String primary) {
		primary = getPrimary(entity, primary);
		try {
			return primary == null ? null : URLEncoder.encode(primary, KernelCharset.DEFAULT.displayName());

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public static Object paramId(Object id) {
		if (id == null) {
			return null;
		}

		if (id instanceof Object[]) {
			Object[] ids = (Object[]) id;
			if (ids.length == 0) {
				return null;
			}

			id = ids[0];

		} else if (id instanceof Collection) {
			Collection<Object> ids = (Collection<Object>) id;
			if (ids.size() <= 0) {
				return null;
			}
		}

		return id;
	}

	/**
	 * @param entityName
	 * @param id
	 * @param input
	 * @return
	 */
	public static Object find(String entityName, Object id, IAttributes input) {
		if (id == null || (id = paramId(id)) == null) {
			return null;
		}

		String entityId = EntityStatics.class.getName() + "@" + entityName + "@" + id;
		Object entity = input.getAttribute(entityId);
		if (entity == null) {
			entity = CrudServiceUtils.find(entityName, id, null);
			input.setAttribute(entityId, entity);
		}

		return entity;
	}

	/**
	 * @param entityName
	 * @param ids
	 * @param input
	 * @return
	 */
	public static List<Object> list(String entityName, Object ids, IAttributes input) {
		if (ids == null) {
			return null;
		}

		return CrudServiceUtils.list(entityName, DynaBinder.to(ids, Object[].class), null);
	}

	/**
	 * @param entityName
	 * @param input
	 * @return
	 */
	public static List list(String entityName, IAttributes input) {
		String entitiesKey = EntityStatics.class.getName() + "-" + entityName + "@LIST";
		List entities = (List) input.getAttribute(entitiesKey);
		if (entities == null) {
			entities = CrudServiceUtils.list(entityName, null, null, 0, 0);
			input.setAttribute(entitiesKey, entities);
		}

		return entities;
	}

	/**
	 * @param input
	 */
	public static void searchConditionMap(IAttributes input) {
		Object searchConditionMap = input.getAttribute("searchConditionMap");
		if (searchConditionMap == null) {
			Object searchConditionList = input.getAttribute("searchConditionList");
			if (searchConditionList != null && searchConditionList instanceof Collection) {
				searchConditionMap = KernelCollection.toMap((Collection) searchConditionList);
				input.setAttribute("searchConditionMap", searchConditionMap);
			}
		}
	}

	/**
	 * @param entityName
	 * @return
	 */
	public static String suggest(String entityName) {
		return "SUGGEST@" + entityName;
	}

	/**
	 * @param entityName
	 * @param input
	 * @return
	 */
	public static List suggest(String entityName, IAttributes input) {
		String entitiesKey = EntityStatics.class.getName() + "-" + entityName + "@SUGGEST";
		List entities = (List) input.getAttribute(entitiesKey);
		if (entities == null) {
			entities = CrudServiceUtils.list(entityName, AccessServiceUtils.suggestCondition(entityName, null), null, 0, 0);
			input.setAttribute(entitiesKey, entities);
		}

		return entities;
	}

	/**
	 * @param entityName
	 * @param fieldName
	 * @return
	 */
	public static String getSharedRuntimeName(String entityName, String fieldName) {
		return UtilRuntime.getRuntimeName(EntityStatics.class, entityName + "-" + fieldName + "@SHARED");
	}

	/**
	 * @param entityName
	 * @param fieldName
	 * @param inpute
	 */
	public static Object getSharedObject(String entityName, String fieldName, IAttributes inpute) {
		return getSharedObject(getSharedRuntimeName(entityName, fieldName), inpute);
	}

	/**
	 * @param runtimeName
	 * @param attributes
	 * @return
	 */
	public static Object getSharedObject(String runtimeName, IAttributes attributes) {
		Object shared = attributes.getAttribute(runtimeName);
		if (shared == null) {
			shared = Developer.getRuntime(runtimeName);
			attributes.setAttribute(runtimeName, shared);
		}

		return shared;
	}

	/**
	 * @param entityName
	 * @param fieldName
	 * @param value
	 * @param attributes
	 */
	public static void setSharedObject(String runtimeName, Object value, IAttributes attributes) {
		attributes.setAttribute(runtimeName, value);
		Developer.setRuntime(runtimeName, value);
	}

	/**
	 * @param uploadPath
	 * @return
	 */
	public static String getUploadUrl(String uploadPath) {
		return UploadCrudFactory.getUploadUrl() + uploadPath;
	}
}
