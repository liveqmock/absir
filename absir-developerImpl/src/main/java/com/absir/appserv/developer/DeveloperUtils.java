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

import javax.servlet.ServletRequest;

import com.absir.appserv.crud.CrudUtils;
import com.absir.appserv.developer.model.EntityModel;
import com.absir.appserv.developer.model.ModelFactory;
import com.absir.appserv.support.DeveloperBreak;
import com.absir.appserv.support.developer.IDeveloper;
import com.absir.appserv.support.developer.IRender;
import com.absir.appserv.support.developer.IRenderSuffix;
import com.absir.appserv.support.developer.RenderUtils;
import com.absir.bean.basis.Configure;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Value;
import com.absir.core.helper.HelperFile;
import com.absir.core.helper.HelperIO;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.CallbackBreak;
import com.absir.core.kernel.KernelLang.ObjectTemplate;
import com.absir.core.kernel.KernelString;
import com.absir.core.util.UtilAbsir;
import com.absir.orm.value.JoEntity;

@Configure
public class DeveloperUtils {

	@Value("developer.suffix")
	protected static String suffix = getSuffix();

	/** Generator_Map_Tokens */
	private static Map<String, Object> Generator_Map_Token = new HashMap<String, Object>();

	/** Generator_Tokens */
	private static Set<Object> Generator_Tokens = new HashSet<Object>();

	/**
	 * @return
	 */
	private static String getSuffix() {
		IRenderSuffix renderSuffix = BeanFactoryUtils.get(IRenderSuffix.class);
		if (renderSuffix == null) {
			if (IRender.ME != null && IRender.ME instanceof IRenderSuffix) {
				renderSuffix = (IRenderSuffix) IRender.ME;
			}
		}

		String suffix = renderSuffix == null ? null : renderSuffix.getSuffix();
		if (KernelString.isEmpty(suffix)) {
			suffix = ".dev";
		}

		return suffix;
	}

	/** DEVELOPER */
	private static final String DEVELOPER = "developer/";

	/** DEVELOPER_LENGTH */
	private static final int DEVELOPER_LENGTH = DEVELOPER.length();

	/**
	 * @param generaterPath
	 * @return
	 */
	public static String getGeneraterPath(String generaterPath) {
		int prefix = generaterPath.indexOf("/", 1) + 1;
		if (generaterPath.startsWith(DEVELOPER, prefix)) {
			return generaterPath.substring(0, prefix) + generaterPath.substring(prefix + DEVELOPER_LENGTH);
		}

		return generaterPath;
	}

	/**
	 * @param includePath
	 * @return
	 */
	public static String getDeveloperPath(String includePath) {
		int prefix = includePath.indexOf("/", 1) + 1;
		if (includePath.startsWith(DEVELOPER, prefix)) {
			return includePath;
		}

		return includePath.substring(0, prefix) + DEVELOPER + includePath.substring(prefix);
	}

	/**
	 * @param filepath
	 */
	public static void clearToken(String filepath) {
		Generator_Map_Token.remove(filepath);
	}

	/**
	 * @param filepath
	 * @param includePath
	 * @param renders
	 * @throws IOException
	 */
	protected static void generateRenders(String filepath, String includePath, Object... renders) throws IOException {
		ServletRequest request = KernelArray.getAssignable(renders, ServletRequest.class);
		if (request != null) {
			generate(filepath, includePath, request, renders);
		}
	}

	/** DIY */
	protected static final String DIY = DeveloperUtils.class.getName() + "@DIY";

	/**
	 * @param request
	 * @param diy
	 */
	public static void diy(ServletRequest request, boolean diy) {
		request.setAttribute(DIY, diy);
	}

