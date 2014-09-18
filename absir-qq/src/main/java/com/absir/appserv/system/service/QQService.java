/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午1:14:29
 */
package com.absir.appserv.system.service;

import java.net.URLEncoder;
import java.util.Set;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.bean.value.OErrorData;
import com.absir.appserv.system.configure.JQQConfigure;
import com.absir.appserv.system.helper.HelperClient;
import com.absir.appserv.system.helper.HelperJson;
import com.absir.bean.inject.value.Bean;
import com.absir.core.kernel.KernelCharset;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@Bean
public class QQService implements IdentityService, IPayInterface {

	/** QQ_CONFIGURE */
	public static final JQQConfigure QQ_CONFIGURE = JConfigureUtils.getConfigure(JQQConfigure.class);

	/** PLAT_FORM_NAME */
	public static final String PLAT_FORM_NAME = "QQ";

	/** CHECH_SESSION_URL */
	private static final String CHECH_SESSION_URL = "https://graph.qq.com/oauth2.0/me?access_token=";

	/** MAC_NAME */
	private static final String MAC_NAME = "HmacSHA1";

	/** CALLBACK_START */
	private static final int CALLBACK_START = "callback( ".length();

	/** CALLBACK_END */
	private static final int CALLBACK_END = " );".length();

	/**
	 * @param request
	 * @param excludes
	 * @return
	 */
	public static String sign(HttpServletRequest request, String... excludes) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(request.getRequestURI());
		Set<String> parameters = new TreeSet<String>(request.getParameterMap().keySet());
		parameters.remove("sign");
		for (String exclude : excludes) {
			parameters.remove(exclude);
		}

		boolean glus = false;
		for (String parameter : parameters) {
			if (glus) {
				stringBuilder.append('&');

			} else {
				glus = true;
			}

			stringBuilder.append(parameter);
			stringBuilder.append(parameter + "=");
			stringBuilder.append(request.getParameter(parameter));
		}

		try {
			SecretKey secretKey = new SecretKeySpec((QQ_CONFIGURE.getAppkey() + '&').getBytes(KernelCharset.UTF8), MAC_NAME);
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(secretKey);
			return Base64.encodeBase64String(mac.doFinal((request.getMethod() + "&" + URLEncoder.encode(stringBuilder.toString(), "UTF-8")).getBytes(KernelCharset.UTF8)));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @author absir
	 *
	 */
	public static class MeToken {

		/** openid */
		public String openid;

		/** client_id */
		public String client_id;
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
			String callBackToken = HelperClient.openConnection(CHECH_SESSION_URL + parameters[2], null, String.class);
			callBackToken = callBackToken.substring(CALLBACK_START, callBackToken.length() - CALLBACK_END);
			MeToken meToken = HelperJson.decodeNull(callBackToken, MeToken.class);
			String openId = parameters[1];
			if (meToken.openid.equals(openId)) {
				return SecurityService.ME.openUserBase(openId, null, PLAT_FORM_NAME);
			}
		}

		return null;
	}

	/**
	 * @author absir
	 * 
	 */
	public static class ResultData extends OErrorData {

		public int Result;

		public int Amount;

		public String client_secret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.pay.IPayInterface#validator(com.absir.appserv
	 * .system.bean.JPayTrade)
	 */
	@Override
	public boolean validator(JPayTrade payTrade) {
		// TODO Auto-generated method stub
		return payTrade.getStatus() == JePayStatus.PAYED;
	}
}
