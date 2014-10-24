/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月23日 下午2:45:27
 */
package com.absir.scene;

/**
 * @author absir
 *
 */
public interface ISceneBroadcast<T extends ISceneObject, E> {

	/**
	 * @param sceneObject
	 * @param key
	 * @param report
	 */
	public boolean broadcast(T sceneObject, E event, Object report);

}
