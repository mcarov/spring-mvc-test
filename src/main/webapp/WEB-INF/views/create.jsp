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

    <title>Create</title>
</head>
<body>
    <div class="container">
        <% if(request.getAttribute("translation") != null) { %>
            <% List<String> translated = (List<String>)request.getAttribute("translation"); %>
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <div class="collapse navbar-collapse">
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="<%=request.getContextPath()%>/"><%=translated.get(0)%></a>
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
                    </ul>
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item active">
                            <a class="nav-link"><%=translated.get(5)%></a>
                        </li>
                    </ul>
                    <form action="<%=request.getContextPath()%>/import" method="post" enctype="multipart/form-data">
                        <div class="input-group">
                            <div class="custom-file">
                                <label class="custom-file-label" for="csv-file"><%=translated.get(6)%></label>
                                <input type="file" class="custom-file-input" id="csv-file" name="csv-file" accept="text/csv" required>
                            </div>
                            <div class="input-group-append">
                                <button class="btn btn-outline-success" type="submit" name="import"><%=translated.get(7)%></button>
                            </div>
                        </div>
                    </form>
                </div>
            </nav>
            <br>
            <form action="<%=request.getContextPath()%>/create">
                <div class="form-group">
                    <label for="title">Название</label>
                    <input id="title" name="title" class="form-control" type="text" required>
                </div>
            </form>
        <% } %>
    </div>
    <%@include file="bootstrap-scripts.jsp"%>
</body>
</html>

