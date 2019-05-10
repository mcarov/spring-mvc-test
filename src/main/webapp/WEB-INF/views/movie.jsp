<%@ page import="ru.itpark.domain.*" %>
<%@ page import="java.util.Locale" %>
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
        <% if(request.getAttribute("movie") != null){ %>
        <% Movie movie = (Movie)request.getAttribute("movie"); %>
            <h1 class="text-center"><%=movie.getTitle()%></h1>
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
                        <td><%=movie.getReleaseDate()%></td>
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
                            <a href="<%=request.getContextPath()%>/countries/<%=country.getIso_3166_1()%>"><%=country.getName()%></a>
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
                            <a href="<%=request.getContextPath()%>/spoken_languages/<%=language.getIso_639_1()%>"><%=language.getName()%></a>
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
                        <a href="<%=request.getContextPath()%>/tags/<%=tag.getId()%>"><%=tag.getName()%></a>
                        <% if(i < tags.length-1) %>,
                        <% } %>
                    </td>
                </tr>
                </tbody>
            </table>
        <% } %>
    </div>
</body>
</html>
