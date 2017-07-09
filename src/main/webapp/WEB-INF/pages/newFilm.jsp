<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="example-taglib" %>
<!DOCTYPE HTML>
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
            <c:if test="${userId != -1}">
                <li><i class="fa fa-user fa-fw"></i> <c:out value="${user}"/></li>
                <li><a onclick="navigator('index')"><i class="fa fa-home fa-fw"></i>Home</a></li>
                <li><a onclick="navigator('watch_list')"><i class="fa fa-watchList fa-fw"></i> Watch list</a></li>
                <li><a onclick="navigator('wish_list')"><i class="fa fa-wishList fa-fw"></i> Wish List</a></li>
                <li><a href="/newFilm"><i class="fa fa-plus-square fa-fw"></i> Add Film</a></li>
                <li><a href="/"><i class="fa fa-logout fa-fw"></i> Logout</a></li>
            </c:if>
            <c:if test="${userId == -1}">
                <li><a href="/"><i class="fa fa-home fa-fw"></i>Home</a></li>
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
                <h1>Add Film to OrgFlix</h1>
            </header>
            <section>
                <h3 style="color:#4581ff">${filesuccess}</h3>
                <form id="" method="POST" action="/newFilm" enctype="multipart/form-data">
                    <div>
                        <span>Film Title</span>
                        <input type="text" id="title" name="title" class="inputField"/>
                    </div>
                    <div style="display: table">
                        <div class="tableCell">
                            <span>Release Date</span>
                            <select id="year" name="year" class="dateField">
                                <c:forEach begin="1900" end="2016" var="val">
                                    <option><c:out value="${val}"/></option>
                                </c:forEach>
                                <option selected="selected">2017</option>
                            </select>
                        </div>
                        <div class="tableCell">
                            <span>Your rate</span><br />
                            <a onmouseover="star(1,false)" onmouseout="star(0,false)" onclick="star(1,true)" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <a onmouseover="star(2,false)" onmouseout="star(0,false)" onclick="star(2,true)" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <a onmouseover="star(3,false)" onmouseout="star(0,false)" onclick="star(3,true)" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <a onmouseover="star(4,false)" onmouseout="star(0,false)" onclick="star(4,true)" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <a onmouseover="star(5,false)" onmouseout="star(0,false)" onclick="star(5,true)" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <span>(1 star)</span>
                            <input type="hidden" id="stars" name="stars" value="1" />
                        </div>
                        <div class="tableCell textCentered">
                            <span>Awards(Oscar)</span><br />
                            <a onclick="hasOscar()"><i id="oscarCheck" class="fa fa-square-o fa-fw"></i></a>
                            <input type="hidden" id="hasOscar" name="hasOscar" value="0" class="pointerA"/>
                        </div>
                        <div class="tableCell">
                            <span>Add image</span>
                            <p><input name="file" id="fileToUpload" type="file" accept="image/*"/></p>
                        </div>
                    </div>
                    <div>
                        <span>Director of the film</span>
                        <input type="text" id="director" name="director" class="inputField"/>
                    </div>
                    <div>
                        <span>Mian actors</span>
                        <input type="text" id="actor" name="actor" onclick="getActorsList()" class="inputField"/>
                        <%--<c:forEach items="${actors}" var="cast" >--%>
                            <%--<div class="castStyle"><c:out value="${cast.id}"/> - <c:out value="${cast.name}"/></div>--%>
                        <%--</c:forEach>--%>

                    </div>
                </form>
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
<script src='<c:url value="../resources/js/custom.js?v=107" />'></script>

</body>
</html>