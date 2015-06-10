<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Kvadarti brojeva</title>
</head>
<body>
	<h1>Popis trazenih kvadarata brojeva</h1>
	<p>Popis se sastoji od ${rezultati.size()} zapisa.</p>
	<table border="1" cellspacing="0">
		<thead>
			<tr>
				<th>Redni broj zapisa</th>
				<th>Kvadrat boja</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="z" items="${rezultati}" varStatus="status">
				<tr>
					<td>${status.index + 1}.</td>
					<td>${z.broj}</td>
					<td>${z.kvadrat}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>