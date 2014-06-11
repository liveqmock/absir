<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>纪录编号: </label> <input name="id >=" type="text" size="10" value="${searchConditionMap['id >=']}" /> <input name="id <=" type="text" size="10" value="${searchConditionMap['id <=']}" /></p>
<p class="s_serverId"><label>服务区: </label> <input name="serverId >=" type="text" size="10" value="${searchConditionMap['serverId >=']}" /> <input name="serverId <=" type="text" size="10" value="${searchConditionMap['serverId <=']}" /></p>
<p class="s_userId"><label>玩家ID: </label> <input name="userId >=" type="text" size="10" value="${searchConditionMap['userId >=']}" /> <input name="userId <=" type="text" size="10" value="${searchConditionMap['userId <=']}" /></p>
<p class="s_name"><label>角色名称: </label> <input name="name" type="text" size="20" value="${searchConditionMap['name']}" /> </p>
<c:set var="value" value="${searchConditionMap['sex']}" />
<p class="s_sex"><label>角色性别: </label> <input name="sex" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="sex" value="false" />
<p class="s_sign"><label>用户签名: </label> <input name="sign" type="text" size="20" value="${searchConditionMap['sign']}" /> </p>
<p class="s_level"><label>角色等级: </label> <input name="level >=" type="text" size="10" value="${searchConditionMap['level >=']}" /> <input name="level <=" type="text" size="10" value="${searchConditionMap['level <=']}" /></p>
<p class="s_cardNumber"><label>卡牌数量: </label> <input name="cardNumber >=" type="text" size="10" value="${searchConditionMap['cardNumber >=']}" /> <input name="cardNumber <=" type="text" size="10" value="${searchConditionMap['cardNumber <=']}" /></p>
<p class="s_friendNumber"><label>好友数量: </label> <input name="friendNumber >=" type="text" size="10" value="${searchConditionMap['friendNumber >=']}" /> <input name="friendNumber <=" type="text" size="10" value="${searchConditionMap['friendNumber <=']}" /></p>
