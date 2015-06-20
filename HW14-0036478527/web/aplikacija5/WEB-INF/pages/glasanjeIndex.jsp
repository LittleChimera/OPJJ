<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<body bgColor="<%=session.getAttribute(ColorSetterServlet.BG_COLOR)%>">
	<h1>${poll.title}</h1>
	<p>${poll.message}</p>
	<ol>
		<c:forEach var="de" items="${definition}">
			<li><a
				href="glasanje-glasaj?id=${de.id}">${de.optionTitle}</a></li>
		</c:forEach>
	</ol>
</body>
</html>