/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-6 下午5:37:12
 */
package com.absir.appserv.system.crud;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.crud.ICrudProcessorInput;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.helper.HelperEncrypt;
import com.absir.core.kernel.KernelString;
import com.absir.core.util.UtilAccessor;
import com.absir.core.util.UtilAccessor.Accessor;
import com.absir.orm.value.JoEntity;
import com.absir.property.PropertyErrors;
import com.absir.server.in.Input;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
public class PasswordCrudFactory implements ICrudFactory {

	/** PASSWORD_PROCESSOR */
	private final ICrudProcessor PASSWORD_PROCESSOR = new ICrudProcessorInput<String>() {

		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler crudHandler, JiUserBase user) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean isMultipart() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String crud(CrudProperty crudProperty, PropertyErrors errors, CrudHandler handler, JiUserBase user, Input input) {
			// TODO Auto-generated method stub
			if (input instanceof InputRequest) {
				return ((InputRequest) input).getRequest().getParameter(handler.getFilter().getPropertyPath() + "@");
			}

			return null;
		}

		/**
		 * @param crudProperty
		 * @param entity
		 * @param handler
		 * @param user
		 * @param requestBody
		 */
		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler handler, JiUserBase user, String requestBody) {
			// TODO Auto-generated method stub
			if (!KernelString.isEmpty(requestBody)) {
				Accessor accessor = UtilAccessor.getAccessorProperty(entity.getClass(), "salt");
				String salt = null;
				if (accessor != null) {
					salt = Integer.toHexString(requestBody.hashCode());
					if (!accessor.set(entity, salt)) {
						salt = null;
					}
				}

				requestBody = getPasswordEncrypt(requestBody, salt);
				crudProperty.set(entity, requestBody);
			}
		}
	};

	/**
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String getPasswordEncrypt(String password, String salt) {
		return HelperEncrypt.encryptionMD5(password, salt == null ? null : salt.getBytes());
	}

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
		return PASSWORD_PROCESSOR;
	}

}
