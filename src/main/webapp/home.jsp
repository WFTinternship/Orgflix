<%@ page import="am.aca.service.impl.FilmServiceImpl" %>
<%@ page import="am.aca.entity.Film" %>
<%@ page import="java.util.List" %>
<%@ page import="am.aca.service.FilmService" %><%--
  Created by IntelliJ IDEA.
  User: Vardan
  Date: 09.06.2017
  Time: 14:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! FilmService filmService = new FilmServiceImpl(); %>
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
    <link rel="stylesheet" href="assets/css/main.css?v=101" />
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
            <li><a href="home">Home</a></li>
            <li><a href="watch_list">Watch list</a></li>
            <li><a href="wish_list">Wish List</a></li>
            <li><a href="loging">Logout</a></li>
        </ul>
    </nav>

    <!-- Main -->
    <div id="main">
        <div class="inner">
            <header>
                <h1>This is OrgFlix, a free film list organizer</h1>
            </header>
            <section class="tiles">
<% List<Film> films = filmService.getFilmsList(Integer.valueOf(request.getAttribute("message").toString())*12);
    for (Film film: films ) {
    out.print("<article class='style1'><span class='image'><img src='images/"+film.getImage()+".jpg'/></span>");
    out.print("<a href='generic'><h2>"+film.getTitle()+"</h2>");
    out.print("<div class='content'><p>Somthing</p></div></a></article>");
}
    int filmnum=filmService.totalNumberOfFilms();
    if(filmnum>12){
        out.print("<form class='pagintion_container' id='pageForm' method='GET' action='/home'><input type='hidden' id='currPage' name='currPage' value='0'>");
        for(int i=0;i<=filmnum/12;++i){
            out.print("<div class='pagination' id='"+i+"' onclick='pagination(this.id)'>"+(i+1)+"</div>");
        }
        out.print("</form>");
    }
%>
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
<script src="assets/js/custom.js?v=102"></script>

</body>
</html>
