/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月20日 上午10:59:42
 */
package com.absir.scene;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

/**
 * @author absir
 *
 */
public class SceneTile {

	/** scenePlayers */
	private Queue<ScenePlayer> scenePlayers = new SynchronousQueue<ScenePlayer>();

}
