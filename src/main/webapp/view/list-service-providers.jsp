<%@ page import="com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="com.bsc.sso.authentication.model.OauthConsumerApp" %>
<%@ page import="javax.swing.*" %>
<%@ page import="com.bsc.sso.authentication.model.OauthState" %>

<title>Service Provider</title>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap CRUD Data Table for Database with Modal Form</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.21/css/jquery.dataTables.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.min.js"></script>
    <link rel="stylesheet" href="/css/tables.css">
</head>

<body>
<div id="middle" class="container">

    <h2 style="font-family: initial">Service Providers</h2>
    <a href="/LogoutServlet" accesskey="1" title="">Logout</a>
    | <a href="/changing-password" accesskey="1" title="">Change Password</a>
    <div style="margin-top: 50px" class="table-responsive">
        <div class="table-wrapper">
            <div class="table-title">
                <div class="row">
                    <div class="col-xs-6">
                        <h2>Service Provider</h2>
                    </div>
                    <div class="col-xs-6">
                        <a href="/add-service" class="btn btn-success" data-toggle="modal"><i class="material-icons">&#xE147;</i> <span>Add Service Provider</span></a>
                    </div>
                </div>
            </div>
            <%
                OauthConsumerApp[] applications = null;
                try {
                    ApplicationManagementServiceClient serviceClient = new ApplicationManagementServiceClient();
                    applications = serviceClient.getAllApplicationBasicInfo();
                } catch (Exception e) {
                    String message = "Error while loading service providers" + " : " + e.getMessage();
                }

            %>
                    <table id="service-table" class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Description</th>
                                <th>Client Key</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <%
                            if (applications != null && applications.length > 0) {
                        %>
                        <tbody>
                        <%
                            for (OauthConsumerApp app : applications) {
                                if (app != null) {
                        %>
                        <tr>
                            <td><%=Encode.forHtml(app.getAppName())%>
                            </td>
                            <td><%=app.getDescription() != null ? Encode.forHtml(app.getDescription()) : ""%>
                            </td>
                            <td>
                                <input style="border: none; background: none; margin-top: 2px; width: 85%; font-size: 16px"
                                       type="password" autocomplete="off"
                                       id="spClientSecret"
                                       name="spClientSecret"
                                       value="<%=app.getConsumerKey() != null ? Encode.forHtml(app.getConsumerKey()) : ""%>"
                                       readonly="readonly">
                            </td>
                            <td>
                                <form style="float:left" action="/LoadServiceProviderServlet" method="get">
                                    <input type="hidden" name="spName" value="<%=Encode.forUriComponent(app.getAppName())%>">
                                    <a onclick='this.parentNode.submit(); return false;' class="edit" data-toggle="modal"><i class="material-icons" data-toggle="tooltip"
                                                                           title="Edit">&#xE254;</i></a>
                                </form>

                                <a style="float:left" onclick="removeItem('<%=Encode.forJavaScriptAttribute(app.getAppName())%>')"
                                   class="delete" data-toggle="modal"><i class="material-icons" data-toggle="tooltip"
                                                                         title="Delete">&#xE872;</i></a>
                            </td>
                        </tr>
                        <%
                                }
                            }
                        %>
                        </tbody>
                        <% }
                            assert applications != null;%>
                    </table>
        </div>
    </div>
</div>

<script type="text/javascript">
    function removeItem(appid) {
        if (confirm('Are you sure you want to delete SP ' + appid + '?')) {
            var appName = appid;
            $.ajax({
                type: 'POST',
                url: '/RemoveServiceProvider',
                headers: {
                    Accept: "text/html"
                },
                data: 'appid=' + appName,
                async: false,
                success: function (responseText, status) {
                    if (status == "success") {
                        location.assign("/service-providers");
                    }
                }
            });
        }
        return false;
    }
</script>
<script>
    $(document).ready( function () {
        $('#service-table').DataTable();
    } );
</script>
</body>
