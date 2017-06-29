<%--
  Main home page
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "m" uri = "/WEB-INF/taglib/customtags.tld" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
    <!--[if lte IE 8]><script src='<c:url value="../resources/js/ie/html5shiv.js" />' ></script><![endif]-->
    <link rel="stylesheet" href='<c:url value="../resources/css/main.css?v=3" />' />
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
            <c:if test="${userId != -1}">
                <li><i class="fa fa-user fa-fw"></i> <c:out value="${user}"/></li>
                <li><a onclick="navigator('index')">Home</a></li>
                <li><a onclick="navigator('watch_list')"><i class="fa fa-watchList fa-fw"></i> Watch list</a></li>
                <li><a onclick="navigator('wish_list')"><i class="fa fa-wishList fa-fw"></i> Wish List</a></li>
                <li><a href="/"><i class="fa fa-logout fa-fw"></i> Logout</a></li>
            </c:if>
            <c:if test="${userId == -1}">
                <li><a href="/">Home</a></li>
                <li><a href="login"><i class="fa fa-login fa-fw"></i> Login</a></li>
                <li><a href="signup"><i class="fa fa-signUp fa-fw"></i> Sign up</a></li>
            </c:if>
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
                <c:if test="${ (page == 'page') || (userId==-1)}">
                    <h1>This is OrgFlix, a free film list organizer</h1>
                </c:if>
                <c:if test="${page == 'watch'}">
                    <h1>Watch list</h1>
                </c:if>
                <c:if test="${page == 'wish'}">
                    <h1>Wish list</h1>
                </c:if>
            </header>
            <section class="tiles">
                <c:forEach items="${films}" var="film" >
                    <article class='style1' id="${film.id}" onmouseout='filmControlMenu(false,this.id)' onmouseover='filmControlMenu(true,this.id)'>
                        <div class='image' >
                            <img src='<c:url value="../resources/${film.image}" />' />

                        </div>
                        <c:if test="${userId != -1}">
                            <div id="contr_${film.id}" class="film-buttons">
                                <c:if test="${page == 'main'}">
                                    <a href="#" class="no_link" onclick="AddToList('WatchList','${film.id}')"><i class="fa fa-wishList fa-fw"></i></a>
                                    <a href="#" class="no_link" onclick="AddToList('WishList','${film.id}')"><i class="fa fa-watchList fa-fw"></i></a>
                                </c:if>
                                <c:if test="${page == 'watch'}">
                                    <a href="#" class="no_link" onclick="RemoveFromList('WatchList','${film.id}')"><i class="fa fa-trash-o fa-fw"></i></a>
                                </c:if>
                                <c:if test="${page == 'wish'}">
                                    <a href="#" class="no_link" onclick="RemoveFromList('WishList','${film.id}')"><i class="fa fa-trash-o fa-fw"></i></a>
                                </c:if>
                            </div>
                        </c:if>
                        <div class="film-ref">
                            <h2><c:out value="${film.title}" /></h2>
                        </div>
                    </article>
                </c:forEach>
                <%--<c:if test="${fn:length(films) > 12}">--%>
                <m:pagination />
                <%--</c:if>--%>
                <div id="pop-up-result" class="pop-up-result"></div>
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
<script src='<c:url value="../resources/js/custom.js?v=107" />' ></script>

</body>
</html>
