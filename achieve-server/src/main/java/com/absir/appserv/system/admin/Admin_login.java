/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-6 下午1:17:33
 */
package com.absir.appserv.system.admin;

import com.absir.appserv.feature.menu.MenuContextUtils;
import com.absir.appserv.system.asset.Asset_verify;
import com.absir.appserv.system.bean.value.JeRoleLevel;
import com.absir.appserv.system.helper.HelperInput;
import com.absir.appserv.system.security.SecurityContext;
import com.absir.appserv.system.server.ServerResolverRedirect;
import com.absir.appserv.system.service.SecurityService;
import com.absir.bean.basis.Base;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.InMethod;
import com.absir.server.in.Input;
import com.absir.server.value.Mapping;
import com.absir.server.value.Nullable;
import com.absir.server.value.Param;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Base
@Server
public class Admin_login extends AdminServer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.admin.AdminServer#checkLogin(com.absir.server
	 * .in.Input)
	 */
	@Override
	protected SecurityContext onAuthentication(Input input) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 登陆界面
	 * 
	 * @param input
	 * @return
	 */
	public String route(Input input) {
		input.getModel().put("remember", remember);
		return HelperInput.isAjax(input) ? "admin/login.timeout" : "admin/login";
	}

	/**
	 * AJAX登录
	 * 
	 * @param input
	 * @return
	 */
	public String ajax(Input input) {
		input.getModel().put("remember", remember);
		return "admin/login.ajax";
	}

	/**
	 * 注销登录
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public void out(Input input) throws Exception {
		SecurityService.ME.logout("admin", input);
		ServerResolverRedirect.redirect(MenuContextUtils.getAdminRoute() + "/login", false, input);
	}

	/**
	 * 登录处理
	 * 
	 * @param username
	 * @param password
	 * @param remember
	 * @param input
	 * @return
	 * @throws Exception
	 */
	@Mapping(method = InMethod.POST)
	public String route(@Param String username, @Param String password, @Param @Nullable long remember, Input input) throws Exception {
		try {
			SecurityService.ME.logout("admin", input);
			if (!HelperInput.isAjax(input) && !Asset_verify.verifyInput(input)) {
				throw new ServerException(ServerStatus.NO_VERIFY);
			}

			SecurityService.ME.login(username, password, remember, JeRoleLevel.ROLE_ADMIN.ordinal(), "admin", input);

		} catch (ServerException e) {
			// TODO: handle exception
			if (HelperInput.isAjax(input)) {
				return "admin/login.failed";
			}

			input.getModel().put("error", e.getExceptionData());
			return "admin/login";
		}

		if (!HelperInput.isAjax(input)) {
			ServerResolverRedirect.redirect(MenuContextUtils.getAdminRoute() + "/main", false, input);
		}

		return "admin/login.success";
	}
}
