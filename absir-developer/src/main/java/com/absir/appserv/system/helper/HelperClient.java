/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午1:29:26
 */
package com.absir.appserv.system.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.core.helper.HelperIO;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public class HelperClient {

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(HelperClient.class);

	/**
	 * @param urlConnection
	 * @return
	 * @throws IOException
	 */
	public static InputStream openConnection(HttpURLConnection urlConnection) throws IOException {
		return urlConnection.getInputStream();
	}

	/**
	 * @param urlConnection
	 * @return
	 * @throws IOException
	 */
	public static <T> T openConnectionJson(HttpURLConnection urlConnection, Class<T> type) throws IOException {
		return HelperJson.OBJECT_MAPPER.reader(type).readValue(openConnection(urlConnection));
	}

	/**
	 * @param url
	 * @param post
	 * @param postParameters
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static <T> T openConnection(String url, Map<String, String> postParameters, Class<T> type) {
		byte[] postBytes = null;
		if (postParameters != null) {
			StringBuffer paramsBuffer = new StringBuffer();
			for (Entry<String, String> entry : postParameters.entrySet()) {
				if (paramsBuffer.length() > 0) {
					paramsBuffer.append("&");
				}

				paramsBuffer.append(entry.getKey()).append("=").append(entry.getValue());
			}

			postBytes = paramsBuffer.toString().getBytes();
		}

		return openConnection(url, postBytes == null ? false : true, postBytes, 0, type);
	}

	/**
	 * @param url
	 * @param post
	 * @param postBytes
	 * @param length
	 * @param type
	 * @return
	 */
	public static <T> T openConnection(String url, boolean post, byte[] postBytes, int length, Class<T> type) {
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) (new URL(url)).openConnection();
			if (post || postBytes != null) {
				urlConnection.setRequestMethod("POST");
				if (postBytes != null) {
					if (length <= 0) {
						length = postBytes.length;
					}

					if (length > 0) {
						urlConnection.setDoOutput(true);
						urlConnection.getOutputStream().write(postBytes, 0, length);
					}
				}
			}

			return type == null || type.isAssignableFrom(String.class) ? (T) HelperIO.toString(urlConnection.getInputStream()) : openConnectionJson(urlConnection, type);

		} catch (Throwable e) {
			if (BeanFactoryUtils.getEnvironment() == Environment.DEVELOP) {
				e.printStackTrace();
			}

			LOGGER.error("", e);
		}

		return null;
	}
}
