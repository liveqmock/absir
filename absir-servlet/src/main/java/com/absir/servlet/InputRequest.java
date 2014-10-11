/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-8 下午5:09:53
 */
package com.absir.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.absir.binder.BinderData;
import com.absir.context.core.ContextUtils;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.helper.HelperIO;
import com.absir.core.kernel.KernelCharset;
import com.absir.core.kernel.KernelLang;
import com.absir.server.in.InMethod;
import com.absir.server.in.InModel;
import com.absir.server.in.Input;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public class InputRequest extends Input {

	/** uri */
	private String uri;

	/** method */
	private InMethod method;

	/** request */
	private HttpServletRequest request;

	/** response */
	private HttpServletResponse response;

	/** input */
	private String input;

	/** parameterMap */
	private Map<String, Object> parameterMap;

	/** PARAMETER_MAP_NAME */
	private static final String PARAMETER_MAP_NAME = InputRequest.class.getName() + "@PARAMETER_MAP_NAME";

	/** SERVLET_FILE_UPLOAD_DEFAULT */
	private static final ServletFileUpload SERVLET_FILE_UPLOAD_DEFAULT = new ServletFileUpload(new DiskFileItemFactory());

	/**
	 * @param request
	 * @return
	 * @throws FileUploadException
	 */
	public static Map<String, List<FileItem>> parseParameterMap(HttpServletRequest request) {
		Object fileItems = request.getAttribute(PARAMETER_MAP_NAME);
		if (fileItems == null || !(fileItems instanceof Map)) {
			try {
				fileItems = SERVLET_FILE_UPLOAD_DEFAULT.parseParameterMap(request);

			} catch (Exception e) {
				// TODO: handle exception
				fileItems = KernelLang.NULL_MAP;
			}

			request.setAttribute(PARAMETER_MAP_NAME, fileItems);
		}

		return (Map<String, List<FileItem>>) fileItems;
	}

	/**
	 * @param uri
	 * @param method
	 * @param model
	 * @param request
	 * @param response
	 */
	public InputRequest(String uri, InMethod method, InModel model, HttpServletRequest request, HttpServletResponse response) {
		super(model);
		if (request instanceof HttpServletRequest) {
			setId(((HttpServletRequest) request).getSession().getId());
		}

		this.uri = uri;
		this.method = method;
		this.request = request;
		this.response = response;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @return
	 */
	public BinderData getBinderData() {
		if (binderData == null) {
			binderData = new BinderRequest();
		}

		return binderData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getUri()
	 */
	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getMethod()
	 */
	@Override
	public InMethod getMethod() {
		// TODO Auto-generated method stub
		return method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#setStatus(int)
	 */
	@Override
	public void setStatus(int status) {
		// TODO Auto-generated method stub
		response.setStatus(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#paramDebug()
	 */
	@Override
	public boolean paramDebug() {
		// TODO Auto-generated method stub
		return request.getParameter("DEBUG") != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getAddress()
	 */
	@Override
	public String getAddress() {
		// TODO Auto-generated method stub
		return request.getRemoteAddr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return request.getAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setAttribute(String name, Object obj) {
		// TODO Auto-generated method stub
		request.setAttribute(name, obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getParam(java.lang.String)
	 */
	@Override
	public String getParam(String name) {
		// TODO Auto-generated method stub
		return request.getParameter(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getParams(java.lang.String)
	 */
	@Override
	public String[] getParams(String name) {
		// TODO Auto-generated method stub
		Object values = getParamMap().get(name);
		return values == null || !(values instanceof String[]) ? null : (String[]) values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getParamMap()
	 */
	@Override
	public Map<String, Object> getParamMap() {
		// TODO Auto-generated method stub
		if (parameterMap == null) {
			if (method != InMethod.GET) {
				String contentType = request.getContentType();
				if (contentType != null && contentType.startsWith("multipart/form-data")) {
					Map<String, List<FileItem>> fileItems = parseParameterMap(request);
					if (!fileItems.isEmpty()) {
						parameterMap = new HashMap<String, Object>();
						for (Entry<String, List<FileItem>> entry : fileItems.entrySet()) {
							List<Object> parameters = null;
							boolean fileItem = false;
							for (FileItem item : entry.getValue()) {
								if (parameters == null) {
									parameters = new ArrayList<Object>();
								}

								if (item.isFormField()) {
									parameters.add(new String(item.get(), ContextUtils.getCharset()));

								} else {
									fileItem = true;
									parameters.add(item);
								}
							}

							if (parameters != null) {
								parameterMap.put(entry.getKey(), DynaBinder.to(parameters, fileItem ? Object[].class : String[].class));
							}
						}

						return parameterMap;
					}
				}
			}

			parameterMap = (Map<String, Object>) (Object) KernelLang.NULL_MAP;
		}

		return (Object) parameterMap == KernelLang.NULL_MAP ? request.getParameterMap() : parameterMap;
	}

	/**
	 * @return
	 */
	public Map<String, List<FileItem>> parseParameterMap() {
		return parseParameterMap(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return request.getInputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getInput()
	 */
	@Override
	public String getInput() {
		// TODO Auto-generated method stub
		if (input == null) {
			try {
				input = HelperIO.toString(getInputStream(), KernelCharset.DEFAULT);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				input = KernelLang.NULL_STRING;
			}
		}

		return input;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#setCharacterEncoding(java.lang.String)
	 */
	@Override
	public void setCharacterEncoding(String charset) {
		// TODO Auto-generated method stub
		response.setCharacterEncoding(charset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#setCharacterEncoding(java.lang.String)
	 */
	@Override
	public void setContentTypeCharset(String contentTypeCharset) {
		// TODO Auto-generated method stub
		if (response instanceof HttpServletResponse) {
			((HttpServletResponse) response).setHeader("content-type", contentTypeCharset);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return response.getOutputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		getOutputStream().write(b, off, len);
	}

	/**
	 * @param name
	 * @return
	 */
	public String getSession(String name) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}

		Object value = session.getAttribute(name);
		return value == null ? null : value.toString();
	}

	/**
	 * @param name
	 * @param value
	 */
	public void setSession(String name, String value) {
		request.getSession().setAttribute(name, value);
	}

	/**
	 * @param name
	 */
	public void removeSession(String name) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			request.getSession().removeAttribute(name);
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public String getCookie(String name) {
		Cookie cookie = InputCookies.getCookie(request, name);
		return cookie == null ? null : cookie.getValue();
	}

	/**
	 * @param name
	 * @param value
	 * @param path
	 * @param remember
	 */
	public void setCookie(String name, String value, String path, long remember) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		cookie.setMaxAge((int) (remember / 1000));
		response.addCookie(cookie);
	}

	/**
	 * @param name
	 * @param path
	 */
	public void removeCookie(String name, String path) {
		setCookie(name, null, path, 0);
	}
}
