<%--
  Custom error page
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Orgflix 1.0</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <!--[if lte IE 8]><script src='<c:url value="../resources/js/ie/html5shiv.js" />' ></script><![endif]-->
    <link rel="stylesheet" href='<c:url value="../resources/css/main.css" />' />
    <link rel="stylesheet" href='<c:url value="../resources/css/custom.css?v=7" />' />
    <!--[if lte IE 9]><link rel="stylesheet" href='<c:url value="../resources/css/ie9.css" />' /><![endif]-->
    <!--[if lte IE 8]><link rel="stylesheet" href='<c:url value="../resources/css/ie8.css" />' /><![endif]-->
</head>
<body>
<!-- Wrapper -->
<div id="wrapper">

    <!-- Header -->
    <header id="header">
        <div class="inner">

            <!-- Logo -->
            <a href="home" class="logo">
                <span class="symbol"><img src='<c:url value="../resources/images/logo.svg" />' alt="" /></span><span class="title">Orgflix</span>
            </a>

            <!-- Nav -->
            <nav>
                <ul>
                    <li><a href="#menu">Menu</a></li>
                </ul>
            </nav>

        </div>
    </header>

    <!-- Menu -->
    <nav id="menu">
        <h2>Menu</h2>
        <ul>
            <li><a href="/"><i class="fa fa-home fa-fw"></i>Home</a></li>
            <%--<li><a href="watch_list">Watch list</a></li>--%>
            <%--<li><a href="wish_list">Wish List</a></li>--%>
            <li><a href="/login"><i class="fa fa-login fa-fw"></i>Login</a></li>
            <li><a href="signup"><i class="fa fa-signUp fa-fw"></i> Sign up</a></li>
        </ul>
    </nav>

    <!-- Main -->
    <div id="main">
        <div class="inner">
            <header>
                <h1>ERROR - Something bad has happaned !</h1>
            </header>
        </div>
    </div>

    <!-- Footer -->
    <footer id="footer">
        <div class="inner">
            <section>
                <h2>Get in touch</h2>
                <form method="post" action="#">
                    <div class="field half first">
                        <input type="text" name="name" id="name" placeholder="Name"/>
                    </div>
                    <div class="field half">
                        <input type="email" name="email" id="email" placeholder="Email"/>
                    </div>
                    <div class="field">
                        <textarea name="message" id="message" placeholder="Message"></textarea>
                    </div>
                    <ul class="actions">
                        <li><input type="submit" value="Send" class="special"/></li>
                    </ul>
                </form>
            </section>
            <section>
                <h2>Follow</h2>
                <ul class="icons">
                    <li><a href="#" class="icon style2 fa-twitter"><span class="label">Twitter</span></a></li>
                    <li><a href="#" class="icon style2 fa-facebook"><span class="label">Facebook</span></a></li>
                    <li><a href="#" class="icon style2 fa-instagram"><span class="label">Instagram</span></a></li>
                    <li><a href="#" class="icon style2 fa-dribbble"><span class="label">Dribbble</span></a></li>
                    <li><a href="#" class="icon style2 fa-github"><span class="label">GitHub</span></a></li>
                    <li><a href="#" class="icon style2 fa-500px"><span class="label">500px</span></a></li>
                    <li><a href="#" class="icon style2 fa-phone"><span class="label">Phone</span></a></li>
                    <li><a href="#" class="icon style2 fa-envelope-o"><span class="label">Email</span></a></li>
                </ul>
            </section>
            <ul class="copyright">
                <li>&copy; Untitled. All rights reserved</li>
                <li>Design: <a href="http://html5up.net">HTML5 UP</a></li>
            </ul>
        </div>
    </footer>

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
