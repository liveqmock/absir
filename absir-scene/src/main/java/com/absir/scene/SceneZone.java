/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月20日 上午11:03:35
 */
package com.absir.scene;

import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.context.bean.IStep;
import com.absir.core.util.UtilLinked;

/**
 * @author absir
 *
 */
public class SceneZone<T extends ISceneObject, E> implements IStep, ISceneBroadcast<T, E> {

	/** sceneObjects */
	@JsonIgnore
	private UtilLinked<T> sceneObjects = new UtilLinked<T>();

	/** sceneBroadCasts */
	@JsonIgnore
	private UtilLinked<ISceneBroadcast<T, E>> sceneBroadCasts = new UtilLinked<ISceneBroadcast<T, E>>();

	/**
	 * @param sceneObject
	 */
	public void addSceneObject(T sceneObject) {
		sceneObjects.add(sceneObject);
	}

	/**
	 * @param sceneObject
	 */
	public void removeSceneObject(T sceneObject) {
		sceneObjects.remove(sceneObject);
	}

	/**
	 * @param sceneObject
	 */
	public void addSceneBroadcast(ISceneBroadcast<T, E> sceneBroadcast) {
		sceneBroadCasts.add(sceneBroadcast);
	}

	/**
	 * @param sceneBroadcast
	 */
	public void removeSceneBroadcast(ISceneBroadcast<T, E> sceneBroadcast) {
		sceneBroadCasts.remove(sceneBroadcast);
	}

	/**
	 * @return
	 */
	public Iterator<T> iterator() {
		return sceneObjects.iterator();
	}

	/**
	 * @param contextTime
	 */
	public boolean stepDone(long contextTime) {
		sceneBroadCasts.sync();
		List<T> adds = sceneObjects.syncAdds();
		List<T> removes = sceneObjects.syncRemoves();
		for (T add : adds) {
			if (add.isSensory()) {
				broadcast(add, null, add.getStatusObject());
			}
		}

		for (T remove : removes) {
			if (remove.isSensory()) {
				broadcast(remove, null, null);
			}
		}

		Iterator<T> iterator = sceneObjects.iterator();
		T sceneObject;
		boolean sensory;
		while (iterator.hasNext()) {
			sceneObject = iterator.next();
			sensory = sceneObject.isSensory();
			if (sceneObject.stepDone(contextTime)) {
				if (sensory != sceneObject.isSensory()) {
					if (sensory) {
						broadcast(sceneObject, null, null);

					} else {
						broadcast(sceneObject, null, sceneObject.getStatusObject());
					}
				}

			} else {
				iterator.remove();
				if (sensory) {
					broadcast(sceneObject, null, null);
				}
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.scene.ISceneBroadcast#broadcast(com.absir.scene.ISceneObject,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean broadcast(T sceneObject, E event, Object report) {
		// TODO Auto-generated method stub
		Iterator<ISceneBroadcast<T, E>> iterator = sceneBroadCasts.iterator();
		while (iterator.hasNext()) {
			if (!iterator.next().broadcast(sceneObject, event, report)) {
				iterator.remove();
			}
		}

		return true;
	}

}
