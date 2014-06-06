<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<p> <label>通知名称:</label> <input name="name" type="text" size="30" value="${entity.name}" /> </p><div class="divider"></div>
<div class=" p"> 
 <label>通知内容:</label> 
 <textarea name="content" type="text" cols="87" rows="7">${entity.content}</textarea> 
</div>
<c:set var="value" value="${entity.updateTime}" />
<p> <label>修改时间:</label> 
<input name="updateTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" readonly="readonly" /> </p>
<div class="divider"></div>
	</div>