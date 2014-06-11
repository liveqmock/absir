<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>纪录编号: </label> <input name="id >=" type="text" size="10" value="${searchConditionMap['id >=']}" /> <input name="id <=" type="text" size="10" value="${searchConditionMap['id <=']}" /></p>
<c:set var="value" value="${searchConditionMap['enable']}" />
<p class="s_enable"><label>激活用户: </label> <input name="enable" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="enable" value="false" />
<c:set var="value" value="${searchConditionMap['expired']}" />
<p class="s_expired"><label>过期用户: </label> <input name="expired" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="expired" value="false" />
<c:set var="value" value="${searchConditionMap['locked']}" />
<p class="s_locked"><label>锁定用户: </label> <input name="locked" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="locked" value="false" />
<c:set var="value" value="${searchConditionMap['lastLogin >=']}" />
<p class="s_lastLogin"><label>最后登录: </label> 
<input name="lastLogin >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['lastLogin <=']}" />
<input name="lastLogin <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
<p class="s_errorLogin"><label>错误登录: </label> <input name="errorLogin >=" type="text" size="10" value="${searchConditionMap['errorLogin >=']}" /> <input name="errorLogin <=" type="text" size="10" value="${searchConditionMap['errorLogin <=']}" /></p>
<c:set var="value" value="${searchConditionMap['lastErrorLogin >=']}" />
<p class="s_lastErrorLogin"><label>最后错误登录: </label> 
<input name="lastErrorLogin >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['lastErrorLogin <=']}" />
<input name="lastErrorLogin <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
<p class="s_username"><label>用户名: </label> <input name="username" type="text" size="20" value="${searchConditionMap['username']}" /> </p>
<c:set value="${searchConditionMap['userType']}" var="value"/>
<p class="s_userType"><label>用户类型: </label> <select name="userType" value="<%=WebJsplUtils.enumValue(pageContext.getAttribute("value"))%>" class=" combox"><option value="">请选择</option><option value="USER_ADMIN">管理员</option> <option value="USER_GUEST">访客</option> <option value="USER_NORMAL">普通用户</option> <option value="USER_VALIDATING">验证用户</option> <option value="USER_BAN">禁用</option></select> </p>
<c:set value="${searchConditionMap['userRoles']}" var="userRolesValue" />
<%pageContext.setAttribute("userRolesValue" ,EntityStatics.list("JUserRole", pageContext.findAttribute("userRolesValue"), WebJsplUtils.getInput(request)));%>
<c:set value="" var="values"/>
<c:set value="" var="ids"/>
<c:forEach items='${userRolesValue}' var="value" varStatus="status">
<c:if test="${status.index > 0}">
<c:set value="${ids}," var="ids"/>
<c:set value="${values}," var="values"/>
</c:if>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.rolename}" var="value"/>
<c:set value="${ids}${id}" var="ids"/>
<c:set value="${values}${value}" var="values"/>
</c:forEach>
<%session.setAttribute("SUGGEST@JUserRole", true);%>
<p class="s_userRoles"><label>用户角色: </label> <input name="userRoles$suggest" type="text" size="20" value="${values}" /><a class="btnLook" href="/achieve-dtyx/admin/entity/lookup/JUserRole?suggest=userRoles" lookupgroup="">查找带回</a> <input type="hidden" name="userRoles" value="${ids}" /></p>
