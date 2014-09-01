/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年8月28日 上午10:10:30
 */
package com.absir.appserv.support.web.tag;

import org.beetl.core.Tag;

import com.absir.appserv.support.web.value.BaTag;

/**
 * @author absir
 *
 */
@BaTag
public class BindTag extends Tag {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beetl.core.Tag#render()
	 */
	@Override
	public void render() {
		// TODO Auto-generated method stub
		ctx.template.binding((String) args[0], getBodyContent());
	}
}
