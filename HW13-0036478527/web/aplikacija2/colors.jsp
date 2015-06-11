<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body bgColor=<%= session.getAttribute(ColorSetterServlet.BG_COLOR) %>>
	<a href=${pageContext.request.contextPath}/setcolor?<%= ColorSetterServlet.BG_COLOR %>=FFFFFF>WHITE</a>
	<a href=${pageContext.request.contextPath}/setcolor?<%= ColorSetterServlet.BG_COLOR %>=FF4136>RED</a>
	<a href=${pageContext.request.contextPath}/setcolor?<%= ColorSetterServlet.BG_COLOR %>=2ECC40>GREEN</a>
	<a href=${pageContext.request.contextPath}/setcolor?<%= ColorSetterServlet.BG_COLOR %>=00FFFF>CYAN</a>
	
	<br>
	
	<a href=index.jsp>Index</a>
	
</body>
</html>