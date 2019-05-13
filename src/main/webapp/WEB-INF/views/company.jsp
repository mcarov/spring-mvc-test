<%@ page import="ru.itpark.domain.Movie" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-16" language="java" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-16">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <%@include file="bootstrap-css.jsp"%>

    <title>Company</title>
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="<%=request.getContextPath()%>/">Top 20</a>
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
            </ul>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="<%=request.getContextPath()%>/create">Add new movie</a>
                </li>
            </ul>
            <form action="<%=request.getContextPath()%>/" method="post" enctype="multipart/form-data">
                <div class="input-group">
                    <div class="custom-file">
                        <input type="file" class="custom-file-input" id="inputGroupFile04" aria-describedby="inputGroupFileAddon04">
                        <label class="custom-file-label" for="inputGroupFile04">Choose csv file...</label>
                    </div>
                    <div class="input-group-append">
                        <button class="btn btn-outline-success" type="button" id="inputGroupFileAddon04">Import</button>
                    </div>
                </div>
            </form>
        </div>
    </nav>
    <br>
    <h2 class="text-center">
        <%=request.getAttribute("company")%>
    </h2>
    <br>
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col"></th>
            <th scope="col">movie</th>
            <th scope="col">release date</th>
        </tr>
        </thead>
        <tbody>
        <% if(request.getAttribute("movies") != null) { %>
        <% List<Movie> movies = (List<Movie>)request.getAttribute("movies"); %>
        <% for(int i = 0; i < movies.size(); i++) { %>
            <tr>
                <th scope="row"><%=i+1%></th>
                <td><a href="<%=request.getContextPath()%>/movies/<%=movies.get(i).getId()%>"><%=movies.get(i).getTitle()%></a></td>
                <td>
                    <% SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); %>
                    <%=dateFormat.format(movies.get(i).getReleaseDate())%>
                </td>
            </tr>
        <% } %>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
