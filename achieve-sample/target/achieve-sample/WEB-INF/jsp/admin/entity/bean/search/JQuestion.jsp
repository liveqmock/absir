<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>纪录编号: </label> <input name="id >=" type="text" size="10" value="${searchConditionMap['id >=']}" /> <input name="id <=" type="text" size="10" value="${searchConditionMap['id <=']}" /></p>
<c:set value="${searchConditionMap['category'][0]}" var="categoryValue" />
<%pageContext.setAttribute("categoryValue" ,EntityStatics.find("JQuestionCategory", pageContext.findAttribute("categoryValue"), WebJsplUtils.getInput(request)));%>
<%pageContext.setAttribute("selects", EntityStatics.suggest("JQuestionCategory", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${categoryValue}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>
<p class="s_category"><label>题目分类: </label> <select name="category" value="${id}" class=" combox">${options}</select> </p>
<p class="s_title"><label>标题: </label> <input name="title" type="text" size="20" value="${searchConditionMap['title']}" /> </p>
<p class="s_difficult"><label>正确率: </label> <input name="difficult >=" type="text" size="10" value="${searchConditionMap['difficult >=']}" /> <input name="difficult <=" type="text" size="10" value="${searchConditionMap['difficult <=']}" /></p>
<p class="s_minLevel"><label>最小等级: </label> <input name="minLevel >=" type="text" size="10" value="${searchConditionMap['minLevel >=']}" /> <input name="minLevel <=" type="text" size="10" value="${searchConditionMap['minLevel <=']}" /></p>
<p class="s_maxLevel"><label>最大等级: </label> <input name="maxLevel >=" type="text" size="10" value="${searchConditionMap['maxLevel >=']}" /> <input name="maxLevel <=" type="text" size="10" value="${searchConditionMap['maxLevel <=']}" /></p>
<p class="s_correct"><label>答案: </label> <input name="correct >=" type="text" size="10" value="${searchConditionMap['correct >=']}" /> <input name="correct <=" type="text" size="10" value="${searchConditionMap['correct <=']}" /></p>
<c:set var="value" value="${searchConditionMap['updateTime >=']}" />
<p class="s_updateTime"><label>修改时间: </label> 
<input name="updateTime >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['updateTime <=']}" />
<input name="updateTime <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
