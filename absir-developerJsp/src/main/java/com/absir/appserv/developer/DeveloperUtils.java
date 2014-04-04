/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.developer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.io.FileUtils;

import com.absir.appserv.developer.model.EntityModel;
import com.absir.appserv.developer.model.ModelFactory;
import com.absir.appserv.support.DeveloperBreak;
import com.absir.appserv.support.web.WebJsplUtils;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.core.helper.HelperFile;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.CallbackBreak;
import com.absir.core.kernel.KernelLang.ObjectTemplate;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class DeveloperUtils {

	/** Developer_Web */
	// private static Boolean Developer_Web = null;

	/** Generator_Tokens */
	private static Set<Object> Generator_Tokens = new HashSet<Object>();

	/** Generator_Map_Tokens */
	private static Map<String, Object> Generator_Map_Token = new HashMap<String, Object>();

	/**
	 * @param includePath
	 * @return
	 */
	public static String getDeveloperPath(String includePath) {
		int prefix = includePath.indexOf("/", 1);
		return includePath.substring(0, prefix) + "/developer" + includePath.substring(prefix);
	}

	/**
	 * @param filepath
	 * @return
	 */
	private static Object getGeneratorTokens(String filepath) {
		Object token = Generator_Map_Token.get(filepath);
		if (token == null) {
			synchronized (Generator_Map_Token) {
				token = Generator_Map_Token.get(filepath);
				if (token == null) {
					token = new Object();
					Generator_Map_Token.put(filepath, token);
				}
			}
		}

		return token;
	}

	/**
	 * @param filePath
	 * @param includePath
	 * @param request
	 * @param response
	 */
	protected static void generate(String filepath, String includePath, HttpServletRequest request, ServletResponse response) {
		try {
			// 实体生成信息
			Object joEntity = request.getAttribute("joEntity");
			if (joEntity == null || !(joEntity instanceof JoEntity)) {
				Object value = request.getAttribute("entityName");
				String entityName = value != null && value instanceof String ? (String) value : null;
				value = request.getAttribute("entityClass");
				Class<?> entityClass = value != null && value instanceof Class ? (Class<?>) value : null;
				if (entityName == null && entityClass == null) {
					return;
				}

				joEntity = new JoEntity(entityName, entityClass);
				request.setAttribute("joEntity", joEntity);
			}

			EntityModel entityModel = ModelFactory.getModelEntity((JoEntity) joEntity);
			// 如果实体信息不存在
			if (entityModel == null) {
				return;
			}

			File file = new File(request.getSession().getServletContext().getRealPath(filepath));
			// 如果生成文件没过期
			if (file.exists() && entityModel.lastModified() != null && entityModel.lastModified() <= file.lastModified()) {
				return;
			}

			Object token = getGeneratorTokens(filepath);
			try {
				synchronized (token) {
					if (!Generator_Tokens.add(token)) {
						return;
					}

					final DeveloperGenerator generator = DeveloperGenerator.pushDeveloperGenerator(request);
					try {
						// 读取原文件定义信息
						if (file.exists()) {
							final ObjectTemplate<String> gDefine = new ObjectTemplate<String>(null);
							try {
								HelperFile.doWithReadLine(file, new CallbackBreak<String>() {

									@Override
									public void doWith(String template) throws BreakException {
										// TODO Auto-generated method stub
										String define = template.trim();
										if (define.contains("<%-- G_DEFINED")) {
											throw new DeveloperBreak();
										}

										if (define.startsWith("<%-- G_BEGAN")) {
											if (gDefine.object == null) {
												gDefine.object = "\r\n";
											}
										}

										if (gDefine.object != null) {
											gDefine.object += template + "\r\n";
											if (define.endsWith("G_END --%>")) {
												generator.addGeneratorDefine(gDefine.object);
												gDefine.object = null;
											}
										}
									}
								});
							} catch (IOException e) {
							}
						}

						// 获取生成文件流
						FileOutputStream output = HelperFile.openOutputStream(file, entityModel.lastModified());
						if (output != null) {
							try {
								request.setAttribute("entityModel", entityModel);
								includePath = getDeveloperPath(includePath);
								WebJsplUtils.render(output, includePath, request, response);
								/*
								 * if (Developer_Web == null) { synchronized
								 * (DeveloperUtils.class) { Developer_Web = new
								 * File(DeveloperComponent.getDeveloperWeb() +
								 * includePath).exists(); // if (Developer_Web)
								 * { // } } Developer_Web && }
								 */

								// 复制生成文件到开发环境
								if (DeveloperService.getDeveloperWeb() != null) {
									FileUtils.copyFile(file, new File(DeveloperService.getDeveloperWeb() + filepath));
								}

							} finally {
								if (output != null) {
									output.close();
								}
							}
						}

					} finally {
						DeveloperGenerator.popDeveloperGenerator(request);
					}
				}

			} finally {
				synchronized (token) {
					Generator_Tokens.remove(token);
				}
			}

		} catch (Exception e) {
			// 显示生成错误
			if (BeanFactoryUtils.getBeanConfig().getEnvironment() == Environment.DEVELOP && !(e instanceof DeveloperBreak)) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param filepath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void generate(String filepath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		generate(filepath, WebJsplUtils.getServletPath(pageContext), pageContext, request, response);
	}

	/**
	 * @param filepath
	 * @param includePath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void generate(String filepath, String includePath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		generate(WebJsplUtils.getFullIncludePath(filepath, pageContext), includePath, request, response);
	}

	/**
	 * @param filepath
	 * @param includePath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void generateInclude(String filepath, String includePath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		filepath = WebJsplUtils.getFullExsitIncludePath(filepath, pageContext);
		generate(filepath, includePath, pageContext, request, response);
		WebJsplUtils.include(filepath, pageContext, request, response);
	}

	/**
	 * @param option
	 * @param types
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void includeExist(String option, List<String> types, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		includeExist(option, types, pageContext, request, response, "/WEB-INF/developer/type/");
	}

	/**
	 * @param types
	 * @param pageContext
	 * @param request
	 * @param response
	 * @param relativePaths
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void includeExist(String option, List<String> types, PageContext pageContext, HttpServletRequest request, HttpServletResponse response, String... relativePaths)
			throws ServletException, IOException {
		for (String relativePath : relativePaths) {
			request.removeAttribute(relativePath + option);
			WebJsplUtils.includeExsit(relativePath + option + "/base.jsp", pageContext, request, response);
			for (String type : types) {
				if (WebJsplUtils.includeExsit(relativePath + option + "/" + type + ".jsp", pageContext, request, response)) {
					request.setAttribute(relativePath + option, type);
					break;
				}
			}

			WebJsplUtils.includeExsit(relativePath + option + "/type.jsp", pageContext, request, response);
		}
	}

	/**
	 * @param option
	 * @param entityName
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void includeExist(String option, String entityName, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		includeExist(option, entityName, pageContext, request, response, "/WEB-INF/developer/bean/");
	}

	/**
	 * @param option
	 * @param entityName
	 * @param pageContext
	 * @param request
	 * @param response
	 * @param relativePaths
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void includeExist(String option, String entityName, PageContext pageContext, HttpServletRequest request, HttpServletResponse response, String... relativePaths)
			throws ServletException, IOException {
		for (String relativePath : relativePaths) {
			WebJsplUtils.includeExsit(relativePath + option + "/" + entityName + ".jsp", pageContext, request, response);
		}
	}
}
