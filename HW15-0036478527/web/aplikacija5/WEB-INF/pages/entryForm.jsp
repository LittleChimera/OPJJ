<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
<c:choose>
		<c:when test="${sessionScope.containsKey('current.user.id')}">
			<p style="text-align: center; display: inline">Welcome
				${sessionScope.get('current.user.fn')}
				${sessionScope.get('current.user.ln')}!</p>
			<a href="logout">Logout</a>
		</c:when>
		<c:otherwise>
			<p>Not logged in.</p>
		</c:otherwise>
	</c:choose>
	<form action="save" method="post">
		
		<input type="hidden" name="id" value="${blogEntry.id}"><br>
		
		Title: <input type="text" name="title" value='<c:out value="${blogEntry.title}"/>' size="40"><br>

		Blog entry body:<br>
		<textarea rows="10" cols="80" name="blogEntryBody">
			${blogEntry.text}
		</textarea>

		<input type="submit" name="metoda" value="Save">
		
		</form>
</body>
</html>
