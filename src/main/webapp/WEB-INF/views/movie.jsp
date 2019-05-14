<%@ page import="ru.itpark.domain.*" %>
<%@ page import="java.util.Locale" %>
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

    <title>Movie</title>
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
                            <label class="custom-file-label" for="csv-file">Choose csv file...</label>
                            <input type="file" class="custom-file-input" id="csv-file" name="csv-file" accept="text/csv" required>
                        </div>
                        <div class="input-group-append">
                            <button class="btn btn-outline-success" type="submit" name="import">Import</button>
                        </div>
                    </div>
                </form>
            </div>
        </nav>
        <br>
        <% if(request.getAttribute("movie") != null){ %>
        <% Movie movie = (Movie)request.getAttribute("movie"); %>
            <h2 class="text-center"><%=movie.getTitle()%></h2>
            <h4 class="text-center"><%=movie.getTagline()%></h4>
            <br>
            <table class="table">
                <tbody>
                    <tr>
                        <td>Original title</td>
                        <td><%=movie.getOriginalTitle()%></td>
                    </tr>
                    <tr>
                        <td>Original language</td>
                        <% Locale loc = new Locale(movie.getOriginalLanguage()); %>
                        <td><%=loc.getDisplayLanguage(loc)%></td>
                    </tr>
                    <tr>
                        <td>Release date</td>
                        <td>
                            <% SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); %>
                            <%=dateFormat.format(movie.getReleaseDate())%>
                        </td>
                    </tr>
                    <tr>
                        <td>Status</td>
                        <td><%=movie.getStatus()%></td>
                    </tr>
                    <tr>
                        <td>Runtime</td>
                        <td><%=movie.getRuntime()%> min.</td>
                    </tr>
                    <tr>
                        <td>Genres</td>
                        <% Genre[] genres = movie.getGenres(); %>
                        <td>
                            <% for(int i = 0; i < genres.length; i++) {
                                Genre genre = genres[i]; %>
                                <a href="<%=request.getContextPath()%>/genres/<%=genre.getId()%>"><%=genre.getName()%></a>
                                <% if(i < genres.length-1) %>,
                            <% } %>
                        </td>
                    </tr>
                    <tr>
                        <td>Production companies</td>
                        <% ProductionCompany[] companies = movie.getProductionCompanies(); %>
                        <td>
                            <% for(int i = 0; i < companies.length; i++) {
                                ProductionCompany company = companies[i]; %>
                            <a href="<%=request.getContextPath()%>/companies/<%=company.getId()%>"><%=company.getName()%></a>
                            <% if(i < companies.length-1) %>,
                            <% } %>
                        </td>
                    </tr>
                    <tr>
                        <td>Production countries</td>
                        <% ProductionCountry[] countries = movie.getProductionCountries(); %>
                        <td>
                            <% for(int i = 0; i < countries.length; i++) {
                                ProductionCountry country = countries[i]; %>
                            <%=country.getName()%>
                            <% if(i < countries.length-1) %>,
                            <% } %>
                        </td>
                    </tr>
                    <tr>
                        <td>Spoken languages</td>
                        <% SpokenLanguage[] languages = movie.getSpokenLanguages(); %>
                        <td>
                            <% for(int i = 0; i < languages.length; i++) {
                                SpokenLanguage language = languages[i]; %>
                            <%=language.getName()%>
                            <% if(i < languages.length-1) %>,
                            <% } %>
                        </td>
                    </tr>
                    <tr>
                        <td>Budget</td>
                        <td><%=movie.getBudget()%> $</td>
                    </tr>
                    <tr>
                        <td>Revenue</td>
                        <td><%=movie.getRevenue()%> $</td>
                    </tr>
                    <tr>
                        <td>Homepage</td>
                        <td><a href="<%=movie.getHomePage()%>"><%=movie.getHomePage()%></a></td>
                    </tr>
                    <tr>
                        <td>Popularity</td>
                        <td><%=String.format("%.3f", movie.getPopularity())%></td>
                    </tr>
                    <tr>
                        <td>Votes</td>
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
                    <td>Tags</td>
                    <% Keyword[] tags = movie.getKeywords(); %>
                    <td>
                        <% for(int i = 0; i < tags.length; i++) {
                            Keyword tag = tags[i]; %>
                        <a href="<%=request.getContextPath()%>/collections/<%=tag.getId()%>"><%=tag.getName()%></a>
                        <% if(i < tags.length-1) %>,
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
