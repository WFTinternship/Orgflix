<%--
  Page footer template
--%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!-- Menu -->
<nav id="menu">
    <h2>Menu</h2>
    <ul>
        <c:if test="${sessionScope.userId != -1}">
            <li><i class="fa fa-user fa-fw"></i> <c:out value="${sessionScope.user}"/></li>
            <li><a onclick="navigator('index')"><i class="fa fa-home fa-fw"></i>Home</a></li>
            <li><a onclick="navigator('watch_list')"><i class="fa fa-watchList fa-fw"></i> Watch list</a></li>
            <li><a onclick="navigator('wish_list')"><i class="fa fa-wishList fa-fw"></i> Wish List</a></li>
            <li><a onclick="navigator('addFilm')"><i class="fa fa-plus-square fa-fw"></i> Add Film</a></li>
            <li><a onclick="navigator('searchFilm')"><i class="fa fa-search fa-fw"></i> Search Film</a></li>
            <li><a onclick="navigator('logout')"><i class="fa fa-logout fa-fw"></i> Logout</a></li>
        </c:if>
        <c:if test="${sessionScope.userId == -1}">
            <li><a onclick="navigator('/')"><i class="fa fa-home fa-fw"></i>Home</a></li>
            <li><a onclick="navigator('login')"><i class="fa fa-login fa-fw"></i> Login</a></li>
            <li><a onclick="navigator('signup')"><i class="fa fa-user-plus fa-fw"></i> Sign up</a></li>
        </c:if>
    </ul>
    <form id="navigator" method="POST" action="0">
        <input type="hidden" id="currPage" name="currentPage" value="${currentPage}"/>
        <input type="hidden" id="page" name="page" value="${page}"/>
    </form>
</nav>
