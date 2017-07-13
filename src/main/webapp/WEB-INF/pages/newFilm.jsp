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
                <h1>Add Film to OrgFlix</h1>
            </header>
            <section>
                <h3 style="color:#4581ff">${filesuccess}</h3>
                <form id="newFilm" method="POST" action="/newFilmResult" enctype="multipart/form-data">
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
                            <a onmouseover="starFilm(1,false,'')" onmouseout="starFilm(0,false,'')" onclick="starFilm(1,true,'')" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <a onmouseover="starFilm(2,false,'')" onmouseout="starFilm(0,false,'')" onclick="starFilm(2,true,'')" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <a onmouseover="starFilm(3,false,'')" onmouseout="starFilm(0,false,'')" onclick="starFilm(3,true,'')" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <a onmouseover="starFilm(4,false,'')" onmouseout="starFilm(0,false,'')" onclick="starFilm(4,true,'')" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <a onmouseover="starFilm(5,false,'')" onmouseout="starFilm(0,false,'')" onclick="starFilm(5,true,'')" class="starSelect pointerA"><i class="fa fa-star fa-fw"></i></a>
                            <span>(1 star)</span>
                            <input type="hidden" id="stars_" name="stars" value="1" />
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
                        <div class="container">
                            <div class="elem longElem">
                                <input type="text" onclick="getActorsList(this,true)" onkeyup="getActorsList(this,false)" class="inputField" style="margin-bottom: 0px"/>
                                <input type="hidden" name="actorId" class="actorId" value="-1">
                                <div></div>
                            </div>
                            <div class="elem">
                                <a onclick="addActor()"><i id="addActor" class="fa fa-plus-square fa-2x"></i></a>
                            </div>
                        </div>
                        <input type="hidden" id="buffer" />
                        <input type="hidden" id="numOfActors" name="numOfActors" value="1"/>
                        <input type="hidden" name="userId" value="${userId}"/>
                        <input type="hidden" name="userAuth" value="${userAuth}"/>
                        <input type="button" onclick="newActor()" value="New actor" />
                        <div class="hiddenElement" id="tempNewActor">
                            <input type="text" class="inputField" id="newActorName" name="newActorName" />
                            <input type="button" value="Add" onclick="addNewActor()">
                        </div>
                    </div>
                    <div>
                        <input type="button" onclick="submitNewFilm()" value="Submit"/>
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