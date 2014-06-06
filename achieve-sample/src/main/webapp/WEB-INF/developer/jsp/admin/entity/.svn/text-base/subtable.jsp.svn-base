<%@page import="java.util.Map"%>
<%@include file="../../../common/option.jsp"%>
<%
	WebJsplDeveloper.setScenario("subtable", request);
	element = (Element) request.getAttribute("element");
	Object subtable = request.getAttribute("subtable");
	if (subtable == null || !(subtable instanceof Map)) {
		subtable = new HashMap();
		request.setAttribute("subtable", subtable);
	}

	if (!((Map) subtable).containsKey("index")) {
		((Map) subtable).put("index", "#index#");
	}
	WebJsplDeveloper.popScenario(request);
%>
<%="<c:set value=\"${field.defaultEntity}\" var=\"entity\"/>\r\n"%>
<%="<c:set value=\"${subtable}\" var=\"i\"/>\r\n"%>
<%=element.html()%>