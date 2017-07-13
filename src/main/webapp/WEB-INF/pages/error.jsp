<%--
  Custom error page
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "m" uri = "/WEB-INF/taglib/customtags.tld" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<!-- Head and CSS Import -->
<%@ include file="/WEB-INF/pages/header.jsp" %>
<body>
<!-- Wrapper -->
<div id="wrapper">
    <!-- Page header -->
    <m:pageHeader />
    <!-- Navigation menu -->
    <%@ include file="/WEB-INF/pages/menu.jsp" %>
    <!-- Main -->
    <div id="main">
        <div class="inner">
            <header>
                <h1>ERROR - Something bad has happaned !</h1>
            </header>
        </div>
    </div>
    <!-- Page footer -->
    <m:pageFooter />
</div>
<!-- JS imports -->
<%@ include file="/WEB-INF/pages/JSImport.jsp" %>
</body>
</html>