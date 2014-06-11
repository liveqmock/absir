<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p> <label>初始金钱:</label> <input name="money" type="text" size="30" value="${entity.money}" /> </p>
<p> <label>初始宝石:</label> <input name="diamond" type="text" size="30" value="${entity.diamond}" /> </p>
<p> <label>初始卡牌:</label> 
 <c:set var="value" value="${entity.cardIds}" /><input name="cardIds" type="text" size="30" value="<%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%>" /> </p>
<p> <label>附加卡牌:</label> 
 <c:set var="value" value="${entity.otherCardIds}" /><input name="otherCardIds" type="text" size="30" value="<%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%>" /> </p>
<p> <label>最大等级:</label> <input name="maxLevel" type="text" size="30" value="${entity.maxLevel}" /> </p>
<p> <label>购买卡牌栏:</label> <input name="cardNumber" type="text" size="30" value="${entity.cardNumber}" /> </p>
<p> <label>购买好友数:</label> <input name="friendNumber" type="text" size="30" value="${entity.friendNumber}" /> </p>
<p> <label>进化概率:</label> 
 <c:set var="value" value="${entity.evoluteProb}" /><input name="evoluteProb" type="text" size="30" value="<%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%>" /> </p>
<p> <label>开启技能价格:</label> <input name="openDiamond" type="text" size="30" value="${entity.openDiamond}" /> </p>
<p> <label>锁定技能价格:</label> <input name="lockDiamond" type="text" size="30" value="${entity.lockDiamond}" /> </p>
<p> <label>新技能概率:</label> 
 <c:set var="value" value="${entity.skillProb}" /><input name="skillProb" type="text" size="30" value="<%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%>" /> </p>
<div class="divider"></div>
	</div>