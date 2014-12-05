/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午12:13:09
 */
package com.absir.appserv.game.service;

import java.io.IOException;
import java.util.List;

import com.absir.appserv.game.bean.JbDiamond;
import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.game.context.PlayerServiceBase;
import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.appserv.system.service.IAPService;
import com.absir.appserv.system.service.IPayProccessor;
import com.absir.appserv.system.service.utils.PayUtils;
import com.absir.async.value.Async;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.context.schedule.value.Schedule;
import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.orm.transaction.value.Transaction;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Base
@Bean
public class PayService implements IPayProccessor {

	/** ME */
	public static final PayService ME = BeanFactoryUtils.get(PayService.class);

	/**
	 * 生成购买宝石订单
	 * 
	 * @param playerId
	 * @param diamond
	 * @param count
	 * @return
	 */
	private JPayTrade buyDiamond(Long playerId, JbDiamond diamond, int count) {
		if (diamond == null || count <= 0) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		JPayTrade payTrade = new JPayTrade();
		payTrade.setId(HelperRandom.randSecendId(System.currentTimeMillis(), 6, playerId.hashCode()));
		payTrade.setUid(playerId.toString());
		payTrade.setName("JDiamond");
		payTrade.setNameData(String.valueOf(diamond.getDiamond() * count));
		payTrade.setAmount(diamond.getPrice() * count);
		return payTrade;
	}

	/**
	 * 购买宝石
	 * 
	 * @param player
	 * @param diamond
	 * @param count
	 * @return
	 */
	@Transaction(rollback = Exception.class)
	public JPayTrade buyDiamond(JbPlayer player, JbDiamond diamond, int count) {
		JPayTrade payTrade = buyDiamond(player.getId(), diamond, count);
		BeanDao.getSession().persist(payTrade);
		return payTrade;
	}

	/**
	 * 购买宝石(IAP)
	 * 
	 * @param diamond
	 * @param count
	 * @param receipt
	 * @param playerContext
	 * @throws IOException
	 */
	@Transaction(rollback = Exception.class)
	public void butInItunes(JbDiamond diamond, int count, String receipt, JbPlayerContext playerContext) throws IOException {
		JPayTrade payTrade = buyDiamond(playerContext.getPlayer().getId(), diamond, count);
		payTrade.setPlatform(IAPService.PLAT_FORM_NAME);
		payTrade.setPlatformData(IAPService.getPlatformData(diamond.getId(), count));
		BeanDao.getSession().persist(payTrade);
	}

	/**
	 * 定时处理未完成支付订单
	 */
	@Async(notifier = true)
	@Schedule(fixedDelay = 600000)
	@Transaction
	protected void proccessPayed() {
		List<JPayTrade> payTrades = QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o FROM JPayTrade o WHERE o.status = ? AND o.platform is not NULL", JePayStatus.PAYED).list();
		for (JPayTrade payTrade : payTrades) {
			PayUtils.proccess(payTrade.getPlatform(), payTrade);
		}
	}

	/*
	 * 完成订单
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.pay.IPayProccessor#proccess(com.absir.appserv
	 * .system.bean.JPayTrade)
	 */
	@Transaction(rollback = Exception.class)
	@Override
	public Object proccess(final JPayTrade payTrade) {
		// TODO Auto-generated method stub
		if ("JDiamond".equals(payTrade.getName())) {
			Long playerId = KernelDyna.toLong(payTrade.getUid());
			final int diamond = KernelDyna.toInteger(payTrade.getNameData());
			PlayerServiceBase.ME.modifyPlayer(playerId, new CallbackTemplate<JbPlayer>() {

				@Override
				public void doWith(JbPlayer template) {
					// TODO Auto-generated method stub
					JbPlayerContext.COMPONENT.consumeDiamond(template, diamond);
				}

			});

			return diamond;
		}

		return null;
	}
}
