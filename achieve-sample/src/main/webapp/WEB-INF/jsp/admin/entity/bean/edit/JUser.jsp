<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<p> <label>用户密码:</label> <input name="password@" type="password" size="30" /> </p>
<c:set var="value" value="${entity.enable}" />
<p> <label>激活用户:</label> <input name="enable" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="enable" value="false" />
<c:set var="value" value="${entity.expired}" />
<p> <label>过期用户:</label> <input name="expired" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="expired" value="false" />
<c:set var="value" value="${entity.locked}" />
<p> <label>锁定用户:</label> <input name="locked" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="locked" value="false" />
<c:set var="value" value="${entity.lastLogin}" />
<p> <label>最后登录:</label> 
<input name="lastLogin" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<p> <label>错误登录:</label> <input name="errorLogin" type="text" size="30" value="${entity.errorLogin}" /> </p>
<c:set var="value" value="${entity.lastErrorLogin}" />
<p> <label>最后错误登录:</label> 
<input name="lastErrorLogin" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<p> <label>用户名:</label> <input name="username" type="text" size="30" value="${entity.username}" /> </p>
<c:set value="${entity.userType}" var="value"/>
<p> <label>用户类型:</label> <select name="userType" value="<%=WebJsplUtils.enumValue(pageContext.getAttribute("value"))%>" class=" combox"><option value="">请选择</option><option value="USER_ADMIN">管理员</option> <option value="USER_GUEST">访客</option> <option value="USER_NORMAL">普通用户</option> <option value="USER_VALIDATING">验证用户</option> <option value="USER_BAN">禁用</option></select> </p>
<c:set value="" var="values"/>
<c:set value="" var="ids"/>
<c:forEach items='${entity.userRoles}' var="value" varStatus="status">
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
<p> <label>用户角色:</label> <input name="userRoles$suggest" type="text" size="30" value="${values}" /><a class="btnLook" href="/achieve-dtyx/admin/entity/lookup/JUserRole?suggest=userRoles" lookupgroup="">查找带回</a> <input type="hidden" name="userRoles" value="${ids}" /></p>
<div class="divider"></div>
	<c:set value="${entity}" var="entityContext"/><div class="tabs">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li id="metaMap"><a href="javascript:;"><span>扩展纪录</span></a></li></ul>
			</div>
		</div>
		<div class="tabsContent">
			<c:set value="${entity.metaMap}" var="entities"/><div id="metaMap" class="pageContent"><input name="!subtables" type="hidden" value="metaMap" />
			<table class="list nowrap itemDetail" width="100%"
				addButton="">
				<thead>
					<tr>
						<th>键值</th>
						<th>内容</th>
						<th class="option" width="60">操作</th>
						</tr>
					<tr class="archetype">
						<td><input class="itemKey metaMap" type="text" readonly="readonly" value="" /></td>
<td><input name="metaMap['']" type="text" style="width: 80%" value="" /></td>
<td><a href="javascript:void(0)" class="btnDel">删除</a></td></tr>
				</thead>
				<tbody>
				<c:forEach items="${entities}" var="entity" varStatus="i"><tr>
					<td><input class="itemKey metaMap" type="text" readonly="readonly" value="${entity.key}" /></td>
<td><input name="metaMap['${entity.key}']" type="text" style="width: 80%" value="${entity.value}" /></td>
<td><a href="javascript:void(0)" class="btnDel">删除</a></td></tr>
				</c:forEach></tbody>
			</table>
			</div>
</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
	</div>