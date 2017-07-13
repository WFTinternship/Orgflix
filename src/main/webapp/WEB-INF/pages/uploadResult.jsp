<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="example-taglib" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Orgflix 1.0</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <!--[if lte IE 8]>
    <script src='<c:url value="../resources/js/ie/html5shiv.js" />'></script><![endif]-->
    <link rel="stylesheet" href='<c:url value="../resources/css/main.css" />'/>
    <link rel="stylesheet" href='<c:url value="../resources/css/custom.css?v=7" />' />
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
                <li><a href="/uploadForm"><i class="fa fa-plus-square fa-fw"></i> Add Film</a></li>
                <li><a href="/"><i class="fa fa-logout fa-fw"></i> Logout</a></li>
            </c:if>
        </ul>
    </nav>

    <!-- Main -->
    <div id="main">
        <div class="inner">
            <section>
                <div>
                    <h1>${fileSuccess}</h1>
                    <form:form method="post" action="filmSaved" enctype="multipart/form-data">
                        <a href="uploadForm">Add another film</a>
                    </form:form>
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