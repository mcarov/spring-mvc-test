<%@ page import="java.util.List" %>
<%@ page import="ru.itpark.domain.ProductionCompany" %>
<%@ page contentType="text/html;charset=UTF-16" language="java" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-16">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <%@include file="bootstrap-css.jsp"%>

    <title>Companies</title>
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="<%=request.getContextPath()%>/">Top 20</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<%=request.getContextPath()%>movies/page/1">Movies</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Collections</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<%=request.getContextPath()%>genres">Genres</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link">Companies</a>
                </li>
            </ul>
            <form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2" type="search" placeholder="Search for..." aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Go!</button>
            </form>
        </div>
    </nav>
    <br>
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col"></th>
            <th scope="col">production company</th>
        </tr>
        </thead>
        <tbody>
        <% if(request.getAttribute("companies") != null){ %>
        <% List<ProductionCompany> companies = (List<ProductionCompany>)request.getAttribute("companies"); %>
        <% for(int i = 0; i < companies.size(); i++){ %>
        <tr>
            <th scope="row"><%=i+1%></th>
            <td><a href="<%=request.getContextPath()%>/companies/<%=companies.get(i).getId()%>"><%=companies.get(i).getName()%></a></td>
        </tr>
        <% } %>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
