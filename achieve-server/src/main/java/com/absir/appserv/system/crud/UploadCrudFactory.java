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
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.bean.inject.value.Value;
import com.absir.core.helper.HelperFile;
import com.absir.core.helper.HelperFileName;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelString;
import com.absir.orm.value.JoEntity;
import com.absir.property.PropertyErrors;
import com.absir.server.in.Input;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
public class UploadCrudFactory implements ICrudFactory {

	/** uploadPath */
	private static String uploadPath;

	/** uploadUrl */
	private static String uploadUrl;

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadCrudFactory.class);

	/**
	 * @return
	 */
	public static String getUploadPath() {
		return uploadPath;
	}

	/**
	 * @return
	 */
	public static String getUploadUrl() {
		return uploadUrl;
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
	 * @param uploadPath
	 */
	@Inject(type = InjectType.Selectable)
	protected void setUploadDir(@Value(value = "${resource.upload.path}", defaultValue = "upload") String uploadPath, @Value(value = "${resource.upload.url}", defaultValue = "upload") String uploadUrl) {
		if (KernelString.isEmpty(uploadPath)) {
			return;
		}

		UploadCrudFactory.uploadPath = HelperFileName.normalizeNoEndSeparator(BeanFactoryUtils.getBeanConfig().getResourcePath() + uploadPath) + "/";
		UploadCrudFactory.uploadUrl = HelperFileName.normalizeNoEndSeparator(uploadUrl);
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

				uploadFile = "upload/" + HelperRandom.randSecendId() + "." + HelperFileName.getExtension(requestBody.getName());
				try {
					HelperFile.write(new File(uploadPath + uploadFile), requestBody.getInputStream());

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
