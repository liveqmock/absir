<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>验证主键: </label> <input name="id" type="text" size="20" value="${searchConditionMap['id']}" /> </p>
<c:set var="value" value="${searchConditionMap['passTime >=']}" />
<p class="s_passTime"><label>过期时间: </label> 
<input name="passTime >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['passTime <=']}" />
<input name="passTime <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
<p class="s_userId"><label>用户ID: </label> <input name="userId >=" type="text" size="10" value="${searchConditionMap['userId >=']}" /> <input name="userId <=" type="text" size="10" value="${searchConditionMap['userId <=']}" /></p>
<p class="s_username"><label>用户名: </label> <input name="username" type="text" size="20" value="${searchConditionMap['username']}" /> </p>
<c:set var="value" value="${searchConditionMap['lastTime >=']}" />
<p class="s_lastTime"><label>最后登录: </label> 
<input name="lastTime >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['lastTime <=']}" />
<input name="lastTime <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
