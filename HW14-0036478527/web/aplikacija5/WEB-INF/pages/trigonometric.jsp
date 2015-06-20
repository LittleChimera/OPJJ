<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Trigonometric functions</title>
</head>
<body bgColor=<%= session.getAttribute(ColorSetterServlet.BG_COLOR) %>>
	<h1>List of sinuses and cosinuses for integers between ${start} and ${end}.</h1>
	<p>Table consists of ${rezultati.size()} rows.</p>
	<table border="1" cellspacing="0">
		<thead>
			<tr>
				<th>x</th>
				<th>Sin(x)</th>
				<th>Cos(x)</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="sc" items="${results}" varStatus="status">
				<tr>
					<td>${start + status.index}.</td>
					<td>${sc.sin}</td>
					<td>${sc.cos}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<br>
	
	<a href=index.jsp>Index</a>
</body>
</html>