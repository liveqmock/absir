<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>纪录编号: </label> <input name="id >=" type="text" size="10" value="${searchConditionMap['id >=']}" /> <input name="id <=" type="text" size="10" value="${searchConditionMap['id <=']}" /></p>
<c:set value="${searchConditionMap['parent'][0]}" var="parentValue" />
<%pageContext.setAttribute("parentValue" ,EntityStatics.find("JMenu", pageContext.findAttribute("parentValue"), WebJsplUtils.getInput(request)));%>
<%pageContext.setAttribute("selects", EntityStatics.suggest("JMenu", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${parentValue}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>
<p class="s_parent"><label>父级菜单: </label> <select name="parent" value="${id}" class=" combox">${options}</select> </p>
<p class="s_name"><label>菜单名称: </label> <input name="name" type="text" size="20" value="${searchConditionMap['name']}" /> </p>
<p class="s_type"><label>菜单类型: </label> <input name="type" type="text" size="20" value="${searchConditionMap['type']}" /> </p>
<p class="s_ordinal"><label>菜单排序: </label> <input name="ordinal >=" type="text" size="10" value="${searchConditionMap['ordinal >=']}" /> <input name="ordinal <=" type="text" size="10" value="${searchConditionMap['ordinal <=']}" /></p>
<p class="s_url"><label>菜单地址: </label> <input name="url" type="text" size="20" value="${searchConditionMap['url']}" /> </p>
