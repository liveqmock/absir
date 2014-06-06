<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<%pageContext.setAttribute("selects", EntityStatics.suggest("JMenu", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${entity.parent}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>
<p> <label>父级菜单:</label> <select name="parent" value="${id}" class=" combox">${options}</select> </p>
<p> <label>菜单名称:</label> <input name="name" type="text" size="30" value="${entity.name}" /> </p>
<p> <label>菜单类型:</label> <input name="type" type="text" size="30" value="${entity.type}" /> </p>
<p> <label>菜单排序:</label> <input name="ordinal" type="text" size="30" value="${entity.ordinal}" /> </p>
<p> <label>菜单地址:</label> <input name="url" type="text" size="30" value="${entity.url}" /> </p>
<p> <label>菜单标注:</label> <input name="ref" type="text" size="30" value="${entity.ref}" /> </p>
<c:set value="${entity.urlType}" var="value"/>
<p> <label>链接类型:</label> <select name="urlType" value="<%=WebJsplUtils.enumValue(pageContext.getAttribute("value"))%>" class=" combox"><option value="">请选择</option><option value="SITE">前台链接</option> <option value="ADMIN">后台链接</option> <option value="NONE">外部链接</option></select> </p>
<div class="divider"></div>
	</div>