	/**
	 * @param filePath
	 * @param includePath
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void generate(String filePath, String includePath, ServletRequest request, Object... renders) throws IOException {
		if (IRender.ME == null) {
			return;
		}

		try {
			// 实体生成信息
			Object joEntity = request.getAttribute("joEntity");
			if (joEntity == null || !(joEntity instanceof JoEntity)) {
				Object value = request.getAttribute("entityName");
				String entityName = value != null && value instanceof String ? (String) value : null;
				if (entityName == null) {
					entityName = request.getParameter("entityName");
				}

				value = request.getAttribute("entityClass");
				Class<?> entityClass = value != null && value instanceof Class ? (Class<?>) value : null;
				if (entityName != null || entityClass != null) {
					joEntity = CrudUtils.newJoEntity(entityName, entityClass);
					request.setAttribute("joEntity", joEntity);
				}
			}

			filePath = getGeneraterPath(filePath);
			File file = new File(IRender.ME.getRealPath(filePath));
			EntityModel entityModel = joEntity == null ? null : ModelFactory.getModelEntity((JoEntity) joEntity);
			// DIY生成
			if (request.getAttribute(DIY) == null) {
				// 非关联实体生成
				if (entityModel == null) {
					joEntity = null;
					if (BeanFactoryUtils.getEnvironment() != Environment.DEVELOP && Generator_Map_Token.containsKey(filePath)) {
						joEntity = Boolean.TRUE;
					}
				}

				// 如果生成文件没过期
				if (file.exists()) {
					if (entityModel == null) {
						if (joEntity != null) {
							return;
						}

					} else if (entityModel.lastModified() != null && entityModel.lastModified() <= file.lastModified()) {
						return;
					}
				}
			}

			// 检测开发文件是否存在
			includePath = getDeveloperPath(includePath);
			if (!new File(IRender.ME.getRealPath(includePath)).exists()) {
				return;
			}

			Object token = UtilAbsir.getToken(filePath, Generator_Map_Token);
			try {
				synchronized (token) {
					if (!Generator_Tokens.add(token)) {
						return;
					}

					final DeveloperGenerator generator = DeveloperGenerator.pushDeveloperGenerator(request);
					try {
						// 读取原文件定义信息
						StringBuilder fileBuilder = new StringBuilder();
						if (file.exists()) {
							final StringBuilder readBuilder = fileBuilder;
							final ObjectTemplate<String> gDefine = new ObjectTemplate<String>(null);
							try {
								HelperFile.doWithReadLine(file, new CallbackBreak<String>() {

									@Override
									public void doWith(String template) throws BreakException {
										// TODO Auto-generated method stub
										if (readBuilder.length() > 0) {
											readBuilder.append("\r\n");
										}

										readBuilder.append(template);
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
						FileOutputStream output = HelperFile.openOutputStream(file, entityModel == null ? null : entityModel.lastModified());
						if (output != null) {
							try {
								request.setAttribute("entityModel", entityModel);
								IRender.ME.rend(output, includePath, renders);
								fileBuilder = null;

								// 复制生成文件到开发环境
								IDeveloper.ME.copyDeveloper(file, filePath);

							} finally {
								if (output != null) {
									if (fileBuilder != null) {
										HelperIO.write(fileBuilder.toString(), output);
									}

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
	 * @param renders
	 * @throws IOException
	 */
	public static void generate(String filepath, Object... renders) throws IOException {
		generate(filepath, IRender.ME.getPath(renders), renders);
	}

	/**
	 * @param filepath
	 * @param includePath
	 * @param renders
	 * @throws IOException
	 */
	public static void generate(String filepath, String includePath, Object... renders) throws IOException {
		generateRenders(IRender.ME.getFullPath(filepath, renders), includePath, renders);
	}

	/**
	 * @param option
	 * @param types
	 * @param renders
	 * @throws IOException
	 */
	public static void includeExist(String option, List<String> types, Object... renders) throws IOException {
		includeExist(option, types, new String[] { "/WEB-INF/developer/type/" }, renders);
	}

	/**
	 * @param option
	 * @param types
	 * @param relativePaths
	 * @param renders
	 * @throws IOException
	 */
	public static void includeExist(String option, List<String> types, String[] relativePaths, Object... renders) throws IOException {
		ServletRequest request = KernelArray.getAssignable(renders, ServletRequest.class);
		for (String relativePath : relativePaths) {
			if (request != null) {
				request.removeAttribute(relativePath + option);
			}

			RenderUtils.includeExist(relativePath + option + "/base" + suffix, renders);
			for (String type : types) {
				if (RenderUtils.includeExist(relativePath + option + "/" + type + suffix, renders)) {
					request.setAttribute(relativePath + option, type);
					break;
				}
			}

			RenderUtils.includeExist(relativePath + option + "/type" + suffix, renders);
		}
	}

	/**
	 * @param option
	 * @param entityName
	 * @param renders
	 * @throws IOException
	 */
	public static void includeExist(String option, String entityName, Object... renders) throws IOException {
		includeExist(option, entityName, new String[] { "/WEB-INF/developer/bean/" }, renders);
	}

	/**
	 * @param option
	 * @param entityName
	 * @param relativePaths
	 * @param renders
	 * @throws IOException
	 */
	public static void includeExist(String option, String entityName, String[] relativePaths, Object... renders) throws IOException {
		for (String relativePath : relativePaths) {
			RenderUtils.includeExist(relativePath + option + "/" + entityName + suffix, renders);
		}
	}
}
