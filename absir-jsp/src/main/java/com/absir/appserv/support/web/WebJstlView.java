/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.support.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.absir.bean.basis.Base;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Value;
import com.absir.context.core.ContextUtils;
import com.absir.core.helper.HelperFile;
import com.absir.core.helper.HelperFileName;
import com.absir.core.kernel.KernelLang;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.route.returned.ReturnedResolverView;
import com.absir.servlet.InDispathFilter;
import com.absir.servlet.InputRequest;

@Base(order = -1)
@Bean
public class WebJstlView extends ReturnedResolverView {

	@Value("web.view.prefix")
	private String prefix = "/WEB-INF/jsp/";

	@Value("web.view.suffix")
	private String suffix = ".jsp";

	/** LAYOUT_ITERATE_DEPTH */
	private static final int LAYOUT_ITERATE_DEPTH = 6;

	/** PRERPARE_NAME */
	private static final String PRERPARE_NAME = "prerpare";

	/** LAYOUT_NAME */
	public static final String LAYOUT_NAME = "layout";

	/** LAYOUT_BODY_NAME */
	public static final String LAYOUT_BODY_NAME = "layout_body";

	/** File_Name_Map_Layout_Name */
	private static final Map<String, String> File_Name_Map_Layout_Name = new HashMap<String, String>();

	/** Layout_Name_View */
	private static String Layout_Name_View = LAYOUT_NAME + ".jsp";

	/** Server_Context_Path */
	private static String Server_Context_Path = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.returned.ReturnedResolverView#resolveReturnedView
	 * (java.lang.String, com.absir.server.on.OnPut)
	 */
	@Override
	public void resolveReturnedView(String view, OnPut onPut) throws Exception {
		// TODO Auto-generated method stub
		Input input = onPut.getInput();
		if (input instanceof InputRequest) {
			InputRequest inputRequest = (InputRequest) input;
			renderView(prefix + view + suffix, input, inputRequest.getRequest(), inputRequest.getResponse());

		} else {
			onPut.getInput().write(view);
		}
	}

	/**
	 * @param view
	 * @param input
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void renderView(String view, Input input, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (Server_Context_Path == null) {
			Server_Context_Path = InDispathFilter.getServletContext().getRealPath("");
		}

		request.setAttribute(LAYOUT_NAME, true);
		request.setAttribute(PRERPARE_NAME, view);
		for (Entry<String, Object> entry : input.getModel().entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
		
		request.setAttribute(WebJsplUtils.REQUEST_INPUT_NAME, input);
		response.setCharacterEncoding(ContextUtils.getCharset().displayName());
		RequestDispatcher rd = request.getRequestDispatcher(view);
		WebResponseWrapper responseWrapper = new WebResponseWrapper(response);
		rd.include(request, responseWrapper);
		renderMergeOutputLayout(new WebRequestAttributes(request), response, responseWrapper, LAYOUT_ITERATE_DEPTH);
	}

	/**
	 * @param filename
	 * @return
	 */
	private String getLayoutFilename(String filename) {
		String layoutFilename = File_Name_Map_Layout_Name.get(filename);
		if (layoutFilename == null) {
			synchronized (this) {
				layoutFilename = File_Name_Map_Layout_Name.get(filename);
				if (layoutFilename == null) {
					layoutFilename = Server_Context_Path + HelperFileName.addFilenameSubExtension(filename, LAYOUT_NAME);
					if (!HelperFile.fileExists(layoutFilename)) {
						layoutFilename = HelperFileName.iterateFilename(Server_Context_Path, filename, Layout_Name_View);
					}

					if (layoutFilename == null) {
						layoutFilename = KernelLang.NULL_STRING;

					} else {
						layoutFilename = layoutFilename.substring(Server_Context_Path.length());
					}

					File_Name_Map_Layout_Name.put(filename, layoutFilename);
				}
			}
		}

		return layoutFilename;
	}

	/**
	 * @param request
	 * @param response
	 * @param wrapper
	 * @param depth
	 * @throws Exception
	 */
	protected void renderMergeOutputLayout(HttpServletRequest request, HttpServletResponse response, WebResponseWrapper wrapper, int depth) throws Exception {
		String content = wrapper.getContent();
		if (depth-- != 0) {
			Object layout = request.getAttribute(LAYOUT_NAME);
			if (layout != null) {
				if (layout instanceof Boolean) {
					if ((Boolean) layout) {
						Object filename = request.getAttribute(PRERPARE_NAME);
						if (filename != null) {
							if (filename instanceof String) {
								layout = getLayoutFilename((String) filename);
							}
						}
					}
				}

				request.removeAttribute(LAYOUT_NAME);
				if (KernelLang.NULL_STRING != layout && layout instanceof String) {
					request.setAttribute(PRERPARE_NAME, layout);
					request.setAttribute(LAYOUT_BODY_NAME, content);
					content = null;

					RequestDispatcher rd = request.getRequestDispatcher((String) layout);
					wrapper.resetBuffer();
					rd.include(request, wrapper);
					renderMergeOutputLayout(request, response, wrapper, depth);
				}
			}
		}

		if (content != null) {
			response.getWriter().append(content);
		}
	}
}
