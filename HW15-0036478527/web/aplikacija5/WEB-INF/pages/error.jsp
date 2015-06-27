<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Error</title>
</head>
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
	<h1>${error}</h1>
</body>
</html>