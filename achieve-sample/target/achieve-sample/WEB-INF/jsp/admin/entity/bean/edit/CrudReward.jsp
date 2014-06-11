<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p> <label>金钱奖励:</label> <input name="money" type="text" size="30" value="${entity.money}" /> </p>
<p> <label>宝石奖励:</label> <input name="diamond" type="text" size="30" value="${entity.diamond}" /> </p>
<p> <label>卡牌奖励:</label> <input name="cardRewards" type="text" size="30" value="${entity.cardRewards}" /> </p>
<p> <label>道具奖励:</label> <input name="propRewards" type="text" size="30" value="${entity.propRewards}" /> </p>
<p> <label>角色IDS:</label> 
 <c:set var="value" value="${entity.playerIds}" /><input name="playerIds" type="text" size="30" value="<%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%>" /> </p>
<p> <label>服务区IDS:</label> 
 <c:set var="value" value="${entity.serverIds}" /><input name="serverIds" type="text" size="30" value="<%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%>" /> </p>
<p> <label>奖励名称:</label> <input name="name" type="text" size="30" value="${entity.name}" /> </p>
<c:set value="${entity.type}" var="value"/>
<p> <label>奖励类型:</label> <select name="type" value="<%=WebJsplUtils.enumValue(pageContext.getAttribute("value"))%>" class=" combox"><option value="">请选择</option><option value="ACTIVITY">活动</option> <option value="PVP">对战</option> <option value="SYSTEM">系统</option> <option value="PAY">充值</option> <option value="WAR">团战</option></select> </p>
<p> <label>奖励参数:</label> <input name="data" type="text" size="30" value="${entity.data}" /> </p>
<div class="divider"></div>
	</div>