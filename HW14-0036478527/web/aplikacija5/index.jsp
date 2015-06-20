<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Home</title>
</head>
<body bgColor="<%=session.getAttribute(ColorSetterServlet.BG_COLOR)%>">
	<ul>
		<li><a href="colors.jsp">Background color chooser</a></li>
		<li><a href="trigonometric?a=0&b=90">Trigonometrics</a></li>
		<li><a href="stories/funny.jsp">State Trooper pulls a car
				over</a></li>
		<li><a href="report.jsp">OS Report</a></li>
		<li><a href="appinfo.jsp">App Info</a></li>
		<li><a href="glasanje">Glasanje</a></li>
	</ul>
</body>
</html>