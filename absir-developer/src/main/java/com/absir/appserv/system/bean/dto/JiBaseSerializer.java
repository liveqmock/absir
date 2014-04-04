/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-24 下午2:23:23
 */
package com.absir.appserv.system.bean.dto;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.absir.appserv.system.bean.proxy.JiBase;

/**
 * @author absir
 * 
 */
public class JiBaseSerializer extends JsonSerializer<JiBase> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
	 * org.codehaus.jackson.JsonGenerator,
	 * org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(JiBase value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		jgen.writeObject(value.getId());
	}
}
