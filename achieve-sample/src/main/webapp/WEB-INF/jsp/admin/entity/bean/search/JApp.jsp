<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>纪录编号: </label> <input name="id >=" type="text" size="10" value="${searchConditionMap['id >=']}" /> <input name="id <=" type="text" size="10" value="${searchConditionMap['id <=']}" /></p>
<p class="s_name"><label>版本名称: </label> <input name="name" type="text" size="20" value="${searchConditionMap['name']}" /> </p>
<p class="s_platform"><label>平台: </label> <input name="platform" type="text" size="20" value="${searchConditionMap['platform']}" /> </p>
<p class="s_version"><label>版本号: </label> <input name="version >=" type="text" size="10" value="${searchConditionMap['version >=']}" /> <input name="version <=" type="text" size="10" value="${searchConditionMap['version <=']}" /></p>
<c:set var="value" value="${searchConditionMap['updateTime >=']}" />
<p class="s_updateTime"><label>修改时间: </label> 
<input name="updateTime >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['updateTime <=']}" />
<input name="updateTime <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
