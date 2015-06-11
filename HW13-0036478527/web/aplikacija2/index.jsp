<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body bgColor="<%=session.getAttribute(ColorSetterServlet.BG_COLOR)%>">
	<a href="colors.jsp">Background color chooser</a>
	<a href="trigonometric?a=0&b=90">Trigonometrics</a>
	<a href="stories/funny.jsp">State Trooper pulls a car over</a>
</body>
</html>