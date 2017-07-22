<%--
  Page for adding new film
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="example-taglib" %>
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
                <h1>Search for a Film</h1>
            </header>
            <section>
                <h3 style="color:#4581ff">${filesuccess}</h3>
                <form id="searchFilm" method="GET" action="searchFilmResult">
                    <div>
                        <span>Film Title</span>
                        <input type="text" id="title" name="title" class="inputField"/>
                    </div>
                    <div style="display: table">
                        <div class="tableCell">
                            <span>Release Date</span>
                            <div style="display: table">
                                <div class="tableCell">
                                    <select id="startYear" name="startYear" class="dateField">
                                        <c:forEach begin="1900" end="2016" var="val">
                                            <option><c:out value="${val}"/></option>
                                        </c:forEach>
                                        <option selected="selected">2017</option>
                                    </select>
                                </div>
                                <div class="tableCell"> - </div>
                                <div class="tableCell">
                                    <select id="finishYear" name="finishYear" class="dateField">
                                        <c:forEach begin="1900" end="2016" var="val">
                                            <option><c:out value="${val}"/></option>
                                        </c:forEach>
                                        <option selected="selected">2017</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                         <div class="tableCell textCentered">
                            <span>Awards(Oscar)</span><br />
                            <a onclick="setHasOscar('Film')"><i id="oscarCheckFilm" class="fa fa-square-o fa-fw"></i></a>
                            <input type="hidden" id="hasOscarFilm" name="hasOscar" value="0" class="pointerA"/>
                        </div>
                        <div class="tableCell">
                            <span>Director of the film</span>
                            <input type="text" id="director" name="director" class="inputField"/>
                        </div>
                        <div class="tableCell">
                            <span>Genre</span>
                            <select id="genre" name="genre" class="dateField">
                                <c:forEach items="${genres}" var="genre">
                                    <option value="${genre.value}">${genre.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="tableCell" style="width:300px">
                            <span>Actor</span>
                            <div class="container">
                                <div class="elem longElem">
                                    <input type="text" onclick="getActorsList(this,true)" onkeyup="getActorsList(this,false)" class="inputField" style="margin-bottom: 0px"/>
                                    <input type="hidden" name="actorId" class="actorId" value="-1">
                                    <div></div>
                                </div>
                            </div>
                            <input type="hidden" id="buffer" />
                        </div>
                    </div>


                    <div>
                        <br /><br /><br />
                        <input type="submit" value="Search"/>
                    </div>
                </form>
                <div id="error" class="error"></div>
            </section>
        </div>
    </div>
    <!-- Page footer -->
    <m:pageFooter/>
</div>
<!-- JS imports -->
<%@ include file="/WEB-INF/pages/JSImport.jsp" %>
</body>
</html>