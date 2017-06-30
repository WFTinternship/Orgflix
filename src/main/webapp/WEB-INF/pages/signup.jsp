<%--
  Page for user sign up
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="m" uri="/WEB-INF/taglib/customtags.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<!--
Phantom by HTML5 UP
html5up.net | @ajlkn
Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
<head>
    <title>Orgflix 1.0</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <!--[if lte IE 8]>
    <script src='<c:url value="../resources/js/ie/html5shiv.js" />'></script><![endif]-->
    <link rel="stylesheet" href='<c:url value="../resources/css/main.css" />'/>
    <!--[if lte IE 9]>
    <link rel="stylesheet" href='<c:url value="../resources/css/ie9.css" />'/><![endif]-->
    <!--[if lte IE 8]>
    <link rel="stylesheet" href='<c:url value="../resources/css/ie8.css" />'/><![endif]-->
</head>

<body>
<!-- Wrapper -->
<div id="wrapper">

    <m:pageHeader/>

    <!-- Menu -->
    <nav id="menu">
        <h2>Menu</h2>
        <ul>
            <li><a href="/"><i class="fa fa-home fa-fw"></i>Home</a></li>
            <li><a href="/login"><i class="fa fa-login fa-fw"></i>Login</a></li>
            <li><a href="signup"><i class="fa fa-signUp fa-fw"></i> Sign up</a></li>
        </ul>
        <form id="navigator" method="POST" action="">
            <input type="hidden" id="userId" name="userId" value="${userId}"'/>
            <input type="hidden" id="userAuth" name="userAuth" value="${userAuth}"/>
            <input type="hidden" id="currPage" name="currPage" value="${currPage}"/>
            <input type="hidden" id="page" name="page" value="${page}"/>
        </form>
    </nav>

    <!-- Main -->
    <div id="main">
        <div class="inner">
            <header>
                <h1>Sign up to OrgFlix</h1>
            </header>
            <section>
                <form id="signUpForm" method="POST" action="/signup">
                    <div>
                        <span>Nick</span>
                        <input type="text" name="nick" id="nick" class="inputField"/>
                    </div>
                    <div>
                        <span>User name</span>
                        <input type="text" name="userName" class="inputField"/>
                    </div>
                    <div>
                        <span>Email</span>
                        <input type="text" name="email" id="email" class="inputField"/>
                    </div>
                    <div>
                        <span>Password</span>
                        <input type="password" id="pass" name="pass" id="pass" class="inputField"/>
                    </div>
                    <div>
                        <span>Confirm password</span>
                        <input type="password" id="pass_check" class="inputField"/>
                    </div>
                    <input type="button" value="submit" onclick="submitSignUp()"/>

                </form>
                <div id="error" class="error"></div>
            </section>
        </div>
    </div>

    <m:pageFooter/>
</div>

<!-- Scripts -->
<script src='<c:url value="../resources/js/jquery.min.js" />'></script>
<script src='<c:url value="../resources/js/skel.min.js" />'></script>
<script src='<c:url value="../resources/js/util.js" />'></script>
<!--[if lte IE 8]>
<script src='<c:url value="../resources/js/ie/respond.min.js" />'></script><![endif]-->
<script src='<c:url value="../resources/js/main.js" />'></script>
<script src='<c:url value="../resources/js/custom.js?v=101" />'></script>


</body>
</html>
