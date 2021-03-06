<%@ page import="java.util.List" %>
<%@ page import="ru.itpark.domain.ProductionCompany" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <%@include file="bootstrap-css.jsp"%>

    <%! private int num = 0;
        private int lastNum = 0;
    %>
    <% if(request.getAttribute("page") != null && request.getAttribute("last") != null) {
        num = (Integer)request.getAttribute("page");
        lastNum = (Integer)request.getAttribute("last");
    } %>

    <title>Companies</title>
</head>
<body>
<div class="container">
    <% if(request.getAttribute("translation") != null && request.getAttribute("companies") != null) { %>
    <% List<String> translated = (List<String>)request.getAttribute("translation"); %>
    <% List<ProductionCompany> companies = (List<ProductionCompany>)request.getAttribute("companies"); %>
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
                <li class="nav-item active">
                    <a class="nav-link"><%=translated.get(4)%></a>
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
    <% if(companies.size() > 0) { %>
    <nav>
        <ul class="pagination justify-content-center">
            <% if(num == 1) { %>
            <li class="page-item disabled">
                <a class="page-link" href="#" aria-label="Begin">
                    <span aria-hidden="true">&laquo;&laquo;</span>
                </a>
            </li>
            <li class="page-item disabled">
                <a class="page-link" href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="page-item disabled"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/2">2</a></li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/3">3</a></li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num+1%>" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=lastNum%>" aria-label="End">
                    <span aria-hidden="true">&raquo;&raquo;</span>
                </a>
            </li>
            <% } else if(num == lastNum) { %>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/1" aria-label="Begin">
                    <span aria-hidden="true">&laquo;&laquo;</span>
                </a>
            </li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-1%>" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-2%>"><%=num-2%></a></li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-1%>"><%=num-1%></a></li>
            <li class="page-item disabled"><a class="page-link" href="#"><%=num%></a></li>
            <li class="page-item disabled">
                <a class="page-link" href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
            <li class="page-item disabled">
                <a class="page-link" href="#" aria-label="End">
                    <span aria-hidden="true">&raquo;&raquo;</span>
                </a>
            </li>
            <% } else { %>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/1" aria-label="Begin">
                    <span aria-hidden="true">&laquo;&laquo;</span>
                </a>
            </li>
            <li class="page-item ">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-1%>" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-1%>"><%=num-1%></a></li>
            <li class="page-item disabled"><a class="page-link" href="#"><%=num%></a></li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num+1%>"><%=num+1%></a></li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num+1%>" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=lastNum%>" aria-label="End">
                    <span aria-hidden="true">&raquo;&raquo;</span>
                </a>
            </li>
            <% } %>
        </ul>
    </nav>
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col"></th>
            <th scope="col"><%=translated.get(8)%></th>
        </tr>
        </thead>
        <tbody>
        <% for(int i = 0; i < companies.size(); i++) { %>
        <tr>
            <th scope="row"><%=(num-1)*50+(i+1)%></th>
            <td><a href="<%=request.getContextPath()%>/companies/<%=companies.get(i).getId()%>"><%=companies.get(i).getName()%></a></td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <nav>
        <ul class="pagination justify-content-center">
            <% if(num == 1) { %>
            <li class="page-item disabled">
                <a class="page-link" href="#" aria-label="Begin">
                    <span aria-hidden="true">&laquo;&laquo;</span>
                </a>
            </li>
            <li class="page-item disabled">
                <a class="page-link" href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="page-item disabled"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/2">2</a></li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/3">3</a></li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num+1%>" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=lastNum%>" aria-label="End">
                    <span aria-hidden="true">&raquo;&raquo;</span>
                </a>
            </li>
            <% } else if(num == lastNum) { %>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/1" aria-label="Begin">
                    <span aria-hidden="true">&laquo;&laquo;</span>
                </a>
            </li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-1%>" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-2%>"><%=num-2%></a></li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-1%>"><%=num-1%></a></li>
            <li class="page-item disabled"><a class="page-link" href="#"><%=num%></a></li>
            <li class="page-item disabled">
                <a class="page-link" href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
            <li class="page-item disabled">
                <a class="page-link" href="#" aria-label="End">
                    <span aria-hidden="true">&raquo;&raquo;</span>
                </a>
            </li>
            <% } else { %>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/1" aria-label="Begin">
                    <span aria-hidden="true">&laquo;&laquo;</span>
                </a>
            </li>
            <li class="page-item ">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-1%>" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num-1%>"><%=num-1%></a></li>
            <li class="page-item disabled"><a class="page-link" href="#"><%=num%></a></li>
            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num+1%>"><%=num+1%></a></li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=num+1%>" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
            <li class="page-item">
                <a class="page-link" href="<%=request.getContextPath()%>/companies/page/<%=lastNum%>" aria-label="End">
                    <span aria-hidden="true">&raquo;&raquo;</span>
                </a>
            </li>
            <% } %>
        </ul>
    </nav>
    <% } %>
    <% } %>
</div>
<%@include file="bootstrap-scripts.jsp"%>
</body>
</html>
