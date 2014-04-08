/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:58:37
 */
package com.absir.appserv.system.api;

import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.appserv.feature.transaction.TransactionIntercepter;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.helper.HelperJson;
import com.absir.appserv.system.security.SecurityContext;
import com.absir.appserv.system.security.SecurityManager;
import com.absir.appserv.system.service.SecurityService;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.in.Input;
import com.absir.server.in.Interceptor;
import com.absir.server.on.OnPut;
import com.absir.server.value.After;
import com.absir.server.value.Before;
import com.absir.server.value.Body;
import com.absir.server.value.Interceptors;
import com.absir.server.value.Mapping;
import com.absir.server.value.OnException;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
@Mapping("/api")
@Interceptors(ApiServer.Route.class)
public abstract class ApiServer {

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiServer.class);

	/**
	 * @param input
	 * @throws Throwable
	 */
	@Before
	protected SecurityContext onAuthentication(Input input) throws Throwable {
		return SecurityService.ME.getSecurityContext(input);
	}

	/**
	 * 统一返回类型
	 * 
	 * @param onPut
	 */
	@Body
	@After
	protected void after(OnPut onPut) {
	}

	/**
	 * 统一异常返回
	 * 
	 * @param e
	 * @return
	 */
	@Body
	@OnException(Throwable.class)
	protected Object onException(Throwable e, Input input) {
		if (input instanceof InputRequest) {
			((InputRequest) input).getResponse().setStatus(HttpStatus.SC_NOT_ACCEPTABLE);
		}

		if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
			e.printStackTrace();

		} else if (!(e instanceof ServerException)) {
			LOGGER.error("api error", e);
		}

		return e.toString();
	}

	/**
	 * @author absir
	 * 
	 */
	public static class Route extends TransactionIntercepter {

		/**
		 * @param iterator
		 * @param input
		 * @return
		 * @throws Throwable
		 */
		public OnPut interceptImpl(Iterator<Interceptor> iterator, Input input) throws Throwable {
			SecurityContext securityContext = SecurityService.ME.autoLogin("api", true, -1, input);
			if (securityContext == null && input instanceof InputRequest) {
				InputRequest inputRequest = (InputRequest) input;
				JiUserBase userBase = getInputUserBase(inputRequest);
				if (userBase != null) {
					SecurityManager securityManager = SecurityService.ME.getSecurityManager("api");
					long remember = securityManager.getSessionExpiration();
					if (remember < securityManager.getSessionLife()) {
						remember = securityManager.getSessionLife();
					}

					SecurityService.ME.loginUser(securityManager, userBase, remember, inputRequest);
				}
			}

			return input.intercept(iterator);
		}

		/**
		 * @param input
		 * @return
		 */
		protected JiUserBase getInputUserBase(InputRequest inputRequest) {
			String identity = inputRequest.getRequest().getHeader("identity");
			if (identity != null) {
				Map<?, ?> identityMap = HelperJson.decodeMap(identity);
				String username = String.valueOf(identityMap.get("username"));
				String password = String.valueOf(identityMap.get("password"));
				JiUserBase userBase = SecurityService.ME.getUserBase(username);
				if (userBase != null && !(SecurityService.ME.validator(userBase, password))) {
					userBase = null;
				}

				return userBase;
			}

			return null;
		}
	}
}
