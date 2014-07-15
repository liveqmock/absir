/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-7-9 下午3:36:15
 */
package com.absir.appserv.system.crud;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.crud.ICrudProcessorInput;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.crud.value.IUploadRule;
import com.absir.appserv.system.crud.value.UploadRule;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.appserv.system.helper.HelperString;
import com.absir.appserv.system.service.utils.CrudServiceUtils;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Started;
import com.absir.bean.inject.value.Value;
import com.absir.core.base.IBase;
import com.absir.core.helper.HelperFile;
import com.absir.core.helper.HelperFileName;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelString;
import com.absir.core.util.UtilAccessor.Accessor;
import com.absir.orm.value.JoEntity;
import com.absir.property.PropertyErrors;
import com.absir.server.in.Input;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class UploadCrudFactory implements ICrudFactory {

	/** uploadUrl */
	private static String uploadUrl;

	/** uploadPath */
	private static String uploadPath;

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadCrudFactory.class);

	/**
	 * @return
	 */
	public static String getUploadUrl() {
		return uploadUrl;
	}

	/**
	 * @return
	 */
	public static String getUploadPath() {
		return uploadPath;
	}

	/**
	 * @param name
	 * @param fileItem
	 * @return
	 */
	public static FileItem getUploadFile(InputRequest input, String name) {
		List<FileItem> fileItems = input.parseParameterMap().get(name);
		return fileItems == null || fileItems.isEmpty() ? null : fileItems.get(0);
	}

	/**
	 * @param uploadUrl
	 * @param uploadPath
	 */
	@Started
	protected void setUploadUrl(@Value(value = "resource.upload.url", defaultValue = "upload") String uploadUrl, @Value(value = "resource.upload.path", defaultValue = "upload") String uploadPath) {
		if (KernelString.isEmpty(uploadPath)) {
			return;
		}

		UploadCrudFactory.uploadUrl = uploadUrl;
		UploadCrudFactory.uploadPath = HelperFileName.normalizeNoEndSeparator(BeanFactoryUtils.getBeanConfig().getResourcePath() + uploadPath) + "/";
	}

	/**
	 * @author absir
	 * 
	 */
	public static class MultipartUploader {

		/** minSize */
		private long minSize;

		/** maxSize */
		private long maxSize;

		/** extensions */
		private String[] extensions;

		/** ruleName */
		private String ruleName;

		/**
		 * @param parameters
		 */
		public MultipartUploader(Object[] parameters) {
			int last = parameters.length - 1;
			if (last > 2) {
				last = 2;
			}

			for (int i = 0; last >= 0; i++, last--) {
				switch (i) {
				case 0:
					Object extension = parameters[last];
					if (extension instanceof String && !"".equals(extension)) {
						extensions = ((String) extension).toLowerCase().split(",");
					}
					break;

				case 1:
					maxSize = (long) (KernelDyna.to(parameters[last], float.class) * 1024);
					break;

				case 3:
					minSize = (long) (KernelDyna.to(parameters[last], float.class) * 1024);
					break;

				default:
					break;
				}
			}
		}

		/**
		 * @param field
		 * @param file
		 * @param errors
		 */
		public void verify(String field, FileItem file, PropertyErrors errors) {
			if (extensions != null && !KernelArray.contain(extensions, HelperFileName.getExtension(file.getName()).toLowerCase())) {
				errors.rejectValue(field, "error file type", null);
				return;
			}

			if (maxSize > 0 && file.getSize() > maxSize) {
				errors.rejectValue(field, "max file size", null);
				return;
			}

			if (minSize > 0 && file.getSize() < minSize) {
				errors.rejectValue(field, "min file size", null);
				return;
			}
		}
	}

	/**
	 * @param field
	 * @param file
	 * @param parameters
	 * @param errors
	 */
	public static void verifyMultipartFile(String field, FileItem file, Object[] parameters, PropertyErrors errors) {
		if (parameters.length > 0) {
			Object uploadVerify = parameters[0];
			if (!(uploadVerify instanceof MultipartUploader)) {
				synchronized (parameters) {
					if (!(parameters[0] instanceof MultipartUploader)) {
						parameters[0] = uploadVerify = new MultipartUploader(parameters);
					}
				}
			}

			((MultipartUploader) uploadVerify).verify(field, file, errors);
		}
	}

	/**
	 * @author absir
	 * 
	 */
	public static class MultipartFileProcessor implements ICrudProcessorInput<FileItem> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.crud.ICrudProcessorRequest#isMultipart()
		 */
		@Override
		public boolean isMultipart() {
			// TODO Auto-generated method stub
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.crud.ICrudProcessorInput#crud(com.absir.appserv
		 * .crud.CrudProperty, com.absir.property.PropertyErrors,
		 * com.absir.appserv.crud.CrudHandler,
		 * com.absir.appserv.system.bean.proxy.JiUserBase,
		 * com.absir.server.in.Input)
		 */
		@Override
		public FileItem crud(CrudProperty crudProperty, PropertyErrors errors, CrudHandler handler, JiUserBase user, Input input) {
			// TODO Auto-generated method stub
			String field = handler.getFilter().getPropertyPath();
			if (input instanceof InputRequest) {
				FileItem file = getUploadFile((InputRequest) input, field + "_file");
				if (file != null && !KernelString.isEmpty(file.getName())) {
					verifyMultipartFile(field, file, crudProperty.getjCrud().getParameters(), errors);
					return file;
				}
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.crud.ICrudProcessorRequest#crud(com.absir.appserv
		 * .crud.CrudProperty, java.lang.Object,
		 * com.absir.appserv.crud.CrudHandler,
		 * com.absir.appserv.system.bean.proxy.JiUserBase, java.lang.Object)
		 */
		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler handler, JiUserBase user, FileItem requestBody) {
			// TODO Auto-generated method stub
			if (requestBody != null) {
				String uploadFile = (String) crudProperty.get(entity);
				if (!KernelString.isEmpty(uploadFile)) {
					HelperFile.deleteQuietly(new File(uploadPath + uploadFile));
				}

				InputStream uploadStream = null;
				String extensionName = HelperFileName.getExtension(requestBody.getName());
				try {
					Object[] parameters = crudProperty.getjCrud().getParameters();
					MultipartUploader multipartUploader = parameters.length == 0 ? null : (MultipartUploader) parameters[0];
					if (multipartUploader != null) {
						if (multipartUploader.ruleName == null) {
							Accessor accessor = crudProperty.getAccessor();
							if (accessor != null) {
								UploadRule uploadRule = accessor.getAnnotation(UploadRule.class, false);
								if (uploadRule != null) {
									multipartUploader.ruleName = uploadRule.value();
								}
							}

							if (multipartUploader.ruleName == null) {
								multipartUploader.ruleName = "";
							}
						}

						if ("".equals(multipartUploader.ruleName)) {
							multipartUploader = null;

						} else {
							String identity = "";
							if (entity instanceof IBase) {
								Serializable id = ((IBase<?>) entity).getId();
								if (id == null) {
									JoEntity joEntity = handler.getCrudEntity().getJoEntity();
									if (joEntity != null && joEntity.getEntityName() != null) {
										CrudServiceUtils.merge(joEntity.getEntityName(), handler.getRoot(), handler.getCrud() == Crud.CREATE, user, null);
									}
								}

								id = ((IBase<?>) entity).getId();
								if (id != null) {
									identity = id.toString();
								}
							}

							uploadFile = HelperString
									.replaceEach(multipartUploader.ruleName, new String[] { ":name", ":id", ":ext" }, new String[] { crudProperty.getName(), identity, extensionName });
						}
					}

					if (multipartUploader == null && entity instanceof IUploadRule) {
						IUploadRule uploadRule = (IUploadRule) entity;
						uploadFile = uploadRule.getUploadRuleName(crudProperty.getName(), extensionName);
						if (uploadFile != null) {
							uploadStream = uploadRule.proccessInputStream(crudProperty.getName(), requestBody.getInputStream(), extensionName);
						}
					}

					if (uploadFile == null) {
						uploadFile = HelperRandom.randSecendId() + "." + HelperFileName.getExtension(requestBody.getName());
					}

					if (uploadStream == null) {
						uploadStream = requestBody.getInputStream();
					}

					HelperFile.write(new File(uploadPath + uploadFile), uploadStream);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					LOGGER.error("upload error", e);
				}

				crudProperty.set(entity, uploadFile);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.crud.ICrudProcessor#crud(com.absir.appserv.crud
		 * .CrudProperty, java.lang.Object, com.absir.appserv.crud.CrudHandler,
		 * com.absir.appserv.system.bean.proxy.JiUserBase)
		 */
		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler crudHandler, JiUserBase user) {
			// TODO Auto-generated method stub
			if (crudHandler.getCrud() == Crud.DELETE) {
				String uploadFile = (String) crudProperty.get(entity);
				if (!KernelString.isEmpty(uploadFile)) {
					HelperFile.deleteQuietly(new File(BeanFactoryUtils.getBeanConfig().getResourcePath() + uploadFile));
				}
			}
		}
	}

	/** Multipart_File_PROCESSOR */
	private static final ICrudProcessor Multipart_File_PROCESSOR = new MultipartFileProcessor();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudFactory#getProcessor(com.absir.appserv.support
	 * .entity.value.JoEntity, com.absir.appserv.support.developer.JCrudField)
	 */
	@Override
	public ICrudProcessor getProcessor(JoEntity joEntity, JCrudField crudField) {
		// TODO Auto-generated method stub
		return Multipart_File_PROCESSOR;
	}

}
