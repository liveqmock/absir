<%@ include file="../../../common/option.jsp"%>
<%
	WebJsplDeveloper.setScenario("search", request);
	element = document.appendElement("tr");
%>
<%=DeveloperCode.script("EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));")%>
<%
	for (IField field : entityModel.getGroupFields("search")) {
%>
<%
	// 显示搜索内容
		identifier = "<p class=\"s_" + field.getName();
		if (!generator.append(identifier, element)) {
			request.setAttribute("field", field);
%>
<c:set var="node">
	<%=identifier + "\">"%>
	<label><%=field.getCaption()%>: </label>
	<input name="<%=field.getName()%>" type="text" size="20"
		value="<%="${searchConditionMap['" + field.getName() + "']}"%>">
	<%="</p>"%>
</c:set>
<c:set var="field" value="${field}" scope="request" />
<%
	// 适配字段特性	
			request.setAttribute("nodes", ScripteNode.append(element, pageContext.getAttribute("node").toString()));
			DeveloperUtils.includeExist("search", field.getTypes(), pageContext, request, response);
		}
		// 显示搜索内容结束
	}
%>
<%
	request.setAttribute("element", element);
	WebJsplDeveloper.popScenario(request);
%>
<%=element.html() + "\r\n"%>