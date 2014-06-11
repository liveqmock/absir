<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<p> <label>关联用户:</label> <input name="playerId" type="text" size="30" value="${entity.playerId}" /> </p>
<p> <label>金钱奖励:</label> <input name="rewardBean.money" type="text" size="30" value="${entity.rewardBean.money}" /> </p>
<p> <label>宝石奖励:</label> <input name="rewardBean.diamond" type="text" size="30" value="${entity.rewardBean.diamond}" /> </p>
<p> <label>卡牌奖励:</label> <input name="rewardBean.cardRewards" type="text" size="30" value="${entity.rewardBean.cardRewards}" /> </p>
<p> <label>道具奖励:</label> <input name="rewardBean.propRewards" type="text" size="30" value="${entity.rewardBean.propRewards}" /> </p>
<p> <label>奖励说明:</label> <input name="name" type="text" size="30" value="${entity.name}" /> </p>
<c:set value="${entity.type}" var="value"/>
<p> <label>奖励类型:</label> <select name="type" value="<%=WebJsplUtils.enumValue(pageContext.getAttribute("value"))%>" class=" combox"><option value="">请选择</option><option value="ACTIVITY">活动</option> <option value="PVP">对战</option> <option value="SYSTEM">系统</option> <option value="PAY">充值</option> <option value="WAR">团战</option></select> </p>
<p> <label>奖励参数:</label> <input name="data" type="text" size="30" value="${entity.data}" /> </p>
<c:set var="value" value="${entity.createTime}" />
<p> <label>创建时间:</label> 
<input name="createTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<div class="divider"></div>
	</div>