/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-27 上午9:33:02
 */
package com.absir.appserv.system.dao;

import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.jdbc.JdbcCondition.Conditions;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.orm.value.JePermission;
import com.absir.orm.value.JiAssoc;

/**
 * @author absir
 * 
 */
public interface IAssocDao {

	/**
	 * 支持关联类型
	 * 
	 * @param assocClass
	 * @param rootEntityName
	 * @param user
	 * @param permission
	 * @return
	 */
	public boolean supportAssocClass(Class<? extends JiAssoc> assocClass, String rootEntityName, JiUserBase user, JePermission permission);

	/**
	 * 生成关联实体条件
	 * 
	 * @param rootEntityName
	 * @param user
	 * @param permission
	 * @param strategies
	 * @param jdbcCondition
	 * @param includeConditions
	 * @param excludeConditions
	 */
	public void assocConditions(String rootEntityName, JiUserBase user, JePermission permission, Object strategies, JdbcCondition jdbcCondition, Conditions includeConditions,
			Conditions excludeConditions);
}
