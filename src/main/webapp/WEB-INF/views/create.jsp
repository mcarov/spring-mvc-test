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
                <div class="row">
                    <div class="col-6">
                        <div class="form-group">
                            <label for="title">Название</label>
                            <input id="title" name="title" class="form-control" type="text" required>
                        </div>
                        <div class="form-group">
                            <label for="tagline">Слоган</label>
                            <input id="tagline" name="tagline" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="original-title">Оригинальное название</label>
                            <input id="original-title" name="original-title" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="original-language">Оригинальный язык</label>
                            <input id="original-language" name="original-language" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="release-date">Дата выхода</label>
                            <input id="release-date" name="release-date" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="status">Статус</label>
                            <input id="status" name="status" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="runtime">Продолжительность</label>
                            <input id="runtime" name="runtime" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="genres">Жанры</label>
                            <input id="genres" name="genres" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="companies">Компании-производители</label>
                            <input id="companies" name="companies" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="countries">Страны производства</label>
                            <input id="countries" name="countries" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <label for="spoken-languages">Разговорные языки</label>
                            <input id="spoken-languages" name="spoken-languages" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="budget">Бюджет</label>
                            <input id="budget" name="budget" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="revenue">Сборы</label>
                            <input id="revenue" name="revenue" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="homepage">Домашняя страница</label>
                            <input id="homepage" name="homepage" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="popularity">Популярность</label>
                            <input id="popularity" name="popularity" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="votes">Количество голосов</label>
                            <input id="votes" name="votes" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="rating">Рейтинг</label>
                            <input id="rating" name="rating" class="form-control" type="text">
                        </div>
                        <div class="form-group">
                            <label for="overview">Обзор</label>
                            <textarea id="overview" name="overview" class="form-control no-resize"></textarea>
                        </div>
                        <div class="form-group">
                            <label for="keywords">Теги</label>
                            <textarea  id="keywords" name="keywords" class="form-control no-resize"></textarea>
                        </div>
                    </div>
                </div>
            </form>
        <% } %>
    </div>
    <%@include file="bootstrap-scripts.jsp"%>
</body>
</html>

