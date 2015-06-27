<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
</head>
<body>

	<c:choose>
		<c:when test="${sessionScope.containsKey('current.user.id')}">
			<h1 style="text-align: center">Welcome
				${sessionScope.get('current.user.fn')}
				${sessionScope.get('current.user.ln')}!</h1>
			<a href="logout">Logout</a>
		</c:when>
		<c:otherwise>
			<h2>Login</h2>
			<c:forEach var="e" items="${loginErrors}">
				<p style="color: red">${e}</p>
			</c:forEach>
			<form action="login" method="post">
				Nick: <input type="text" name="nick" placeholder="Johny"
					required="required"><br> Password: <input
					type="password" name="password" required="required"><br>
				<input type="submit" name="metoda" value="Login">
			</form>

			<hr>

			<h2>Register</h2>
			<c:forEach var="e" items="${registerErrors}">
				<p style="color: red">${e}</p>
			</c:forEach>
			<form action="register" method="post">
				First name: <input type="text" name="firstName" placeholder="John"
					required="required"><br> Last name: <input type="text"
					name="lastName" placeholder="Doe" required="required"><br>
				Nick: <input type="text" name="nick" placeholder="Johny"
					required="required"><br> E-mail: <input type="email"
					name="email" placeholder="john.doe@none.com" required="required"><br>
				Password: <input type="password" name="password" required="required"><br>
				<input type="submit" name="metoda" value="Register">
			</form>
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${blogAuthors.isEmpty()}">
			<h3>No registered authors yet!</h3>
		</c:when>
		<c:otherwise>
			<h3>Authors:</h3>
			<c:forEach var="a" items="${blogAuthors}">
				<a href="author/${a.nick}">${a.nick}</a>
			</c:forEach>

		</c:otherwise>
	</c:choose>


</body>
</html>
