<%@ page import="com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="com.bsc.sso.authentication.model.OauthConsumerApp" %>

<script src="../js/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<div id="middle">
    <h2 style="margin-left: 10px; font-family: initial">Service Providers</h2>
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
        <table style="width: 70%">
            <tbody>
            <tr>
                <td style="border:none !important">
                    <table width="100%" id="ServiceProviders" class="table table-bordered">
                        <br/>
                        <thead>
                        <tr style="white-space: nowrap">
                            <th style="width: 20%;font-family: initial">Service Provider ID</th>
                            <th style="width: 30%;font-family: initial">Description</th>
                            <th style="width: 35%;font-family: initial">Client Key</th>
                            <th style="width: 15%;font-family: initial">Actions</th>
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
                            <td style="font-family: initial;vertical-align: middle"><%=Encode.forHtml(app.getAppName())%>
                            </td>
                            <td style="font-family: initial;vertical-align: middle"><%=app.getDescription() != null ? Encode.forHtml(app.getDescription()) : ""%>
                            </td>
                            <td style="font-family: initial;vertical-align: middle"><%=app.getConsumerKey() != null ? Encode.forHtml(app.getConsumerKey()) : ""%>
                            </td>
                            <td style="width: 100px; white-space: nowrap; font-family: initial">
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
                </td>
            </tr>
            </tbody>
        </table>
        <input type="button" value="Add Service Provider" class="btn btn-default btn-sm"
               onclick="javascript:location.href='add-service-provider.jsp'"/>
    </div>
</div>