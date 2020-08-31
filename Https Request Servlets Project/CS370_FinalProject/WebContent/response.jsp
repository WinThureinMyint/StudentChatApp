<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
table td {
	font-size: 14px;
}
</style>
<title>Upload File Response</title>
</head>
<body>
	<a href="index.html">Home Page</a>
	<%-- Using JSP EL to get message attribute value from request scope --%>
	<h2>${requestScope.fileName}${requestScope.message}</h2>

</body>
</html>