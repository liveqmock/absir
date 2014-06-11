<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<%pageContext.setAttribute("selects", EntityStatics.suggest("JQuestionCategory", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${entity.category}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>
<p> <label>题目分类:</label> <select name="category" value="${id}" class=" combox">${options}</select> </p>
<p> <label>标题:</label> <input name="title" type="text" size="30" value="${entity.title}" class=" required" /> </p>
<p> <label>正确率:</label> <input name="difficult" type="text" size="30" value="${entity.difficult}" /> </p>
<p> <label>最小等级:</label> <input name="minLevel" type="text" size="30" value="${entity.minLevel}" /> </p>
<p> <label>最大等级:</label> <input name="maxLevel" type="text" size="30" value="${entity.maxLevel}" /> </p>
<p> <label>答案:</label> <input name="correct" type="text" size="30" value="${entity.correct}" /> </p>
<p> <label>解释:</label> <input name="answer" type="text" size="30" value="${entity.answer}" /> </p><div class="divider"></div>
<div class=" p"> 
 <label>详细解释:</label> 
 <textarea name="answerDesc" type="text" cols="87" rows="7">${entity.answerDesc}</textarea> 
</div><div class="divider"></div>
<div class=" p"> 
 <label>选项A:</label> 
 <textarea name="choiceA" type="text" cols="87" rows="7">${entity.choiceA}</textarea> 
</div><div class="divider"></div>
<div class=" p"> 
 <label>选项B:</label> 
 <textarea name="choiceB" type="text" cols="87" rows="7">${entity.choiceB}</textarea> 
</div><div class="divider"></div>
<div class=" p"> 
 <label>选项C:</label> 
 <textarea name="choiceC" type="text" cols="87" rows="7">${entity.choiceC}</textarea> 
</div><div class="divider"></div>
<div class=" p"> 
 <label>选项D:</label> 
 <textarea name="choiceD" type="text" cols="87" rows="7">${entity.choiceD}</textarea> 
</div>
<c:set var="value" value="${entity.updateTime}" />
<p> <label>修改时间:</label> 
<input name="updateTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" readonly="readonly" /> </p>
<div class="divider"></div>
	</div>