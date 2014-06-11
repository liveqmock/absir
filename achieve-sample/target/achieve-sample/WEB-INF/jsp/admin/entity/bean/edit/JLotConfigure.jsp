<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p> <label>抽奖友情数:</label> <input name="friendshipNumber" type="text" size="30" value="${entity.friendshipNumber}" /> </p>
<p> <label>抽奖宝石数:</label> <input name="diamondNumber" type="text" size="30" value="${entity.diamondNumber}" /> </p>
<p> <label>卡牌友情概率:</label> 
 <c:set var="value" value="${entity.friendProbabilities}" /><input name="friendProbabilities" type="text" size="30" value="<%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%>" /> </p>
<p> <label>卡牌宝石概率:</label> 
 <c:set var="value" value="${entity.diamondProbabilities}" /><input name="diamondProbabilities" type="text" size="30" value="<%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%>" /> </p>
<div class="divider"></div>
	</div>