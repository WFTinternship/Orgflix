<%--
  Page for user sign up
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="m" uri="/WEB-INF/taglib/customtags.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<!-- Head and CSS Import -->
<%@ include file="/WEB-INF/pages/header.jsp" %>
<body>
<!-- Wrapper -->
<div id="wrapper">
    <!-- Page header -->
    <m:pageHeader/>
    <!-- Navigation menu -->
    <%@ include file="/WEB-INF/pages/menu.jsp" %>
    <!-- Main page-->
    <div id="main">
        <div class="inner">
            <header>
                <h1>Sign up to OrgFlix</h1>
            </header>
            <section>
                <form id="signUpForm" method="POST" action="/signed">
                    <div>
                        <span>Nick</span>
                        <input type="text" name="nick" id="nick" class="inputField"/>
                    </div>
                    <div>
                        <span>User name</span>
                        <input type="text" name="userName" class="inputField"/>
                    </div>
                    <div>
                        <span>Email</span>
                        <input type="text" name="email" id="email" class="inputField"/>
                    </div>
                    <div>
                        <span>Password</span>
                        <input type="password" id="pass" name="pass" id="pass" class="inputField"/>
                    </div>
                    <div>
                        <span>Confirm password</span>
                        <input type="password" id="pass_check" class="inputField"/>
                    </div>
                    <input type="button" value="submit" onclick="submitSignUp()"/>

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