<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>Image File Upload</title>
</head>
<body>
<h3>${requestScope["message"]}</h3>
<h3 style="color:#4581ff">${fileSuccess}</h3>
<form:form method="post" action="filmSaved" enctype="multipart/form-data">
    <a href="uploadForm">Add another film</a>
</form:form>
</body>
</html>