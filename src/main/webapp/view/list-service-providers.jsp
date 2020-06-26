<%@ page import="com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="com.bsc.sso.authentication.model.OauthConsumerApp" %>

<script src="../js/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<div id="middle" class="container">
    <h2 style="margin-left: 10px; font-family: initial">Service Providers</h2>
    <a href="/LogoutServlet" accesskey="1" title="">Logout</a>
    | <a href="change-password.jsp" accesskey="1" title="">Change Password</a>
    <div id="workArea" style="margin-left: 30px;">
        <script type="text/javascript">
            function removeItem(appid) {
                if (confirm('Are you sure you want to delete SP ' + appid + '?')) {
                    var appName = appid;
                    $.ajax({
                        type: 'POST',
                        url: 'remove-service-provider-finish-ajaxprocessor.jsp',
                        headers: {
                            Accept: "text/html"
                        },
                        data: 'appid=' + appName,
                        async: false,
                        success: function (responseText, status) {
                            if (status == "success") {
                                location.assign("list-service-providers.jsp?");
                            }
                        }
                    });
                }
                return false;
            }
        </script>
        <%
            OauthConsumerApp[] applications = null;
            try {
                ApplicationManagementServiceClient serviceClient = new ApplicationManagementServiceClient();
                applications = serviceClient.getAllApplicationBasicInfo();
            } catch (Exception e) {
                String message = "Error while loading service providers" + " : " + e.getMessage();
            }
        %>
                    <table id="ServiceProviders" class="table table-striped">
                        <br/>
                        <thead>
                        <tr>
                            <th>Service Provider ID</th>
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
                            <td><%=app.getConsumerKey() != null ? Encode.forHtml(app.getConsumerKey()) : ""%>
                            </td>
                            <td>
                                <input type="button" value="Edit" title="Edit Service Providers"
                                       class="btn btn-default btn-sm"
                                       onclick="javascript:location.href='load-service-provider.jsp?spName=<%=Encode.forUriComponent(app.getAppName())%>'"/>
                                <input type="button" value="Delete" title="Remove Service Providers"
                                       class="btn btn-default btn-sm"
                                       onclick="removeItem('<%=Encode.forJavaScriptAttribute(app.getAppName())%>')"/>

                            </td>
                        </tr>
                        <%
                                }
                            }
                        %>
                        </tbody>
                        <% } %>
                    </table>
        <input type="button" value="Add Service Provider" class="btn btn-default btn-sm"
               onclick="javascript:location.href='add-service-provider.jsp'"/>
    </div>
</div>