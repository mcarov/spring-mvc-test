<%@ page import="ru.itpark.domain.Movie" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <%@include file="bootstrap-css.jsp"%>

    <title>Remove</title>
</head>
<body>
<% Movie movie = (Movie)request.getAttribute("movie"); %>
<% List<String> translated = (List<String>)request.getAttribute("translation"); %>
<div class="container">
    <br>
    <h4 class="text-center mt-100"><%=translated.get(0)%> "<%=movie.getTitle()%>" <%=translated.get(1)%> ?</h4>
    <div class="d-flex flex-row justify-content-center">
        <form action="<%=request.getContextPath()%>/movies/<%=movie.getId()%>/remove" method="post">
            <button class="btn btn-outline-danger mr-2"><%=translated.get(2)%></button>
        </form>
        <a href="<%=request.getContextPath()%>/movies/<%=movie.getId()%>" class="btn btn-outline-success"><%=translated.get(3)%></a>
    </div>
</div>
</body>
</html>
