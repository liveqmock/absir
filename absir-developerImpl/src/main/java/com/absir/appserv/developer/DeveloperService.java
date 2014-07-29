/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.developer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.absir.appserv.developer.model.EntityModel;
import com.absir.appserv.developer.model.ModelFactory;
import com.absir.appserv.support.Developer;
import com.absir.appserv.support.developer.IDeveloper;
import com.absir.appserv.support.developer.IField;
import com.absir.appserv.support.developer.IModel;
import com.absir.appserv.support.developer.JCrud;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Started;
import com.absir.bean.inject.value.Value;
import com.absir.core.helper.HelperFile;
import com.absir.core.helper.HelperFileName;
import com.absir.core.kernel.KernelCollection;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class DeveloperService implements IDeveloper {

	@Value("developer.web")
	private static String developerWeb;

	/**
	 * 初始化开发环境
	 */
	@Started
	protected static void postConstruct() {
		if (developerWeb != null) {
			String developePath = HelperFileName.normalizeNoEndSeparator(developerWeb + "/../../../");
			String deployPath = HelperFileName.normalizeNoEndSeparator(BeanFactoryUtils.getBeanConfig().getClassPath() + "/../../");
			if ((HelperFileName.getName(developePath)).equals(HelperFileName.getName(deployPath))) {
				final String resourcesPath = developePath + "/src/main/resources/";
				if (HelperFile.directoryExists(resourcesPath)) {
					// 复制开发文件到开发环境
					if (IDeveloper.ME != null) {
						try {
							HelperFile.copyDirectoryOverWrite(IDeveloper.ME.getClass().getResource("/deploy"), new File(deployPath), false, null, true);
							HelperFile.copyDirectoryOverWrite(IDeveloper.ME.getClass().getResource("/deploy"), new File(developerWeb), false, null, true);

						} catch (Throwable e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}

					// 复制缓存文件到开发环境
					Developer.addListener(new CallbackTemplate<Entry<String, File>>() {

						@Override
						public void doWith(Entry<String, File> template) {
							// TODO Auto-generated method stub
							try {
								HelperFile.copyFile(template.getValue(), new File(resourcesPath + template.getKey()));

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

					return;
				}
			}

			developerWeb = null;
		}
	}

	/**
	 * @return the developerWeb
	 */
	public static String getDeveloperWeb() {
		return developerWeb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.developer.IDeveloper#getModelEntity(com.absir
	 * .appserv.support.entity.value.JoEntity)
	 */
	@Override
	public IModel getModelEntity(JoEntity joEntity) {
		// TODO Auto-generated method stub
		return ModelFactory.getModelEntity(joEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.CrudExecutor.ICrudDeveloper#getCrudFields(com.
	 * absir.appserv.support.entity.value.JoEntity)
	 */
	@Override
	public List<JCrudField> getCrudFields(JoEntity joEntity) {
		// TODO Auto-generated method stub
		EntityModel entityModel = ModelFactory.getModelEntity(joEntity);
		if (entityModel == null) {
			return null;
		}

		List<JCrudField> crudFields = new ArrayList<JCrudField>();
		for (IField field : entityModel.getFields()) {
			if (field.getName().indexOf(".") < 0 && (field.getCrudField().getjCrud() != null || field.getCrudField().getCruds() != null)) {
				crudFields.add(field.getCrudField());
			}
		}

		if (entityModel.getjCruds() != null) {
			for (JCrud jCrud : entityModel.getjCruds()) {
				JCrudField crudField = new JCrudField();
				crudField.setjCrud(jCrud);
				crudFields.add(crudField);
			}
		}

		return crudFields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.CrudUtils.ICrudDeveloper#getCrudFields(com.absir
	 * .appserv.support.entity.value.JoEntity, java.lang.String)
	 */
	@Override
	public String[] getCrudFields(JoEntity joEntity, String group) {
		// TODO Auto-generated method stub
		EntityModel entityModel = ModelFactory.getModelEntity(joEntity);
		if (entityModel == null) {
			return null;
		}

		List<IField> fields = entityModel.getGroupFields(group);
		if (fields == null) {
			return null;
		}

		List<String> crudFields = new ArrayList<String>();
		for (IField field : entityModel.getGroupFields(group)) {
			crudFields.add(field.getName());
		}

		return KernelCollection.toArray(crudFields, String.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.developer.IDeveloper#generate(java.lang.String,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	public void generate(String filepath, String includePath, Object... renders) throws IOException {
		// TODO Auto-generated method stub
		DeveloperUtils.generateRenders(filepath, includePath, renders);
	}
}
