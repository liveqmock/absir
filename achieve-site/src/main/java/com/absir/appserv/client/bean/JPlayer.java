/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-28 上午9:26:44
 */
package com.absir.appserv.client.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.game.value.ILevelExp;
import com.absir.appserv.system.bean.JPlatformUser;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.dto.IBaseSerializer;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaClasses;
import com.absir.orm.value.JaColum;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("游戏管理") }, name = "角色")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class JPlayer extends JbBean implements ILevelExp {

	@JaLang("服务区")
	@JsonIgnore
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	@JaColum(indexs = @Index(columnList = "serverId,userId"))
	private long serverId;

	@JaLang("玩家ID")
	@JsonIgnore
	@JaClasses(JPlatformUser.class)
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	private long userId;

	@JaLang("角色名称")
	@JaEdit(groups = { JaEdit.GROUP_SUGGEST })
	@JaColum(indexs = @Index(columnList = "serverId,name", unique = true))
	private String name;

	@JaLang("角色性别")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	private boolean sex;

	@JaLang("用户签名")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	private String sign;

	@JaLang("金钱")
	private int money;

	@JaLang("宝石数量")
	private int diamond;

	@JaLang("角色等级")
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

	@JaLang("队伍领导力")
	private int cost;

	@JaLang("最大领导力")
	private int maxCost;

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

	@JaLang("卡牌0号位")
	@JsonSerialize(using = IBaseSerializer.class)
	@NotFound(action = NotFoundAction.IGNORE)
	@JaColum(foreignKey = false)
	@OneToOne(fetch = FetchType.EAGER)
	private JCard card0;

	@JaLang("卡牌1号位")
	@JsonSerialize(using = IBaseSerializer.class)
	@NotFound(action = NotFoundAction.IGNORE)
	@JaColum(foreignKey = false)
	@OneToOne(fetch = FetchType.EAGER)
	private JCard card1;

	@JaLang("卡牌2号位")
	@JsonSerialize(using = IBaseSerializer.class)
	@NotFound(action = NotFoundAction.IGNORE)
	@JaColum(foreignKey = false)
	@OneToOne(fetch = FetchType.EAGER)
	private JCard card2;

	@JaLang("卡牌3号位")
	@JsonSerialize(using = IBaseSerializer.class)
	@NotFound(action = NotFoundAction.IGNORE)
	@JaColum(foreignKey = false)
	@OneToOne(fetch = FetchType.EAGER)
	private JCard card3;

	@JaLang("卡牌4号位")
	@JsonSerialize(using = IBaseSerializer.class)
	@NotFound(action = NotFoundAction.IGNORE)
	@JaColum(foreignKey = false)
	@OneToOne(fetch = FetchType.EAGER)
	private JCard card4;

	// @JaLang("卡牌5号位")
	// @NotFound(action = NotFoundAction.IGNORE)
	// @JaColum(foreignKey = false)
	// @OneToOne(fetch = FetchType.EAGER)
	// private JCard card5;

	/** CARD_FORMATION_COUNT */
	public static final int CARD_FORMATION_COUNT = 5;

	@JaLang("冠军次数")
	private int championNumber;

	@JaLang("亚军次数")
	private int secondNumber;

	@JaLang("季军次数")
	private int thirdNumber;

	@JaLang("答题情况")
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonMap")
	private Map<Long, JbAnswer> answers;

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
	 * @return the sex
	 */
	public boolean isSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(boolean sex) {
		this.sex = sex;
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
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * @return the maxCost
	 */
	public int getMaxCost() {
		return maxCost;
	}

	/**
	 * @param maxCost
	 *            the maxCost to set
	 */
	public void setMaxCost(int maxCost) {
		this.maxCost = maxCost;
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
	 * @return the card0
	 */
	public JCard getCard0() {
		return card0;
	}

	/**
	 * @param card0
	 *            the card0 to set
	 */
	public void setCard0(JCard card0) {
		this.card0 = card0;
	}

	/**
	 * @return the card1
	 */
	public JCard getCard1() {
		return card1;
	}

	/**
	 * @param card1
	 *            the card1 to set
	 */
	public void setCard1(JCard card1) {
		this.card1 = card1;
	}

	/**
	 * @return the card2
	 */
	public JCard getCard2() {
		return card2;
	}

	/**
	 * @param card2
	 *            the card2 to set
	 */
	public void setCard2(JCard card2) {
		this.card2 = card2;
	}

	/**
	 * @return the card3
	 */
	public JCard getCard3() {
		return card3;
	}

	/**
	 * @param card3
	 *            the card3 to set
	 */
	public void setCard3(JCard card3) {
		this.card3 = card3;
	}

	/**
	 * @return the card4
	 */
	public JCard getCard4() {
		return card4;
	}

	/**
	 * @param card4
	 *            the card4 to set
	 */
	public void setCard4(JCard card4) {
		this.card4 = card4;
	}

	/**
	 * @param position
	 * @return
	 */
	public JCard getCard(int position) {
		switch (position) {
		case 0:
			return card0;
		case 1:
			return card1;
		case 2:
			return card2;
		case 3:
			return card3;
		case 4:
			return card4;
		}

		return null;
	}

	/**
	 * @param card
	 * @param position
	 */
	public void setCard(JCard card, int position) {
		cards = null;
		switch (position) {
		case 0:
			if (card == null) {
				throw new ServerException(ServerStatus.IN_FAILED);
			}

			this.card = card.getCardDefine().getId();
			card0 = card;
			break;
		case 1:
			card1 = card;
			break;
		case 2:
			card2 = card;
			break;
		case 3:
			card3 = card;
			break;
		case 4:
			card4 = card;
			break;
		// case 5:
		// card5 = card;
		// break;
		default:
			break;
		}
	}

	@Transient
	private List<JCard> cards;

	/**
	 * @return
	 */
	@JsonIgnore
	public List<JCard> getCards() {
		List<JCard> cardList = cards;
		if (cardList == null) {
			cardList = new ArrayList<JCard>();
			if (card0 != null) {
				cardList.add(card0);
			}

			if (card1 != null) {
				cardList.add(card1);
			}

			if (card2 != null) {
				cardList.add(card2);
			}

			if (card3 != null) {
				cardList.add(card3);
			}

			if (card4 != null) {
				cardList.add(card4);
			}

			cards = cardList;
		}

		return cards;
	}

	/**
	 * @param card
	 * @return
	 */
	public boolean containCard(JCard card) {
		for (JCard ele : getCards()) {
			if (ele == card) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return the championNumber
	 */
	public int getChampionNumber() {
		return championNumber;
	}

	/**
	 * @param championNumber
	 *            the championNumber to set
	 */
	public void setChampionNumber(int championNumber) {
		this.championNumber = championNumber;
	}

	/**
	 * @return the secondNumber
	 */
	public int getSecondNumber() {
		return secondNumber;
	}

	/**
	 * @param secondNumber
	 *            the secondNumber to set
	 */
	public void setSecondNumber(int secondNumber) {
		this.secondNumber = secondNumber;
	}

	/**
	 * @return the thirdNumber
	 */
	public int getThirdNumber() {
		return thirdNumber;
	}

	/**
	 * @param thirdNumber
	 *            the thirdNumber to set
	 */
	public void setThirdNumber(int thirdNumber) {
		this.thirdNumber = thirdNumber;
	}

	/**
	 * @return the answers
	 */
	public Map<Long, JbAnswer> getAnswers() {
		return answers;
	}

	/**
	 * @param answers
	 *            the answers to set
	 */
	public void setAnswers(Map<Long, JbAnswer> answers) {
		this.answers = answers;
	}
}
