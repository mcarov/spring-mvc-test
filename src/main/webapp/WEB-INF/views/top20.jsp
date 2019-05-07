<%@ page import="ru.itpark.domain.Movie" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%@include file="bootstrap-css.jsp"%>

    <title>Movies Top 20</title>
</head>
<body>
    <div class="container">
        <h1 class="text-center">Movies Top 20</h1>
        <ol>
            <% if(request.getAttribute("top20") != null){ %>
            <% for(Movie movie : (List<Movie>)request.getAttribute("top20")){ %>
            <li>
                <%=movie.getOriginalTitle()%> : <%=movie.getPopularity()%>
            </li>
            <% } %>
            <% } %>
        </ol>
    </div>
</body>
</html>
