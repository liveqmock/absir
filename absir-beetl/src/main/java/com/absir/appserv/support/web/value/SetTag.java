/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年8月1日 下午5:06:09
 */
package com.absir.appserv.support.web.value;

import org.beetl.core.Tag;

/**
 * @author absir
 *
 */
@BaTag
public class SetTag extends Tag {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beetl.core.Tag#render()
	 */
	@Override
	public void render() {
		// TODO Auto-generated method stub
		ctx.set((String) args[0], getBodyContent().getBody());
	}

}
