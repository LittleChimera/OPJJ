<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Error</title>
</head>
<body bgColor=<%=session.getAttribute(ColorSetterServlet.BG_COLOR)%>>
	<h1>Following error has occurred while visiting:</h1>
	<a href="${pageContext.request.requestURL}">${pageContext.request.requestURL}</a>
	<br>
	<p>${error}</p>

	<br>

	<a href=index.jsp>Index</a>
</body>
</html>