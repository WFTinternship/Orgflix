<%--
  Main home page
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "m" uri = "/WEB-INF/taglib/customtags.tld" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<!-- Head and CSS Import -->
<%@ include file="/WEB-INF/pages/header.jsp" %>
<body>
<!-- Wrapper -->
<div id="wrapper">
    <!-- Page header -->
    <m:pageHeader />
    <!-- Navigation menu -->
    <%@ include file="/WEB-INF/pages/menu.jsp" %>
    <!-- Main page -->
    <div id="main">
        <div class="inner">
            <header>
                <c:if test="${ (page == 'index') || (sessionScope.userId==-1)}">
                    <h1>This is OrgFlix, a free organizer for your flix</h1>
                </c:if>
                <c:if test="${page == 'watch_list'}">
                    <h1>Watch list</h1>
                </c:if>
                <c:if test="${page == 'wish_list'}">
                    <h1>Wish list</h1>
                </c:if>
                <c:if test="${page == 'watchListUserOther'}">
                    <h1>${title}</h1>
                </c:if>
                <c:if test="${page == 'wishListUserOther'}">
                    <h1>${title}</h1>
                </c:if>
            </header>
            <section class="tiles">
                <c:forEach items="${films}" var="film" varStatus="loop">
                    <article class='style1' id="${film.id}" onmouseout='filmControlMenu(false,this.id)' onmouseover='filmControlMenu(true,this.id)'>
                        <div class='image' >
                            <img src='<c:url value="../resources/images/${film.image}" />' />

                        </div>
                        <c:if test="${sessionScope.userId == -1}">
                            <div class="film-buttons average-star">
                                <i class="fa fa-star fa-sm"></i> (<c:out value="${ratings[loop.index]}"/> )
                            </div>
                        </c:if>
                        <c:if test="${sessionScope.userId != -1}">
                            <div id="contr_${film.id}" class="film-buttons">
                                <c:if test="${page == 'index'}">
                                    <div class="tableCell">
                                        <a href="#" class="no_link" onclick="AddToList('watch','${film.id}')"><i class="fa fa-watchList fa-fw"></i></a>
                                        <a href="#" class="no_link" onclick="AddToList('wish','${film.id}')"><i class="fa fa-wishList fa-fw"></i></a>
                                        <a onclick="isPublic(${film.id})" class="no_link"><i id="publicCheck_${film.id}" class="fa fa-square-o fa-fw"></i> Public</a>
                                        <input type="hidden" id="isPublic_${film.id}" value="0" class="pointerA"/>
                                    </div>
                                    <div class="tableCell">
                                        <a onmouseover="starFilm(1,false,${film.id})" onmouseout="starFilm(0,false,${film.id})" onclick="recordStar(1,${film.id})" class="starSelect pointerA"><i class="fa fa-star fa-sm"></i></a>
                                        <a onmouseover="starFilm(2,false,${film.id})" onmouseout="starFilm(0,false,${film.id})" onclick="recordStar(2,${film.id})" class="starSelect pointerA"><i class="fa fa-star fa-sm"></i></a>
                                        <a onmouseover="starFilm(3,false,${film.id})" onmouseout="starFilm(0,false,${film.id})" onclick="recordStar(3,${film.id})" class="starSelect pointerA"><i class="fa fa-star fa-sm"></i></a>
                                        <a onmouseover="starFilm(4,false,${film.id})" onmouseout="starFilm(0,false,${film.id})" onclick="recordStar(4,${film.id})" class="starSelect pointerA"><i class="fa fa-star fa-sm"></i></a>
                                        <a onmouseover="starFilm(5,false,${film.id})" onmouseout="starFilm(0,false,${film.id})" onclick="recordStar(5,${film.id})" class="starSelect pointerA"><i class="fa fa-star fa-sm"></i></a>
                                        <input type="hidden" id="stars_${film.id}" value="1" />
                                    </div>
                                    <c:if test="${film.hasOscar}">
                                        <div class="keep-right">
                                            <i class="fa fa-trophy fa-fw"></i>
                                        </div>
                                    </c:if>
                                </c:if>
                                <c:if test="${page == 'watch_list'}">
                                    <a href="#" class="no_link" onclick="RemoveFromList('watch','${film.id}')"><i class="fa fa-trash-o fa-fw"></i></a>
                                    <c:if test="${privacyList[loop.index] == 0}">
                                        <a href="#" class="no_link" data-privacy="true" onclick="invertPrivacy(this,'${film.id}')"><i class="fa fa-lock fa-fw"></i></a>
                                    </c:if>
                                    <c:if test="${privacyList[loop.index] == 1}">
                                        <a href="#" class="no_link" data-privacy="false" onclick="invertPrivacy(this,'${film.id}')"><i class="fa fa-unlock fa-fw"></i></a>
                                    </c:if>
                                </c:if>
                                <c:if test="${page == 'wish_list'}">
                                    <a href="#" class="no_link" onclick="RemoveFromList('wish','${film.id}')"><i class="fa fa-trash-o fa-fw"></i></a>
                                    <c:if test="${privacyList[loop.index] == 0}">
                                        <a href="#" class="no_link" data-privacy="true" onclick="invertPrivacy(this,'${film.id}')"><i class="fa fa-lock fa-fw"></i></a>
                                    </c:if>
                                    <c:if test="${privacyList[loop.index] == 1}">
                                        <a href="#" class="no_link" data-privacy="false" onclick="invertPrivacy(this,'${film.id}')"><i class="fa fa-unlock fa-fw"></i></a>
                                    </c:if>
                                </c:if>
                            </div>
                        </c:if>
                        <div class="film-ref">
                            <h2><c:out value="${film.title}" /></h2>
                            <div class="genreStyle">
                                <c:forEach items="${film.genres}" var="genre" varStatus="loop" >
                                    <c:out value="${genre}"/>
                                    <c:if test="${loop.index+1 == film.genres.size()}">, </c:if>
                                </c:forEach>
                            </div>
                            <c:forEach items="${film.casts}" var="cast" >
                                <div class="castStyle"><c:out value="${cast.name}"/></div>
                            </c:forEach>
                        </div>
                    </article>
                </c:forEach>
                <c:if test="${numOfPages != 0}">
                    <div class="pagintion_container">
                        <c:forEach var = "i" begin = "0" end = "${numOfPages}" >
                            <div class="pagination" id="goTo_${i}" onclick='pagination(this.id,"${page}")'>${i+1}</div>
                        </c:forEach>
                    </div>
                </c:if>

                <div id="outer-pop-up">
                    <div id="inner-pop-up" ></div>
                </div>
            </section>
        </div>
    </div>
    <!-- Page footer -->
    <m:pageFooter />
</div>
<!-- JS imports -->
<%@ include file="/WEB-INF/pages/JSImport.jsp" %>
</body>
</html>