/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-28 上午9:26:44
 */
package com.absir.appserv.game.bean;

import javax.persistence.Index;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.game.value.ILevelExp;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaClasses;
import com.absir.orm.value.JaColum;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public class JbPlayer extends JbBean implements ILevelExp {

	@JaLang("服务区")
	@JsonIgnore
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	@JaColum(indexs = @Index(columnList = "serverId,userId"))
	private long serverId;

	@JaLang("用户ID")
	@JsonIgnore
	@JaClasses(JPlatformUser.class)
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	private long userId;

	@JaLang("名称")
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	@JaColum(indexs = @Index(columnList = "serverId,name", unique = true))
	private String name;

	@JaLang("性别")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	private int gender;

	@JaLang("签名")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	private String sign;

	@JaLang("金钱")
	private int money;

	@JaLang("宝石")
	private int diamond;

	@JaLang("等级")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	private int level;

	@JaLang("经验")
	private int exp;

	@JaLang("升级经验")
	private int maxExp;

	@JaLang("行动力")
	private int ep;

	@JaLang("最大行动力")
	private int maxEp;

	@JaLang("卡牌数量")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	private int cardNumber;

	@JaLang("最大卡牌数量")
	private int maxCardNumber;

	@JaLang("好友数量")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	private int friendNumber;

	@JaLang("最大好友数量")
	private int maxFriendNumber;

	@JaLang("友情点数")
	private int friendshipNumber;

	@JaLang("在线状态")
	private transient boolean online;

	@JaLang("队长卡牌")
	private int card;

	/**
	 * @return the serverId
	 */
	public long getServerId() {
		return serverId;
	}

	/**
	 * @param serverId
	 *            the serverId to set
	 */
	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
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
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign
	 *            the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            the money to set
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * @return the diamond
	 */
	public int getDiamond() {
		return diamond;
	}

	/**
	 * @param diamond
	 *            the diamond to set
	 */
	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the exp
	 */
	public int getExp() {
		return exp;
	}

	/**
	 * @param exp
	 *            the exp to set
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}

	/**
	 * @return the maxExp
	 */
	public int getMaxExp() {
		return maxExp;
	}

	/**
	 * @param maxExp
	 *            the maxExp to set
	 */
	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
	}

	/**
	 * @return the ep
	 */
	public int getEp() {
		return ep;
	}

	/**
	 * @param ep
	 *            the ep to set
	 */
	public void setEp(int ep) {
		this.ep = ep;
	}

	/**
	 * @return the maxEp
	 */
	public int getMaxEp() {
		return maxEp;
	}

	/**
	 * @param maxEp
	 *            the maxEp to set
	 */
	public void setMaxEp(int maxEp) {
		this.maxEp = maxEp;
	}

	/**
	 * @return the cardNumber
	 */
	public int getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber
	 *            the cardNumber to set
	 */
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * @return the maxCardNumber
	 */
	public int getMaxCardNumber() {
		return maxCardNumber;
	}

	/**
	 * @param maxCardNumber
	 *            the maxCardNumber to set
	 */
	public void setMaxCardNumber(int maxCardNumber) {
		this.maxCardNumber = maxCardNumber;
	}

	/**
	 * @return the friendNumber
	 */
	public int getFriendNumber() {
		return friendNumber;
	}

	/**
	 * @param friendNumber
	 *            the friendNumber to set
	 */
	public void setFriendNumber(int friendNumber) {
		this.friendNumber = friendNumber;
	}

	/**
	 * @return the maxFriendNumber
	 */
	public int getMaxFriendNumber() {
		return maxFriendNumber;
	}

	/**
	 * @param maxFriendNumber
	 *            the maxFriendNumber to set
	 */
	public void setMaxFriendNumber(int maxFriendNumber) {
		this.maxFriendNumber = maxFriendNumber;
	}

	/**
	 * @return the friendshipNumber
	 */
	public int getFriendshipNumber() {
		return friendshipNumber;
	}

	/**
	 * @param friendshipNumber
	 *            the friendshipNumber to set
	 */
	public void setFriendshipNumber(int friendshipNumber) {
		this.friendshipNumber = friendshipNumber;
	}

	/**
	 * @return the online
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * @param online
	 *            the online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * @return the card
	 */
	public int getCard() {
		return card;
	}

	/**
	 * @param card
	 *            the card to set
	 */
	public void setCard(int card) {
		this.card = card;
	}

	/**
	 * @return
	 */
	public int getMaxCardFormation() {
		return 5;
	}

	/**
	 * @param card
	 * @return
	 */
	public boolean containCard(JbCard card) {
		return true;
	}

	/**
	 * @param position
	 * @return
	 */
	public JbCard getCard(int position) {
		return null;
	}

	/**
	 * @param card
	 * @param position
	 */
	public void setCard(JbCard card, int position) {
	}

	public JbCard[] getCards() {
		return null;
	}
}
