<%@ page import="ru.itpark.domain.Movie" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-16" language="java" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-16">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <%@include file="bootstrap-css.jsp"%>

    <title>Movies Top 20</title>
</head>
<body>
    <div class="container">
        <h1 class="text-center">Movies Top 20</h1>
        <br>
        <table class="table">
            <thead class="thead-dark">
                <tr>
                    <th scope="col"></th>
                    <th scope="col">movie</th>
                    <th scope="col">popularity</th>
                </tr>
            </thead>
            <tbody>
                <% if(request.getAttribute("top20") != null){ %>
                    <% List<Movie> movies = (List<Movie>)request.getAttribute("top20"); %>
                    <% for(int i = 0; i < movies.size(); i++){ %>
                        <tr>
                            <th scope="row"><%=i+1%></th>
                            <td><a href="<%=request.getContextPath()%>/movies/<%=movies.get(i).getId()%>"><%=movies.get(i).getTitle()%></a></td>
                            <td><%=String.format("%.3f", movies.get(i).getPopularity())%></td>
                        </tr>
                    <% } %>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
