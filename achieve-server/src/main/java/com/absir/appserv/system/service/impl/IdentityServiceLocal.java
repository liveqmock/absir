/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午12:27:30
 */
package com.absir.appserv.system.service.impl;

import java.util.Map;

import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.helper.HelperString;
import com.absir.appserv.system.service.IdentityService;
import com.absir.appserv.system.service.SecurityService;
import com.absir.bean.basis.Base;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class IdentityServiceLocal implements IdentityService {

	/** identityServiceMap */
	@Inject(value = "IdentityService")
	private static Map<String, IdentityService> identityServiceMap;

	/**
	 * @param identity
	 * @return
	 */
	public static JiUserBase getUserBase(String identity) {
		if (!KernelString.isEmpty(identity)) {
			String[] parameters = HelperString.split(identity, ',');
			if (parameters.length > 0) {
				IdentityService identityService = identityServiceMap.get(parameters[0]);
				if (identityService != null) {
					return identityService.getUserBase(parameters);
				}
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.IdentityService#getUserBase(java.lang
	 * .String[])
	 */
	@Override
	public JiUserBase getUserBase(String[] parameters) {
		// TODO Auto-generated method stub
		if (parameters.length == 3) {
			JiUserBase userBase = SecurityService.ME.getUserBase(parameters[1]);
			if (userBase != null) {
				if (SecurityService.ME.validator(userBase, parameters[2])) {
					return userBase;
				}
			}
		}

		return null;
	}
}