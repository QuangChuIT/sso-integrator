<title>Login page</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<div class="container-fluid bg">
    <div class="row">
        <div class="col-md-5 col-sm-4 col-xs-12"></div>
        <div class="col-md-2 col-sm-4 col-xs-12">
            <form class="form-container" action="LoginServlet" method="post">
                <%
                    if (request.getAttribute("err") != null) {
                %>
                <div class="form-group">
                    <label style="color: red"><%=request.getAttribute("err")%>
                    </label>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <label>Username:</label>
                    <input class="form-control" type="text" name="user">
                </div>
                <div class="form-group">
                    <label>Password:</label>
                    <input class="form-control" type="password" name="pwd">
                </div>
                <button type="submit" class="btn btn-success">Login</button>
            </form>
        </div>
    </div>
</div>