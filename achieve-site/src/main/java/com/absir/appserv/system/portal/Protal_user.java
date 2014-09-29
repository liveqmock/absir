/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月29日 下午2:59:39
 */
package com.absir.appserv.system.portal;

import com.absir.appserv.developer.Pag;
import com.absir.appserv.system.bean.JUser;
import com.absir.appserv.system.service.SecurityService;
import com.absir.appserv.system.service.VerifierService;
import com.absir.appserv.system.service.utils.CrudServiceUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.Input;
import com.absir.server.route.parameter.ParameterResolverBinder;
import com.absir.server.value.Param;
import com.absir.server.value.Server;

/**
 * @author absir
 *
 */
@Server
public class Protal_user {

	/** TAG */
	protected static final String TAG = "LA";

	/**
	 * 用户注册
	 * 
	 * @param user
	 */
	public void register(Input input) {
		if (!Pag.CONFIGURE.isAllowRegister()) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		JUser user = ParameterResolverBinder.getBinderObject(null, JUser.class, 1, input);
		if (Pag.CONFIGURE.getVerifyTime() > 0) {
			user.setActivation(false);

		} else {
			user.setActivation(true);
		}

		user.setDisabled(Pag.CONFIGURE.isRegisterDisable());
		CrudServiceUtils.merge("JUser", null, user, true, null, input.getBinderData().getBinderResult().getPropertyFilter());
		SecurityService.ME.setUserBase(user, input);
		// 通知激活
		if (!user.isActivation()) {
			VerifierService.ME.persistVerifier(user, TAG, user.getId().toString(), Pag.CONFIGURE.getVerifyTime());
		}
	}

	/**
	 * 发送激活
	 * 
	 * @param verifierId
	 */
	public void activate(@Param String verifierId) {

	}

	/**
	 * 用户激活
	 * 
	 * @param verifierId
	 */
	public void activation(@Param String verifierId) {
		if (Pag.CONFIGURE.getVerifyTime() <= 0) {

		}
	}
}
