/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-31 下午5:16:29
 */
package com.absir.appserv.system.service;

import java.util.Map;

import javax.servlet.ServletRequest;

import com.absir.appserv.support.developer.IDeveloper.ISecurity;
import com.absir.appserv.support.developer.Pag;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JeRoleLevel;
import com.absir.appserv.system.helper.HelperLong;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.appserv.system.security.ISecurityService;
import com.absir.appserv.system.security.SecurityContext;
import com.absir.appserv.system.security.SecurityManager;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Inject;
import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelString;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.Input;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
public abstract class SecurityService implements ISecurityService, ISecurity {

	/** ME */
	public static final SecurityService ME = BeanFactoryUtils.get(SecurityService.class);

	/** securityManager */
	@Inject
	private SecurityManager securityManager;

	/** securityManagerMap */
	@Inject
	private Map<String, SecurityManager> securityManagerMap;

	/** SECURITY_USER_NAME */
	private static final String SECURITY_USER_NAME = SecurityService.class.getName() + "@SECURITY_USER_NAME";

	/** SECURITY_CONTEXT_NAME */
	private static final String SECURITY_CONTEXT_NAME = SecurityService.class.getName() + "@SECURITY_CONTEXT_NAME";

	/**
	 * @param input
	 * @return
	 */
	public JiUserBase getUserBase(Input input) {
		Object user = input.getAttribute(SECURITY_USER_NAME);
		return user == null || !(user instanceof JiUserBase) ? null : (JiUserBase) user;
	}

	/**
	 * @param userBase
	 * @param input
	 */
	public void setUserBase(JiUserBase userBase, Input input) {
		input.setAttribute(SECURITY_USER_NAME, userBase);
	}

	/**
	 * @param input
	 * @return
	 */
	public SecurityContext getSecurityContext(Input input) {
		Object securityContext = input.getAttribute(SECURITY_CONTEXT_NAME);
		return securityContext == null || !(securityContext instanceof SecurityContext) ? null : (SecurityContext) securityContext;
	}

	/**
	 * @param securityContext
	 * @param input
	 */
	public void setSecurityContext(SecurityContext securityContext, Input input) {
		input.setAttribute(SECURITY_CONTEXT_NAME, securityContext);
	}

	/**
	 * @param name
	 * @return
	 */
	public SecurityManager getSecurityManager(String name) {
		SecurityManager securityManager = KernelString.isEmpty(name) ? null : securityManagerMap.get(name);
		return securityManager == null ? this.securityManager : securityManager;
	}

	/**
	 * @param name
	 * @param userBase
	 * @param inputRequest
	 * @return
	 */
	public SecurityContext loginUser(String name, JiUserBase userBase, InputRequest inputRequest) {
		SecurityManager securityManager = SecurityService.ME.getSecurityManager(name);
		long remember = securityManager.getSessionExpiration();
		if (remember < securityManager.getSessionLife()) {
			remember = securityManager.getSessionLife();
		}

		return SecurityService.ME.loginUser(securityManager, userBase, remember, inputRequest);
	}

	/**
	 * @param securityContext
	 * @param userBase
	 */
	protected abstract void setSecurityContext(SecurityContext securityContext, JiUserBase userBase);

	/**
	 * @param securityManager
	 * @param userBase
	 * @param remember
	 * @param inputRequest
	 * @return
	 */
	public SecurityContext loginUser(SecurityManager securityManager, JiUserBase userBase, long remember, InputRequest inputRequest) {
		long contextTime = ContextUtils.getContextTime();
		String sessionId = HelperRandom.randSecendId(contextTime, 8, inputRequest.getRequest().hashCode());
		SecurityContext securityContext = ContextUtils.getContext(SecurityContext.class, sessionId);
		securityContext.setUser(userBase);
		securityContext.setAddress(HelperLong.longIP(inputRequest.getRequest().getRemoteAddr(), -1));
		securityContext.setAgent(inputRequest.getRequest().getHeader("user-agent"));
		securityContext.setLifeTime(securityManager.getSessionLife());
		securityContext.retainAt(contextTime);
		long sessionExpiration = securityManager.getSessionExpiration();
		if (sessionExpiration >= 0 && sessionExpiration < remember) {
			sessionExpiration = remember;
		}

		setSecurityContext(securityContext, userBase);
		securityContext.setMaxExpirationTime(sessionExpiration);
		setSecurityContext(securityContext, inputRequest);
		if (remember > 0) {
			inputRequest.setCookie(securityManager.getSessionKey(), sessionId, securityManager.getCookiePath(), remember);
		}

		setUserBase(userBase, inputRequest);
		return securityContext;
	}

