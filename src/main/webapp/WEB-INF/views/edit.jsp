<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.stream.Stream" %>
<%@ page import="ru.itpark.domain.*" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <%@include file="bootstrap-css.jsp"%>

    <title>Edit</title>
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
                            <% if(request.getAttribute("movie") != null) { %>
                                <li class="nav-item">
                                    <a class="nav-link" href="<%=request.getContextPath()%>/0/edit"><%=translated.get(5)%></a>
                                </li>
                            <% } else { %>
                                <li class="nav-item active">
                                    <a class="nav-link"><%=translated.get(5)%></a>
                                </li>
                            <% } %>
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
            <% Movie movie = new Movie();
                if(request.getAttribute("movie") != null)
                    movie = (Movie)request.getAttribute("movie");
            %>
            <form action="<%=request.getContextPath()%>/<%=movie.getId()%>/save" method="post">
                <div class="row">
                    <div class="col-6">
                        <div class="form-group">
                            <% Optional<String> title = Optional.ofNullable(movie.getTitle()); %>
                            <label for="title"><%=translated.get(8)%></label>
                            <input id="title" name="title" class="form-control" type="text" value="<%=title.orElse("")%>" required>
                        </div>
                        <div class="form-group">
                            <% Optional<String> tagLine = Optional.ofNullable(movie.getTagline()); %>
                            <label for="tagline"><%=translated.get(9)%></label>
                            <input id="tagline" name="tagline" class="form-control" type="text" value="<%=tagLine.orElse("")%>">
                        </div>
                        <div class="form-group">
                            <% Optional<String> originalTitle = Optional.ofNullable(movie.getOriginalTitle()); %>
                            <label for="original-title"><%=translated.get(10)%></label>
                            <input id="original-title" name="original-title" class="form-control" type="text" value="<%=originalTitle.orElse("")%>">
                        </div>
                        <div class="form-group">
                            <% String lan ="";
                                if(movie.getOriginalLanguage() != null) {
                                    Locale loc = new Locale(movie.getOriginalLanguage());
                                    lan = loc.getDisplayLanguage(loc);
                                }
                            %>
                            <label for="original-language"><%=translated.get(11)%></label>
                            <input id="original-language" name="original-language" class="form-control" type="text" value="<%=lan%>">
                        </div>
                        <div class="row">
                            <div class="col">
                                <div class="form-group">
                                    <% String date = "";
                                        if(movie.getReleaseDate() != null) {
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            date = dateFormat.format(movie.getReleaseDate());
                                        }
                                    %>
                                    <label for="release-date"><%=translated.get(12)%></label>
                                    <input id="release-date" name="release-date" class="form-control" type="text" value="<%=date%>">
                                </div>
                            </div>
                            <div class="col">
                                <div class="form-group">
                                    <% Optional<String> status = Optional.ofNullable(movie.getStatus()); %>
                                    <label for="status"><%=translated.get(13)%></label>
                                    <input id="status" name="status" class="form-control" type="text" value="<%=status.orElse("")%>">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <% int runtime = movie.getRuntime(); %>
                            <label for="runtime"><%=translated.get(14)%></label>
                            <input id="runtime" name="runtime" class="form-control" type="text" value="<%=(runtime == 0 ? "" : runtime)%>">
                        </div>
                        <div class="form-group">
                            <% String genreNames = "";
                                if(movie.getGenres() != null) {
                                    Genre[] genres = movie.getGenres();
                                    String[] namesArray = Arrays.stream(genres).map(Genre::getName).toArray(String[]::new);
                                    genreNames = String.join(", ", namesArray);
                                }
                            %>
                            <label for="genres"><%=translated.get(15)%></label>
                            <input id="genres" name="genres" class="form-control" type="text" value="<%=genreNames%>">
                        </div>
                        <div class="form-group">
                            <% String companyNames = "";
                                if(movie.getProductionCompanies() != null) {
                                    ProductionCompany[] companies = movie.getProductionCompanies();
                                    String[] namesArray = Arrays.stream(companies).map(ProductionCompany::getName).toArray(String[]::new);
                                    companyNames = String.join(", ", namesArray);
                                }
                            %>
                            <label for="companies"><%=translated.get(16)%></label>
                            <input id="companies" name="companies" class="form-control" type="text" value="<%=companyNames%>">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <% String countryNames = "";
                                if(movie.getProductionCompanies() != null) {
                                    ProductionCountry[] countries = movie.getProductionCountries();
                                    String[] namesArray = Arrays.stream(countries).map(ProductionCountry::getName).toArray(String[]::new);
                                    countryNames = String.join(", ", namesArray);
                                }
                            %>
                            <label for="countries"><%=translated.get(17)%></label>
                            <input id="countries" name="countries" class="form-control" type="text" value="<%=countryNames%>">
                        </div>
                        <div class="form-group">
                            <% String languageNames = "";
                                if(movie.getProductionCompanies() != null) {
                                    SpokenLanguage[] languages = movie.getSpokenLanguages();
                                    String[] namesArray = Arrays.stream(languages).map(SpokenLanguage::getName).toArray(String[]::new);
                                    languageNames = String.join(", ", namesArray);
                                }
                            %>
                            <label for="spoken-languages"><%=translated.get(18)%></label>
                            <input id="spoken-languages" name="spoken-languages" class="form-control" type="text" value="<%=languageNames%>">
                        </div>
                        <div class="row">
                            <div class="col">
                                <div class="form-group">
                                    <% long budget = movie.getBudget(); %>
                                    <label for="budget"><%=translated.get(19)%></label>
                                    <input id="budget" name="budget" class="form-control" type="text" value="<%=(budget == 0 ? "" : budget)%>">
                                </div>
                            </div>
                            <div class="col">
                                <div class="form-group">
                                    <% long revenue = movie.getRevenue(); %>
                                    <label for="revenue"><%=translated.get(20)%></label>
                                    <input id="revenue" name="revenue" class="form-control" type="text" value="<%=(revenue == 0 ? "" : revenue)%>">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <% Optional<String> homepage = Optional.ofNullable(movie.getHomePage()); %>
                            <label for="homepage"><%=translated.get(21)%></label>
                            <input id="homepage" name="homepage" class="form-control" type="text" value="<%=homepage.orElse("")%>">
                        </div>
                        <div class="row">
                            <div class="col">
                                <div class="form-group">
                                    <% double popularity = movie.getPopularity(); %>
                                    <label for="popularity"><%=translated.get(22)%></label>
                                    <input id="popularity" name="popularity" class="form-control" type="text" value="<%=(popularity == 0 ? "" : String.format("%.3f", popularity))%>">
                                </div>
                            </div>
                            <div class="col">
                                <div class="form-group">
                                    <% long votes = movie.getVoteCount(); %>
                                    <label for="votes"><%=translated.get(23)%></label>
                                    <input id="votes" name="votes" class="form-control" type="text" value="<%=(votes == 0 ? "" : votes)%>">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <% double rating = movie.getVoteAverage(); %>
                            <label for="rating"><%=translated.get(24)%></label>
                            <input id="rating" name="rating" class="form-control" type="text" value="<%=(rating == 0 ? "" : rating)%>">
                        </div>
                        <div class="form-group">
                            <% Optional<String> overview = Optional.ofNullable(movie.getOverview()); %>
                            <label for="overview"><%=translated.get(25)%></label>
                            <input id="overview" name="overview" class="form-control" type="text" value="<%=overview.orElse("")%>">
                        </div>
                        <div class="form-group">
                            <% String keywordNames = "";
                                if(movie.getProductionCompanies() != null) {
                                    Keyword[] keywords = movie.getKeywords();
                                    String[] namesArray = Arrays.stream(keywords).map(Keyword::getName).toArray(String[]::new);
                                    keywordNames = String.join(", ", namesArray);
                                }
                            %>
                            <label for="keywords"><%=translated.get(26)%></label>
                            <input id="keywords" name="keywords" class="form-control" type="text" value="<%=keywordNames%>">
                        </div>
                    </div>
                </div>
                <br>
                <div class="row justify-content-center">
                    <button class="btn btn-outline-success" type="submit"><%=translated.get(27)%></button>
                </div>
            </form>
        <% } %>
    </div>
    <%@include file="bootstrap-scripts.jsp"%>
</body>
</html>

