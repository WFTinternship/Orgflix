<%--
  Page for user login
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "m" uri = "/WEB-INF/taglib/customtags.tld" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Orgflix 1.0</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <!--[if lte IE 8]><script src='<c:url value="../resources/js/ie/html5shiv.js" />' ></script><![endif]-->
    <link rel="stylesheet" href='<c:url value="../resources/css/main.css" />' />
    <!--[if lte IE 9]><link rel="stylesheet" href='<c:url value="../resources/css/ie9.css" />' /><![endif]-->
    <!--[if lte IE 8]><link rel="stylesheet" href='<c:url value="../resources/css/ie8.css" />' /><![endif]-->
</head>
<body>
<!-- Wrapper -->
<div id="wrapper">

    <m:pageHeader />

    <!-- Menu -->
    <nav id="menu">
        <h2>Menu</h2>
        <ul>
            <li><a href="index">Home</a></li>
            <li><a href="login">Login</a></li>
            <li><a href="signup">Sign up</a></li>
        </ul>
    </nav>

    <!-- Main -->
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

    <m:pageFooter />
</div>

<!-- Scripts -->
<script src='<c:url value="../resources/js/jquery.min.js" />' ></script>
<script src='<c:url value="../resources/js/skel.min.js" />' ></script>
<script src='<c:url value="../resources/js/util.js" />' ></script>
<!--[if lte IE 8]><script src='<c:url value="../resources/js/ie/respond.min.js" />' ></script><![endif]-->
<script src='<c:url value="../resources/js/main.js" />' ></script>
<script src='<c:url value="../resources/js/custom.js?v=101" />' ></script>

</body>
</html>