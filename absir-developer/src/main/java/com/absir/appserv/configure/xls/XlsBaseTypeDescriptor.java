/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-14 下午12:54:06
 */
package com.absir.appserv.configure.xls;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class XlsBaseTypeDescriptor extends AbstractTypeDescriptor<XlsBase> {

	/**
	 * @param type
	 */
	protected XlsBaseTypeDescriptor(Class<? extends XlsBase> type) {
		super((Class) type);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.type.descriptor.java.JavaTypeDescriptor#toString(java.lang
	 * .Object)
	 */
	@Override
	public String toString(XlsBase value) {
		// TODO Auto-generated method stub
		return KernelString.valueOf(value.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.type.descriptor.java.JavaTypeDescriptor#fromString(java
	 * .lang.String)
	 */
	@Override
	public XlsBase fromString(String string) {
		// TODO Auto-generated method stub
		return XlsUtils.findXlsBean(getJavaTypeClass(), string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.type.descriptor.java.JavaTypeDescriptor#unwrap(java.lang
	 * .Object, java.lang.Class, org.hibernate.type.descriptor.WrapperOptions)
	 */
	@Override
	public <X> X unwrap(XlsBase value, Class<X> type, WrapperOptions options) {
		// TODO Auto-generated method stub
		if (value == null) {
			return null;
		}

		return KernelDyna.to(value.getId(), type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.type.descriptor.java.JavaTypeDescriptor#wrap(java.lang.
	 * Object, org.hibernate.type.descriptor.WrapperOptions)
	 */
	@Override
	public <X> XlsBase wrap(X value, WrapperOptions options) {
		// TODO Auto-generated method stub
		if (value == null) {
			return null;
		}

		return XlsUtils.findXlsBean(getJavaTypeClass(), value);
	}
}
