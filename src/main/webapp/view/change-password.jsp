<title>Change Password</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!--===============================================================================================-->
<link rel="icon" type="image/png" href="/images/icons/key.ico"/>
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="/vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="/vendor/animate/animate.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="/vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="/vendor/select2/select2.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="/css/util.css">
<link rel="stylesheet" type="text/css" href="/css/main.css">
<!--===============================================================================================-->

<div class="limiter">
    <div class="container-login100">
        <div class="wrap-login100" style="padding-top: 100px; padding-bottom: 100px">
            <div class="login100-pic js-tilt">
                <img src="/images/img-02.png" alt="IMG">
            </div>

            <form class="form-conatiner" action="/ChangePasswordServlet" method="post">
                <%
                    if(request.getAttribute("err") != null) {
                %>
                <div class="form-group">
                    <label style="color: red"><%=request.getAttribute("err")%></label>
                </div>
                <%
                } else if(request.getAttribute("msg") != null) {
                %>
                <div class="form-group">
                    <label style="color: green"><%=request.getAttribute("msg")%></label>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <label>New Password:</label>
                    <input class="form-control" type="password" name="newPassword">
                </div>
                <div class="form-group">
                    <label>Confirm New Password:</label>
                    <input class="form-control" type="password" name="confirmNewPassword">
                </div>
                <div>
                    <button type="submit" style="margin-bottom:15px" class="btn btn-success">Change Password</button>
                    <p>
                        <a style="color: black" href="/service-providers"><u>Back</u></a>
                    </p>
                </div>
            </form>
        </div>
    </div>
</div>

<!--===============================================================================================-->
<script src="/vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
<script src="/vendor/bootstrap/js/popper.js"></script>
<script src="/vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
<script src="/vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
<script src="/vendor/tilt/tilt.jquery.min.js"></script>
<!--===============================================================================================-->
<script src="/js/main.js"></script>