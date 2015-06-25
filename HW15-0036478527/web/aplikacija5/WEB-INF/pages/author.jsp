<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
	<h1>${author}</h1>
	<c:choose>
		<c:when test="${author != null}">
			<c:param name="blogEntry" value="${author.entries}" ></c:param>
			<c:choose>
				<c:when test="${blogEntry.isEmpty()}">
					<p>No entries!</p>
				</c:when>
				<c:otherwise>
					<h1>
						<c:out value="${blogEntry.title}" />
					</h1>
					<p>
						<c:out value="${blogEntry.text}" />
					</p>
					<c:if test="${!blogEntry.comments.isEmpty()}">
						<ul>
							<c:forEach var="e" items="${blogEntry.comments}">
								<li><div style="font-weight: bold">
										[Korisnik=
										<c:out value="${e.usersEMail}" />
										]
										<c:out value="${e.postedOn}" />
									</div>
									<div style="padding-left: 10px;">
										<c:out value="${e.message}" />
									</div> <c:if test="${author.equals(sessionScope.get(user.nick))}">
										<a href="edit?id=${blogEntry.id}">Edit</a>
									</c:if></li>
							</c:forEach>
						</ul>
						<c:if test="${author.equals(sessionScope.get(user.nick))}">
							<a href="new">Create new entry</a>
						</c:if>
					</c:if>
				</c:otherwise>
			</c:choose>

		</c:when>

		<c:otherwise>
			<h2>Unknown author!</h2>
		</c:otherwise>
	</c:choose>

</body>
</html>
