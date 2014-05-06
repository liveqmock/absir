/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-24 下午2:23:23
 */
package com.absir.appserv.system.bean.dto;

import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdKeyDeserializer;

/**
 * @author absir
 * 
 */
public class XlsBaseKeyDeserialize extends StdKeyDeserializer {

	/**
	 * @param cls
	 */
	protected XlsBaseKeyDeserialize(Class<?> cls) {
		super(cls);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.deser.std.StdKeyDeserializer#_parse(java.lang.String, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	protected Object _parse(String key, DeserializationContext ctxt) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

}
