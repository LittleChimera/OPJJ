<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>OS Usage</title>
</head>
<body bgColor="<%=session.getAttribute(ColorSetterServlet.BG_COLOR)%>">
	<h1>OS Usage</h1>
	<p>Here are the results of OS usage in survey that we completed.</p>
	<img src='reportImage' ALT='OS statistics' />
	
	<br>
	
	<a href=index.jsp>Index</a>
</body>
</html>