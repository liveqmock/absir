/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-31 下午5:16:29
 */
package com.absir.appserv.system.service;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.absir.appserv.developer.Pag;
import com.absir.appserv.dyna.DynaBinderUtils;
import com.absir.appserv.support.developer.IDeveloper.ISecurity;
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
import com.absir.context.lang.LangBundle;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.core.kernel.KernelString;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.IGet;
import com.absir.server.in.Input;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
public abstract class SecurityService implements ISecurityService, ISecurity, IGet {

	/** ME */
	public static final SecurityService ME = BeanFactoryUtils.get(SecurityService.class);

	/** securityManager */
	@Inject
	private SecurityManager securityManager;

	/** securityManagerMap */
	@Inject
	private Map<String, SecurityManager> securityManagerMap;

	/** SECURITY_USER_NAME */
	private static final String SECURITY_USER_NAME = "USER";

	/** SECURITY_CONTEXT_NAME */
	private static final String SECURITY_CONTEXT_NAME = "SECURITY";

	/**
	 * @param input
	 * @return
	 */
	public JiUserBase getUserBase(Input input) {
		Object user = input.getModel().get(SECURITY_USER_NAME);
		return user == null || !(user instanceof JiUserBase) ? null : (JiUserBase) user;
	}

	/**
	 * @param userBase
	 * @param input
	 */
	public void setUserBase(JiUserBase userBase, Input input) {
		input.getModel().put(SECURITY_USER_NAME, userBase);
	}

	/**
	 * @param input
	 * @return
	 */
	public SecurityContext getSecurityContext(Input input) {
		Object securityContext = input.getModel().get(SECURITY_CONTEXT_NAME);
		return securityContext == null || !(securityContext instanceof SecurityContext) ? null : (SecurityContext) securityContext;
	}

	/**
	 * @param securityContext
	 * @param input
	 */
	public void setSecurityContext(SecurityContext securityContext, Input input) {
		input.getModel().put(SECURITY_CONTEXT_NAME, securityContext);
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

		if (securityContext == null || securityContext.getUser() == null || securityContext.getUser().isDisabled()) {
			return null;
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
			if (userBase == null || userBase.isDisabled()) {
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

	/** SECURITY_SESSION_NAME */
	protected static final String SECURITY_SESSION_NAME = SecurityService.class.getName() + "@" + "SECURITY_SESSION_NAME" + "@";

	/**
	 * @param name
	 * @param toClass
	 * @param input
	 * @return
	 */
	public <T> T getSession(String name, Class<T> toClass, Input input) {
		T value = null;
		JiUserBase user = getUserBase(input);
		if (user != null) {
			value = DynaBinderUtils.to(user.getMetaMap(name), toClass);
		}

		if (value == null && input instanceof InputRequest) {
			value = DynaBinderUtils.to(((InputRequest) input).getSession(SECURITY_SESSION_NAME + name), toClass);
		}

		return value;
	}

	/**
	 * @param name
	 * @param value
	 * @param input
	 */
	public void setSession(final String name, final Object value, Input input) {
		JiUserBase user = getUserBase(input);
		if (user == null) {
			if (input instanceof InputRequest) {
				((InputRequest) input).setSession(SECURITY_SESSION_NAME + name, DynaBinderUtils.to(value, String.class));
			}

		} else {
			BeanService.MERGE.merge(user, user.getUserId(), new CallbackTemplate<Object>() {

				@Override
				public void doWith(Object template) {
					// TODO Auto-generated method stub
					((JiUserBase) template).setMetaMap(name, DynaBinderUtils.to(value, String.class));
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.IGet#getLocale(com.absir.server.in.Input)
	 */
	@Override
	public Locale getLocale(Input input) {
		// TODO Auto-generated method stub
		Integer locale = getSession("locale", Integer.class, input);
		if (locale == null && input instanceof InputRequest) {
			locale = LangBundle.ME.getLocaleCode(((InputRequest) input).getRequest().getLocale());
			if (locale != null) {
				setSession("locale", locale, input);
			}
		}

		return LangBundle.ME.getLocale(locale);
	}
}
