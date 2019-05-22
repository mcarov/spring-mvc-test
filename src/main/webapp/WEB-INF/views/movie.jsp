<%@ page import="ru.itpark.domain.*" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
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

    <title>Movie</title>
</head>
<body>
<div class="container">
    <% if(request.getAttribute("translation") != null && request.getAttribute("movie") != null) { %>
    <% List<String> translated = (List<String>)request.getAttribute("translation"); %>
    <% Movie movie = (Movie)request.getAttribute("movie"); %>
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
                <li class="nav-item">
                    <a class="nav-link" href="<%=request.getContextPath()%>/movies/0/edit"><%=translated.get(5)%></a>
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
    <div class="d-flex flex-row justify-content-between">
        <div class="d-flex flex-column">
            <a href="<%=request.getContextPath()%>/movies/<%=movie.getId()%>/edit" class="btn btn-outline-success align-self-stretch my-2"><%=translated.get(27)%></a>
            <a href="<%=request.getContextPath()%>/movies/<%=movie.getId()%>/remove" class="btn btn-outline-danger"><%=translated.get(28)%></a>
        </div>
        <div class="d-flex flex-column">
            <h2 class="text-center"><%=movie.getTitle()%></h2>
            <h4 class="text-center"><%=movie.getTagline()%></h4>
        </div>
        <div class="d-flex flex-column"></div>
    </div>
    <br>
    <table class="table">
        <tbody>
        <tr>
            <td><%=translated.get(10)%></td>
            <td><%=movie.getOriginalTitle()%></td>
        </tr>
        <tr>
            <td><%=translated.get(11)%></td>
            <% Locale loc = new Locale(movie.getOriginalLanguage()); %>
            <td><%=loc.getDisplayLanguage(loc)%></td>
        </tr>
        <tr>
            <td><%=translated.get(12)%></td>
            <td>
                <% SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); %>
                <%=dateFormat.format(movie.getReleaseDate())%>
            </td>
        </tr>
        <tr>
            <td><%=translated.get(13)%></td>
            <td><%=movie.getStatus()%></td>
        </tr>
        <tr>
            <td><%=translated.get(14)%></td>
            <td><%=movie.getRuntime()%> <%=translated.get(29)%></td>
        </tr>
        <tr>
            <td><%=translated.get(15)%></td>
            <% Genre[] genres = movie.getGenres(); %>
            <td>
                <% for(int i = 0; i < genres.length; i++) {
                    Genre genre = genres[i]; %>
                <a href="<%=request.getContextPath()%>/genres/<%=genre.getId()%>"><%=genre.getName()%></a><% if(i < genres.length-1) %>,
                <% } %>
            </td>
        </tr>
        <tr>
            <td><%=translated.get(16)%></td>
            <% ProductionCompany[] companies = movie.getProductionCompanies(); %>
            <td>
                <% for(int i = 0; i < companies.length; i++) {
                    ProductionCompany company = companies[i]; %>
                <a href="<%=request.getContextPath()%>/companies/<%=company.getId()%>"><%=company.getName()%></a><% if(i < companies.length-1) %>,
                <% } %>
            </td>
        </tr>
        <tr>
            <td><%=translated.get(17)%></td>
            <% ProductionCountry[] countries = movie.getProductionCountries(); %>
            <td>
                <% for(int i = 0; i < countries.length; i++) {
                    ProductionCountry country = countries[i]; %>
                <%=country.getName()%><% if(i < countries.length-1) %>,
                <% } %>
            </td>
        </tr>
        <tr>
            <td><%=translated.get(18)%></td>
            <% SpokenLanguage[] languages = movie.getSpokenLanguages(); %>
            <td>
                <% for(int i = 0; i < languages.length; i++) {
                    SpokenLanguage language = languages[i]; %>
                <%=language.getName()%><% if(i < languages.length-1) %>,
                <% } %>
            </td>
        </tr>
        <tr>
            <td><%=translated.get(19)%></td>
            <td><%=movie.getBudget()%>$</td>
        </tr>
        <tr>
            <td><%=translated.get(20)%></td>
            <td><%=movie.getRevenue()%>$</td>
        </tr>
        <tr>
            <td><%=translated.get(21)%></td>
            <td><a href="<%=movie.getHomepage()%>"><%=movie.getHomepage()%></a></td>
        </tr>
        <tr>
            <td><%=translated.get(22)%></td>
            <td><%=String.format(Locale.US, "%.3f", movie.getPopularity())%></td>
        </tr>
        <tr>
            <td><%=translated.get(23)%></td>
            <td><%=movie.getVoteCount()%></td>
        </tr>
        </tbody>
    </table>
    <br>
    <div class="row align-items-center">
        <div class="col-4">
            <div class="progress" style="height: 10px">
                <% long rating = Math.round(movie.getVoteAverage()*10); %>
                <div class="progress-bar bg-success" role="progressbar" style="width: <%=rating%>%" aria-valuenow="<%=rating%>" aria-valuemin="0" aria-valuemax="100"></div>
                <div class="progress-bar bg-danger" role="progressbar" style="width: <%=100-rating%>%" aria-valuenow="<%=100-rating%>" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
        </div>
        <h2 <% if(rating >= 70 ) { %> class="text-success"
                <% } else if(rating >= 50) { %> class="text-secondary"
                <% } else %> class="text-danger"
        >
            <%=movie.getVoteAverage()%>
        </h2>
    </div>
    <br>
    <p><%=movie.getOverview()%></p>
    <br>
    <table class="table">
        <tbody>
        <tr>
            <td><%=translated.get(26)%></td>
            <% Keyword[] keywords = movie.getKeywords(); %>
            <td>
                <% for(int i = 0; i < keywords.length; i++) { %>
                <% Keyword keyword = keywords[i]; %>
                <a href="<%=request.getContextPath()%>/collections/<%=keyword.getId()%>"><%=keyword.getName()%></a><% if(i < keywords.length-1) %>,
                <% } %>
            </td>
        </tr>
        </tbody>
    </table>
    <% } %>
</div>
<%@include file="bootstrap-scripts.jsp"%>
</body>
</html>
