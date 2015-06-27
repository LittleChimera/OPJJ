<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
	<c:choose>
		<c:when test="${sessionScope.containsKey('current.user.id')}">
			<p style="text-align: center; display: inline">Welcome
				${sessionScope.get('current.user.fn')}
				${sessionScope.get('current.user.ln')}!</p>
			<a href="/aplikacija5/servleti/logout">Logout</a>
		</c:when>
		<c:otherwise>
			<p>Not logged in.</p>
		</c:otherwise>
	</c:choose>
	<h1>${author.nick}</h1>
	<c:choose>
		<c:when test="${author != null}">
			<c:choose>
				<c:when test="${blogEntry.isEmpty()}">
					<p>No entries!</p>
				</c:when>
				<c:otherwise>
					<c:forEach var="blogEntry" items="${blogEntries}">
						<h3 style="display: inline"><c:out value="${blogEntry.title}" /></h3>
						<a href="${author.nick}/${blogEntry.id}" style="display: inline">Show</a>

						<c:if
							test="${author.nick.equals(sessionScope.get('current.user.nick'))}">
							<a href="${author.nick}/edit?id=${blogEntry.id}" style="display: inline">Edit</a>
						</c:if>
						<br>
					</c:forEach>
				</c:otherwise>
			</c:choose>

		</c:when>

		<c:otherwise>
			<h2>Unknown author!</h2>
		</c:otherwise>
	</c:choose>

	<c:if
		test="${author.nick.equals(sessionScope.get('current.user.nick'))}">
		<a href="${author.nick}/new">Create new entry</a>
	</c:if>

</body>
</html>
