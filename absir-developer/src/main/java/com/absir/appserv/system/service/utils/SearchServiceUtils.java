/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-6 下午5:44:23
 */
package com.absir.appserv.system.service.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.absir.appserv.dyna.DynaBinderUtils;
import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.system.helper.HelperCondition;
import com.absir.appserv.system.service.BeanService;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelCollection;
import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelLang;
import com.absir.core.kernel.KernelLang.PropertyFilter;
import com.absir.core.kernel.KernelString;
import com.absir.orm.hibernate.SessionFactoryUtils;

/**
 * @author absir
 * 
 */
public abstract class SearchServiceUtils {

	/**
	 * 获取查询条件
	 * 
	 * @param entityName
	 * @param filter
	 * @param conditions
	 * @param jdbcCondition
	 * @return
	 */
	public static JdbcCondition getSearchCondition(String entityName, PropertyFilter filter, List<Object> conditions, JdbcCondition jdbcCondition) {
		// TODO Auto-generated method stub
		if (jdbcCondition == null) {
			jdbcCondition = new JdbcCondition();
		}

		Map<String, Object[]> fieldMetas = SessionFactoryUtils.getEntityFieldMetas(entityName, null);
		int last = conditions.size() - 1;
		if (last > 0) {
			List<List<Object>> metasConditions = new ArrayList<List<Object>>();
			for (int i = 0; i < last; i += 2) {
				addSearchMetasCondition(filter, String.valueOf(conditions.get(i)), conditions.get(i + 1), fieldMetas, metasConditions);
			}

			addSearchJdbcCondition(metasConditions, jdbcCondition);
		}

		return jdbcCondition;
	}

	/**
	 * 添加查询条件
	 * 
	 * @param filter
	 * @param propertyExpression
	 * @param propertyValue
	 * @param fieldMetas
	 * @param metasConditions
	 */
	public static void addSearchMetasCondition(PropertyFilter filter, String propertyExpression, Object propertyValue, Map<String, Object[]> fieldMetas, List<List<Object>> metasConditions) {
		if (propertyValue != null && propertyValue.getClass().isArray() && Array.getLength(propertyValue) == 0) {
			return;
		}

		if (filter != null) {
			filter.begin();
		}

		int expressionIndex = propertyExpression.indexOf(' ');
		boolean expression = expressionIndex > 0;
		String[] propertyNames = (expression ? KernelString.leftString(propertyExpression, expressionIndex) : propertyExpression).split("\\.");
		int last = propertyNames.length - 1;
		if (last < 0) {
			return;
		}

		List<Object> metasCondition = new ArrayList<Object>();
		for (int i = 0; i <= last; i++) {
			// just realize aop locale there
			String propertyName = propertyNames[i];
			if (!(filter == null || filter.isMatch(propertyName))) {
				return;
			}

			Object[] metas = fieldMetas.get(propertyName);
			if (metas == null) {
				return;
			}

			if (metas.length == 2 && SessionFactoryUtils.getEntityFieldMetas((String) metas[1], null) == null) {
				return;
			}

			if (i == last) {
				// could find langs property?
				if (expression) {
					propertyName += HelperCondition.expression(KernelString.leftSubString(propertyExpression, expressionIndex));
				}

				metasCondition.add(expression);
				metasCondition.add(propertyName);
				metasCondition.add(metas);
				metasCondition.add(propertyValue);
				break;
			}

			// ready add JLocale relateId
			metasCondition.add(propertyName);
			metasCondition.add(metas);
		}

		metasConditions.add(metasCondition);
	}

	/**
	 * 处理查询条件
	 * 
	 * @param metasConditions
	 * @param jdbcCondition
	 */
	public static void addSearchJdbcCondition(List<List<Object>> metasConditions, JdbcCondition jdbcCondition) {
		for (List<Object> metasCondition : metasConditions) {
			int last = metasCondition.size() - 4;
			jdbcCondition.pushAlias();
			for (int i = 0; i < last; i += 2) {
				Object[] metas = (Object[]) metasCondition.get(i + 1);
				if (metas.length == 2) {
					jdbcCondition.joinAliasProperyName(jdbcCondition.getAlias(), (String) metasCondition.get(i));
				}
			}

			addSearchJdbcCondition((Boolean) metasCondition.get(last), (String) metasCondition.get(last + 1), (Object[]) metasCondition.get(last + 2), metasCondition.get(last + 3), jdbcCondition);
			jdbcCondition.popAlias();
		}
	}

	/**
	 * 生成查询条件
	 * 
	 * @param expression
	 * @param propertyName
	 * @param metas
	 * @param propertyValue
	 * @param jdbcCondition
	 */
	public static Object addSearchJdbcCondition(boolean expression, String propertyName, Object[] metas, Object propertyValue, JdbcCondition jdbcCondition) {
		String alias = jdbcCondition.getAlias();
		List<Object> searchConditions = jdbcCondition.getConditions();
		Class<?> fieldType = (Class<?>) metas[0];
		if (propertyValue == null || propertyValue == KernelLang.NULL_OBJECT) {
			if (expression) {
				searchConditions.add(alias + '.' + propertyName);

			} else {
				searchConditions.add(alias + '.' + propertyName + " IS NULL");
			}

			searchConditions.add(KernelLang.NULL_OBJECT);

		} else if (String.class.isAssignableFrom(fieldType)) {
			propertyValue = KernelDyna.to(propertyValue instanceof Object[] ? ((Object[]) propertyValue)[0] : propertyValue, String.class);
			searchConditions.add(alias + '.' + (expression ? propertyName : (propertyName + " LIKE ?")));
			if (expression && propertyName.indexOf('=') > 0) {
				searchConditions.add(propertyValue);

			} else {
				searchConditions.add("%" + propertyValue + '%');
			}

		} else if (expression || metas.length == 1) {
			propertyValue = DynaBinderUtils.to(propertyValue instanceof Object[] ? ((Object[]) propertyValue)[0] : propertyValue, fieldType);
			searchConditions.add(alias + '.' + propertyName);
			searchConditions.add(propertyValue);

		} else {
			if (metas.length == 2) {
				String entityName = (String) metas[1];
				if (propertyValue instanceof String) {
					propertyValue = ((String) propertyValue).split(",");

				} else if (propertyValue instanceof Collection) {
					propertyValue = KernelCollection.toArray((Collection<?>) propertyValue, Object.class);

				} else if (propertyValue.getClass().isArray() && !Object.class.isAssignableFrom(propertyValue.getClass().getComponentType())) {
					propertyValue = DynaBinder.to(propertyValue, Object[].class);
				}

				if (propertyValue.getClass().isArray()) {
					if (Collection.class.isAssignableFrom(fieldType)) {
						jdbcCondition.pushAlias();
						searchConditions.add(jdbcCondition.joinProperyName(propertyName) + '.' + SessionFactoryUtils.getIdentifierName(entityName, null, null) + " IN (?)");
						jdbcCondition.popAlias();

					} else {
						searchConditions.add(alias + '.' + propertyName + '.' + SessionFactoryUtils.getIdentifierName(entityName, null, null) + " IN (?)");
					}

					searchConditions.add(BeanService.ME.getSearchIds(entityName, (Object[]) propertyValue).toArray());
				}
			}
		}

		return propertyValue;
	}
}
