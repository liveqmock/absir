/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-7-22 下午7:37:26
 */
package com.absir.appserv.system.dao.hibernate;

import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.jdbc.JdbcCondition.Conditions;
import com.absir.appserv.system.bean.base.JbRelation;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.dao.IRelateDao;

/**
 * @author absir
 * 
 */
public class JRelateDaoImpl extends BaseDaoImpl<JbRelation, Long> implements IRelateDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.dao.IRelateDao#relateConditions(java.lang.String
	 * , com.absir.appserv.system.bean.proxy.Proxies.JpUserBase,
	 * java.lang.Object, java.lang.String, java.lang.String,
	 * com.absir.appserv.jdbc.JdbcCondition,
	 * com.absir.appserv.jdbc.JdbcCondition.Conditions,
	 * com.absir.appserv.jdbc.JdbcCondition.Conditions,
	 * com.absir.appserv.jdbc.JdbcCondition.Conditions)
	 */
	@Override
	public void relateConditions(String rootEntityName, JiUserBase user, Object strategies, String joinAlias, String entityId, JdbcCondition jdbcCondition, Conditions includeConditions,
			Conditions excludeConditions, Conditions joinConditions) {
		// TODO Auto-generated method stub
	}
}
