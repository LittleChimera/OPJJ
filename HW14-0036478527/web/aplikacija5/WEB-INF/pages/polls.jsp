<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<body bgColor="<%=session.getAttribute(ColorSetterServlet.BG_COLOR)%>">
	<h1>Select poll:</h1>
	<c:forEach var="p" items="${polls}">
		<li><a href="glasanje?pollId=${p.id}">${p.title}</a></li>	
	</c:forEach>
	</ol>
</body>
</html>