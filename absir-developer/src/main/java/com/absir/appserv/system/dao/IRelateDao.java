/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-7-22 下午7:28:15
 */
package com.absir.appserv.system.dao;

import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.jdbc.JdbcCondition.Conditions;
import com.absir.appserv.system.bean.proxy.JiUserBase;

/**
 * @author absir
 * 
 */
public interface IRelateDao {

	/**
	 * 处理关联关系
	 * 
	 * @param rootEntityName
	 * @param user
	 * @param strategies
	 * @param joinAlias
	 * @param entityId
	 * @param jdbcCondition
	 * @param includeConditions
	 * @param excludeConditions
	 * @param joinConditions
	 */
	public void relateConditions(String rootEntityName, JiUserBase user, Object strategies, String joinAlias, String entityId, JdbcCondition jdbcCondition, Conditions includeConditions,
			Conditions excludeConditions, Conditions joinConditions);

}
