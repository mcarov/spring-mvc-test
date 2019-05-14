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

    <title>Top 20</title>
</head>
<body>
    <div class="container">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <% if(request.getAttribute("translation") != null) { %>
                        <% List<String> translated = (List<String>)request.getAttribute("translation"); %>
                        <li class="nav-item active">
                            <a class="nav-link"><%=translated.get(0)%></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%=request.getContextPath()%>/movies/page/1"><%=translated.get(1)%></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%=request.getContextPath()%>/collections/page/1"><%=translated.get(2)%></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%=request.getContextPath()%>/genres"><%=translated.get(3)%></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%=request.getContextPath()%>/companies/page/1"><%=translated.get(4)%></a>
                        </li>
                    <% } else { %>
                        <li class="nav-item active">
                            <a class="nav-link">Top 20</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%=request.getContextPath()%>/movies/page/1">Movies</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%=request.getContextPath()%>/collections/page/1">Collections</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%=request.getContextPath()%>/genres">Genres</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%=request.getContextPath()%>/companies/page/1">Companies</a>
                        </li>
                    <% } %>
                </ul>
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/create">Add new movie</a>
                    </li>
                </ul>
                <form action="<%=request.getContextPath()%>/import" method="post" enctype="multipart/form-data">
                    <div class="input-group">
                        <div class="custom-file">
                            <label class="custom-file-label" for="csv-file">Choose csv file...</label>
                            <input type="file" id="csv-file" name="csv-file" accept="text/csv" required>
                        </div>
                        <div class="input-group-append">
                            <button class="btn btn-outline-success" type="submit">Import</button>
                        </div>
                    </div>
                </form>
            </div>
        </nav>
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
                <% if(request.getAttribute("movies") != null){ %>
                    <% List<Movie> movies = (List<Movie>)request.getAttribute("movies"); %>
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
    <%@include file="bootstrap-scripts.jsp"%>
</body>
</html>
