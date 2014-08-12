/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-10 下午6:37:40
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.JPlayer;
import com.absir.appserv.system.bean.base.JbBase;

/**
 * @author absir
 * 
 */
public class OPlayer extends JbBase {

	/** id */
	private Long id;

	/** name */
	private String name;

	/** cardId */
	private Long cardId;

	/**
	 * @param player
	 */
	public OPlayer(JPlayer player) {
		if (player != null) {
			id = player.getId();
			name = player.getName();
			cardId = player.getCard0().getId();
		}
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the cardId
	 */
	public Long getCardId() {
		return cardId;
	}

	/**
	 * @param cardId
	 *            the cardId to set
	 */
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
}
