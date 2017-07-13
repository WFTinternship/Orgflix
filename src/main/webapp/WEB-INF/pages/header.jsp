<%--
  Page header template
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<head>
    <title>Orgflix 1.0</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <!--[if lte IE 8]><script src="${contextPath}/resources/js/ie/html5shiv.js" ></script><![endif]-->
    <link rel="stylesheet" href="${contextPath}/resources/css/main.css" />
    <link rel="stylesheet" href="${contextPath}/resources/css/custom.css?v=7" />
    <!--[if lte IE 9]><link rel="stylesheet" href="${contextPath}/resources/css/ie9.css" /><![endif]-->
    <!--[if lte IE 8]><link rel="stylesheet" href="${contextPath}/resources/css/ie8.css" /><![endif]-->
</head>