	/**
	 * @param sessionId
	 * @param securityManager
	 * @return
	 */
	protected abstract SecurityContext findSecurityContext(String sessionId, SecurityManager securityManager);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.security.ISecurityService#autoLogin(java.lang
	 * .String, boolean, int, com.absir.server.in.Input)
	 */
	@Override
	public SecurityContext autoLogin(String name, boolean remeber, int roleLevel, Input input) {
		// TODO Auto-generated method stub
		SecurityContext securityContext = getSecurityContext(input);
		if (securityContext == null) {
			if (input instanceof InputRequest) {
				InputRequest inputRequest = (InputRequest) input;
				SecurityManager securityManager = getSecurityManager(name);
				String sessionId = inputRequest.getSession(securityManager.getSessionKey());
				if (sessionId == null && remeber) {
					sessionId = inputRequest.getCookie(securityManager.getSessionKey());
				}

				if (sessionId != null) {
					securityContext = ContextUtils.findContext(SecurityContext.class, sessionId);
					if (securityContext == null) {
						securityContext = ME.findSecurityContext(sessionId, securityManager);
						if (securityContext == null) {
							return null;
						}
					}

					securityContext.retainAt();
					setSecurityContext(securityContext, input);
					setUserBase(securityContext.getUser(), input);
				}
			}
		}

		return securityContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.security.ISecurityService#login(java.lang.String
	 * , java.lang.String, long, int, java.lang.String,
	 * com.absir.server.in.Input)
	 */
	@Override
	public SecurityContext login(String username, String password, long remember, int roleLevel, String name, Input input) {
		// TODO Auto-generated method stub
		if (input instanceof InputRequest) {
			InputRequest inputRequest = (InputRequest) input;
			JiUserBase userBase = ME.getUserBase(username);
			if (userBase == null) {
				throw new ServerException(ServerStatus.NO_USER);
			}

			SecurityManager securityManager = getSecurityManager(name);
			if (!validator(userBase, password, securityManager.getError(), securityManager.getErrorTime())) {
				throw new ServerException(ServerStatus.NO_USER, userBase);
			}

			if (userBase.getUserRoleLevel() >= roleLevel) {
				SecurityContext securityContext = loginUser(securityManager, userBase, remember, inputRequest);
				if (securityContext != null) {
					inputRequest.setSession(securityManager.getSessionKey(), securityContext.getId());
				}

				return securityContext;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.security.ISecurityService#logout(java.lang.String
	 * , com.absir.server.in.Input)
	 */
	@Override
	public void logout(String name, Input input) {
		// TODO Auto-generated method stub
		SecurityContext securityContext = autoLogin(name, false, 0, input);
		if (securityContext != null) {
			// 销毁之前的登录
			securityContext.setSecuritySupply(null);
			securityContext.setExpiration();
			if (input instanceof InputRequest) {
				InputRequest inputRequest = (InputRequest) input;
				SecurityManager securityManager = getSecurityManager(name);
				inputRequest.removeSession(securityManager.getSessionKey());
				inputRequest.removeCookie(securityManager.getSessionKey(), securityManager.getCookiePath());
			}

			setSecurityContext(null, input);
		}

		setUserBase(null, input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.developer.IDeveloper.ISecurity#loginRender(
	 * java.lang.Object)
	 */
	@Override
	public JiUserBase loginRender(Object render) {
		// TODO Auto-generated method stub
		Input input = null;
		if (render instanceof Input) {
			input = (Input) render;

		} else if (render instanceof ServletRequest) {
			input = Pag.getInput((ServletRequest) render);
		}

		SecurityContext securityContext = input == null ? null : autoLogin("admin", true, JeRoleLevel.ROLE_ADMIN.ordinal(), input);
		return securityContext == null ? null : securityContext.getUser();
	}
}
