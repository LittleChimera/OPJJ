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
					</div></li>
			</c:forEach>
		</ul>
	</c:if>
	<form action="addComment" method="post">
		<input type="hidden" name="blogId" value="${blogEntry.id}">
		<c:if test="${!sessionScope.containsKey('current.user.id')}">
			<input type="text" name="email" placeholder="john.doe@none.com">
		</c:if>
		<textarea rows="5" cols="40" name="message"></textarea>
		<input type="submit" value="Add comment">
	</form>

</body>
</html>
