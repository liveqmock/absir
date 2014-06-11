<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>纪录编号: </label> <input name="id >=" type="text" size="10" value="${searchConditionMap['id >=']}" /> <input name="id <=" type="text" size="10" value="${searchConditionMap['id <=']}" /></p>
<p class="s_platform"><label>平台名称: </label> <input name="platform" type="text" size="20" value="${searchConditionMap['platform']}" /> </p>
<p class="s_username"><label>用户名: </label> <input name="username" type="text" size="20" value="${searchConditionMap['username']}" /> </p>
<c:set var="value" value="${searchConditionMap['locked']}" />
<p class="s_locked"><label>锁定用户: </label> <input name="locked" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="locked" value="false" />
<p class="s_serverId"><label>服务区: </label> <input name="serverId >=" type="text" size="10" value="${searchConditionMap['serverId >=']}" /> <input name="serverId <=" type="text" size="10" value="${searchConditionMap['serverId <=']}" /></p>
<p class="s_playerId"><label>角色ID: </label> <input name="playerId >=" type="text" size="10" value="${searchConditionMap['playerId >=']}" /> <input name="playerId <=" type="text" size="10" value="${searchConditionMap['playerId <=']}" /></p>
