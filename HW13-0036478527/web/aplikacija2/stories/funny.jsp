<%@page import="java.util.Random"%>
<%@page import="hr.fer.zemris.java.hw13.servlets.ColorSetterServlet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%!private String randomColor() {
		Random random = new Random();
		String color = Integer.toHexString(random.nextInt()).substring(0, 6);
		return color;
	}%>

<html>
<head>
<title>State Trooper pulls a car over</title>
</head>
<body bgColor="<%=session.getAttribute(ColorSetterServlet.BG_COLOR)%>">

	<h1>State Trooper pulls a car over</h1>

	<p style="color: <%=randomColor()%>">A Texas State trooper pulled
		a car over on I-35 about 2 miles south of Waco Texas. When the trooper
		asked the driver why he was speeding, the driver said he was a
		magician and a juggler and was on his way to Austin to do a show for
		the Shrine Circus. He didn't want to be late. The trooper told the
		driver he was fascinated by juggling and said that if the driver would
		do a little juggling for him, then he wouldn't give him a ticket. He
		told the trooper he had sent his equipment ahead and didn't have
		anything to juggle. The trooper said he had some flares and asked if
		he could juggle them. The juggler said he could, so the trooper got 5
		flares, lit them and handed them to the juggler. While the man was
		juggling, a car pulled in behind the State Trooper's car. A drunken
		good old boy from central Texas got out, watched the performance, then
		went over to the trooper's car, opened the rear door and got in. The
		trooper observed him and went over to his car and opened the door
		asking the drunk what he thought he was doing. The drunk replied, "You
		might as well take my ass to jail, cause there ain't no way I can pass
		that test."</p>
</body>
</html>
