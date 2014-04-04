/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-27 上午9:37:49
 */
package com.absir.appserv.system.dao.hibernate;

import java.util.Collection;
import java.util.List;

import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.jdbc.JdbcCondition.Conditions;
import com.absir.appserv.system.bean.base.JbAssoc;
import com.absir.appserv.system.bean.proxy.JiUser;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.dao.IAssocDao;
import com.absir.appserv.system.helper.HelperCondition;
import com.absir.bean.inject.value.Bean;
import com.absir.core.kernel.KernelCollection;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.value.JePermission;
import com.absir.orm.value.JiAssoc;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@Bean
public class JAssocDaoImpl<T> extends BaseDaoImpl<JbAssoc, Long> implements IAssocDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.dao.IAssocDao#supportAssocClass(java.lang.Class,
	 * java.lang.String, com.absir.appserv.system.bean.proxy.JiUserBase,
	 * com.absir.appserv.support.entity.value.JePermission)
	 */
	@Override
	public boolean supportAssocClass(Class<? extends JiAssoc> assocClass, String rootEntityName, JiUserBase userBase, JePermission permission) {
		// TODO Auto-generated method stub
		return userBase != null && assocClass.isAssignableFrom(getBaseClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.dao.IAssocDao#assocConditions(java.lang.String,
	 * com.absir.appserv.system.bean.proxy.JiUserBase,
	 * com.absir.appserv.support.entity.value.JePermission, java.lang.Object,
	 * com.absir.appserv.jdbc.JdbcCondition,
	 * com.absir.appserv.jdbc.JdbcCondition.Conditions,
	 * com.absir.appserv.jdbc.JdbcCondition.Conditions)
	 */
	@Override
	public final void assocConditions(String rootEntityName, JiUserBase user, JePermission permission, Object strategies, JdbcCondition jdbcCondition, Conditions includeConditions,
			Conditions excludeConditions) {
		// TODO Auto-generated method stub
		if (strategies == null || !supportStrategy(rootEntityName, user, strategies)) {
			assocConditions(rootEntityName, user, jdbcCondition, includeConditions, excludeConditions);

		} else {
			assocStrategies(rootEntityName, user, strategies, jdbcCondition, includeConditions, excludeConditions);
		}
	}

	/**
	 * @param rootEntityName
	 * @param user
	 * @param jdbcCondition
	 * @param includeConditions
	 * @param excludeConditions
	 */
	protected void assocConditions(String rootEntityName, JiUserBase user, JdbcCondition jdbcCondition, Conditions includeConditions, Conditions excludeConditions) {
		// TODO Auto-generated method stub
		if (JiUser.class.isAssignableFrom(getBaseClass())) {
			if (user == null) {
				throw new ServerException(ServerStatus.NO_LOGIN);
			}

			String alias = jdbcCondition.getPropertyAlias();
			HelperCondition.concatOR(includeConditions, alias + ".userId = ?");
			includeConditions.add(user.getUserId());
		}
	}

	/**
	 * @param rootEntityName
	 * @param user
	 * @param strategies
	 * @return
	 */
	protected boolean supportStrategy(String rootEntityName, JiUserBase user, Object strategies) {
		return strategies instanceof List && ((List<?>) strategies).size() > 0;
	}

	/**
	 * @param rootEntityName
	 * @param user
	 * @param strategies
	 * @param jdbcCondition
	 * @param includeConditions
	 * @param excludeConditions
	 */
	protected void assocStrategies(String rootEntityName, JiUserBase user, Object strategies, JdbcCondition jdbcCondition, Conditions includeConditions, Conditions excludeConditions) {
		HelperCondition.concatOR(includeConditions, jdbcCondition.getPropertyAlias(1) + ".id IN (?)");
		includeConditions.add(KernelCollection.castToArray((Collection<?>) strategies, SessionFactoryUtils.getIdentifierType(null, getBaseClass())));
	}
}
