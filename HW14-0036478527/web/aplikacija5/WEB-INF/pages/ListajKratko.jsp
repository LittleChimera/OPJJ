<%@page import="hr.fer.zemris.java.hw13.model.PollEntry"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<%
  List<PollEntry> entries = (List<PollEntry>)request.getAttribute("entries");
%>
<html>
  <body>

  <b>Pronađeni su sljedeći unosi:</b><br>

  <% if(entries.isEmpty()) { %>
    Nema unosa.
  <% } else { %>
    <ul>
    <% for(PollEntry e : entries) { %>
    <li>[ID=<%= e.getId() %>] <%= e.getOptionTitle() %> </li>  
    <% } %>  
    </ul>
  <% } %>

  </body>
</html>
