<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>纪录编号: </label> <input name="id >=" type="text" size="10" value="${searchConditionMap['id >=']}" /> <input name="id <=" type="text" size="10" value="${searchConditionMap['id <=']}" /></p>
<p class="s_activityBase.name"><label>活动名称: </label> <input name="activityBase.name" type="text" size="20" value="${searchConditionMap['activityBase.name']}" /> </p>
<c:set var="value" value="${searchConditionMap['beginTime >=']}" />
<p class="s_beginTime"><label>开始时间: </label> 
<input name="beginTime >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['beginTime <=']}" />
<input name="beginTime <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
<c:set var="value" value="${searchConditionMap['passTime >=']}" />
<p class="s_passTime"><label>过期时间: </label> 
<input name="passTime >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['passTime <=']}" />
<input name="passTime <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
<c:set var="value" value="${searchConditionMap['updateTime >=']}" />
<p class="s_updateTime"><label>修改时间: </label> 
<input name="updateTime >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['updateTime <=']}" />
<input name="updateTime <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
