<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.KernelArray"%>
<%
	request.setAttribute("layout.jsp", "/WEB-INF/jsp/layout.jsp");
%>
<c:set var="page_title" scope="request">${app_name}登录后台</c:set>
<c:set var="page_header" scope="request">
</c:set>

<div id="da-login">
	<div id="da-login-box-wrapper">
		<div id="da-login-top-shadow"></div>
		<div id="da-login-box">
			<div id="da-login-box-header">
				<h1>Login</h1>
			</div>
			<div id="da-login-box-content">
				<form id="da-login-form" method="post" action="${admin_route}/login">
					<div id="da-login-input-wrapper">
						<c:if test="${not empty param.login_error}">
							<div class="errors">
								<b><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" /></b>
							</div>
						</c:if>
						<div class="da-login-input">
							<input type="text" name="username" id="da-login-username"
								placeholder="Username" />
						</div>
						<div class="da-login-input">
							<input type="password" name="password" id="da-login-password"
								placeholder="Password" />
						</div>
						<div class="da-login-input">
							<input type="text" name="verifycode" /><img
								src="${site_route}/asset/verify" alt=""
								width="66" height="24" onClick="this.src=this.src;" />
						</div>
						<div class="da-login-input">
							RememberMe:<input type="checkbox"
								name="_spring_security_remember_me" />
						</div>
					</div>
					<div id="da-login-button">
						<input type="submit" value="Login" id="da-login-submit" />
					</div>
				</form>
			</div>
			<div id="da-login-box-footer">
				<a>forgot your password?</a> <a>download.</a>
				<div id="da-login-tape"></div>
			</div>
		</div>
		<div id="da-login-bottom-shadow"></div>
	</div>
</div>