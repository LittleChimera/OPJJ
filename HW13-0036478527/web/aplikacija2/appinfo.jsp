<%@page import="hr.fer.zemris.java.hw13.servlets.AppInfo"%>
<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Running time</title>
</head>
<body bgColor=<%=session.getAttribute(ColorSetterServlet.BG_COLOR)%>>
	<h1>App's running time:</h1>
	<h3><%=AppInfo.formatElapsedTime((long)pageContext.getServletContext().getAttribute("startTime"))%></h3>
	
	

	<br>

	<a href=index.jsp>Index</a>
</body>
</html>