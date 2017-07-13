<%--
  Page for user login
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "m" uri = "/WEB-INF/taglib/customtags.tld" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<!-- Head and CSS Import -->
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- Wrapper -->
<div id="wrapper">
    <!-- Page header -->
    <m:pageHeader />
    <!-- Navigation menu -->
    <%@ include file="/WEB-INF/pages/menu.jsp" %>
    <!-- Menu page-->
    <div id="main">
        <div class="inner">
            <header>
                <h1>Login to Your account at OrgFlix</h1>
            </header>
            <section>
                <form id="loginForm" method="POST" action="/logedIn">
                    <div>
                        <span>Email</span>
                        <input type="text" id="email" name="email" class="inputField" />
                    </div>
                    <div>
                        <span>Password</span>
                        <input type="password" id="pass" name="pass" class="inputField" />
                    </div>

                    <input type="button" value="Login" onclick="submitLogin()"/>
                </form>
                <div id="error" class="error"></div>
            </section>
        </div>
    </div>
    <!-- Page footer -->
    <m:pageFooter />
</div>
<!-- JS imports -->
<%@ include file="/WEB-INF/pages/JSImport.jsp" %>
</body>
</html>