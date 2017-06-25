<%--
  Main home page
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "m" uri = "/WEB-INF/taglib/customtags.tld" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<!--
Phantom by HTML5 UP
html5up.net | @ajlkn
Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
<head>
    <title>Orgflix 1.0</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <!--[if lte IE 8]><script src="assets/js/ie/html5shiv.js"></script><![endif]-->
    <link rel="stylesheet" href="assets/css/main.css" />
    <!--[if lte IE 9]><link rel="stylesheet" href="assets/css/ie9.css" /><![endif]-->
    <!--[if lte IE 8]><link rel="stylesheet" href="assets/css/ie8.css" /><![endif]-->
</head>
<body>
<!-- Wrapper -->
<div id="wrapper">

    <!-- Header -->
    <header id="header">
        <div class="inner">

            <!-- Logo -->
            <a href="home" class="logo">
                <span class="symbol"><img src="images/logo.svg" alt="" /></span><span class="title">Orgflix</span>
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
            <c:if test="${userId != -1}">
                <li><i class="fa fa-user fa-fw"></i> <c:out value="${user}"/></li>
                <li><a href="home">Home</a></li>
                <li><a onclick="navigator('watch_list')"><i class="fa fa-watchList fa-fw"></i> Watch list</a></li>
                <li><a href="wish_list"><i class="fa fa-wishList fa-fw"></i> Wish List</a></li>
                <li><a href="home"><i class="fa fa-logout fa-fw"></i> Logout</a></li>
            </c:if>
            <c:if test="${userId == -1}">
                <li><a href="home">Home</a></li>
                <li><a href="login"><i class="fa fa-login fa-fw"></i> Login</a></li>
                <li><a href="signup"><i class="fa fa-signUp fa-fw"></i> Sign up</a></li>
            </c:if>
        </ul>
        <form id="navigator" method="POST" action="">
        <input type="hidden" id="userId" value='<c:out value="${userId}"/>'/>
        <input type="hidden" id="userAuth" value='<c:out value="${userAuth}"/>'/>
        </form>
    </nav>

    <!-- Main -->
    <div id="main">
        <div class="inner">
            <header>
                <h1>This is OrgFlix, a free film list organizer</h1>
            </header>
            <section class="tiles">
                <c:forEach items="${films}" var="film" >
                    <article class='style1' id="${film.id}" onmouseout='filmControlMenu(false,this.id)' onmouseover='filmControlMenu(true,this.id)'>
                        <div class='image' >
                            <img src='<c:out value="${film.image}" />' />

                        </div>
                        <c:if test="${userId != -1}">
                            <div id="contr_${film.id}" class="film-buttons">
                                <a href="#" class="no_link" onclick="AddToList(true,'${film.id}')"><i class="fa fa-wishList fa-fw"></i></a>
                                <a href="#" class="no_link" onclick="AddToList(false,'${film.id}')"><i class="fa fa-watchList fa-fw"></i></a>
                            </div>
                        </c:if>
                        <div class="film-ref">
                            <h2><c:out value="${film.title}" /></h2>
                        </div>
                    </article>
                </c:forEach>

                <m:pagination />
                <div class="pop-up-result"></div>
            </section>
        </div>
    </div>

    <!-- Footer -->
    <footer id="footer">
        <div class="inner">
            <section>
                <h2>Get in touch</h2>
                <form method="post" action="#">
                    <div class="field half first">
                        <input type="text" name="name" id="name" placeholder="Name" />
                    </div>
                    <div class="field half">
                        <input type="email" name="email" id="email" placeholder="Email" />
                    </div>
                    <div class="field">
                        <textarea name="message" id="message" placeholder="Message"></textarea>
                    </div>
                    <ul class="actions">
                        <li><input type="submit" value="Send" class="special" /></li>
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
                <li>&copy; Untitled. All rights reserved</li><li>Design: <a href="http://html5up.net">HTML5 UP</a></li>
            </ul>
        </div>
    </footer>

</div>

<!-- Scripts -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/skel.min.js"></script>
<script src="assets/js/util.js"></script>
<!--[if lte IE 8]><script src="assets/js/ie/respond.min.js"></script><![endif]-->
<script src="assets/js/main.js"></script>
<script src="assets/js/custom.js"></script>

</body>
</html>
