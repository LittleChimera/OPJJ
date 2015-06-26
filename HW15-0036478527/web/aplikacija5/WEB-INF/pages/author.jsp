<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
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
							<a href="edit?id=${blogEntry.id}" style="display: inline">Edit</a>
						</c:if>
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
