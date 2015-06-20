<%@page import="hr.fer.zemris.java.hw13.voting.VotingHelper"%>
<%@page import="hr.fer.zemris.java.hw13.model.PollEntry"%>
<%@page import="java.util.List"%>
<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<style type="text/css">
table.rez td {
	text-align: center;
}
</style>
</head>
<body bgColor="<%=session.getAttribute(ColorSetterServlet.BG_COLOR)%>">
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Option title</th>
				<th>Votes count</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="r" items="${results}">
				<tr>
					<td>${r.optionTitle}</td>
					<td>${r.votesCount}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart"
		src="glasanje-grafika?pollId=<%=request.getParameter("pollId")%>"
		width="600" height="400" />
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a
			href="glasanje-xls?pollId=<%=request.getParameter("pollId")%>">ovdje</a>
	</p>
	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<%
		VotingHelper.sortResults((List<PollEntry>)request.getAttribute("results"));
	%>
	<ul>
		<c:forEach var="r" items="${results}" varStatus="status">
			<c:if test="${status.index == 0}">
				<c:set var="maxVotes" value="${r.votesCount}"></c:set>
			</c:if>
			<c:if test="${maxVotes == r.votesCount}">
				<li><a href="${r.optionLink}" target="_blank">
						${r.optionTitle}</a></li>
			</c:if>
		</c:forEach>
	</ul>
</body>
</html>