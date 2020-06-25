<%@ page import="com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient" %>
<%@ page import="com.bsc.sso.authentication.model.OauthConsumerApp" %>
<%@ page import="javax.swing.*" %>
<%@ page import="com.bsc.sso.authentication.model.OauthState" %>
<%@ page import="org.owasp.encoder.Encode" %>

<script src="../js/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<link href="../css/app.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
    function updateAppOnclick() {
        var spName = document.getElementById("spName").value;
        var callbackUrl = document.getElementById("spCallbackUrl").value;
        var tokenExpiryTime = document.getElementById("spTokenExpiryTime").value;
        var tokenRefreshExpiryTime = document.getElementById("spRefreshTokenExpiryTime").value;
        if (spName === '' || callbackUrl === '' || tokenExpiryTime === '' || tokenRefreshExpiryTime === '') {
            alert("Field is required!");
        } else {
            document.getElementById("configure-sp-form").submit();
            return true;
        }
        return false;
    }

    function updateBeanAndPostTo(postURL, data) {
        $.ajax({
            type: 'POST',
            url: postURL,
            data: data,
            success: function (data, textStatus, request) {
                window.location = request.getResponseHeader('redirectUrl');
            }

        });
    }

    function showHidePassword(element, inputId) {
        if ($(element).text() == 'Show') {
            document.getElementById(inputId).type = 'text';
            $(element).text('Hide');
        } else {
            document.getElementById(inputId).type = 'password';
            $(element).text('Show');
        }
    }

</script>

<%
    OauthConsumerApp applications = null;
    ApplicationManagementServiceClient serviceClient = new ApplicationManagementServiceClient();
    applications = serviceClient.getConsumerApp(request.getParameter("spName"));
    if (applications == null || applications.getAppName() == null) {
        JOptionPane.showMessageDialog(null, "Load service provider fail!");
%>
<script>
    location.href = "list-service-providers.jsp";
</script>
<%
        return;
    }
%>

<div id="middle">
    <h2 style="margin-left: 10px; font-family: initial">Service Providers</h2>
    <a href="/LogoutServlet" accesskey="1" title="">Logout</a>
    | <a href="change-password.html" accesskey="1" title="">Change Password</a>
    <div id="workArea" style="margin-left: 30px">
        <form id="configure-sp-form" method="post" name="configure-sp-form" method="post"
              action="configure-service-provider-finish-ajaxprocessor.jsp">
            <input type="hidden" name="oldSPName" id="oldSPName" value="<%=applications.getAppName()%>"/>
            <input type="hidden" name="spClientKey" id="spClientKey" value="<%=applications.getConsumerKey()%>"/>
            <div>
                <table style="width: 70%">
                    <tr>
                        <td style="width:35%;font-family: initial;padding: 10px">Service Provider Name:<span
                                class="required">*</span></td>
                        <td>
                            <input style="width:100%;height: 30px;font-family: initial;font-size: medium" id="spName"
                                   name="spName" type="text"
                                   value="<%=applications.getAppName()%>" class="form-control"
                                   white-list-patterns="^[a-zA-Z0-9\s.+_-]*$" autofocus/>
                        </td>
                    </tr>

                    <tr>
                        <td style="width:35%;font-family: initial;padding: 10px">App state:</td>
                        <td>
                            <input style="width:100%;border:none;font-family: initial" class="form-control"
                                   id="spAppState" name="spAppState" type="text" readonly
                                   value="<%=applications.getAppState() %>"/>
                        </td>
                    </tr>

                    <tr>
                        <td style="width:35%;font-family: initial;padding: 10px">Description:</td>
                        <td>
                            <input style="width:100%;font-family: initial;font-size: medium" class="form-control"
                                   id="spDescription" name="spDescription" type="text"
                                   value="<%=applications.getDescription() %>"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:35%;font-family: initial;padding: 10px">Callback Url:<span
                                class="required">*</span></td>
                        <td>
                            <input style="width:100%;font-family: initial;font-size: medium" class="form-control"
                                   id="spCallbackUrl" name="spCallbackUrl" type="text"
                                   value="<%=applications.getCallbackUrl() != null ? applications.getCallbackUrl() : "" %>"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:35%;font-family: initial;padding: 10px">Token Expiry Time:<span
                                class="required">*</span></td>
                        <td>
                            <input style="width:100%;font-family: initial;font-size: medium" class="form-control"
                                   id="spTokenExpiryTime" name="spTokenExpiryTime" type="text"
                                   value="<%=applications.getTokenExpireTime() %>"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:35%;font-family: initial;padding: 10px">Refresh Token Expiry Time:<span
                                class="required">*</span></td>
                        <td>
                            <input style="width:100%;font-family: initial;font-size: medium" class="form-control"
                                   id="spRefreshTokenExpiryTime" name="spRefreshTokenExpiryTime" type="text"
                                   value="<%=applications.getRefreshTokenExpireTime() %>"/>
                        </td>
                    </tr>
                </table>
            </div>
            <div style="margin-left: 10px">
                <br>
                <table id="samlTable" style="width: 70%" class="table table-bordered">
                    <tr style="white-space: nowrap">
                        <th style="width: 34%;font-family: initial">OAuth Client Key</th>
                        <th style="width: 41%;font-family: initial">OAuth Client Secret</th>
                        <th style="width: 25%;font-family: initial">Actions</th>
                    </tr>
                    <tr>
                        <td style="font-family: initial;vertical-align: middle"><%=applications.getConsumerKey()%>
                        </td>
                        <td style="font-family: initial;vertical-align: middle">
                            <div>
                                <input style="border: none; background: white; margin-top: 2px; width: 85%; font-size: 16px"
                                       type="password" autocomplete="off"
                                       id="spClientSecret"
                                       name="spClientSecret"
                                       value="<%=applications.getConsumerSecret()%>"
                                       readonly="readonly">
                                <span style="float: right;">
                                    <a style="margin-top: 5px;" class="showHideBtn"
                                       onclick="showHidePassword(this, 'spClientSecret')">Show</a>
                                </span>
                            </div>
                        </td>
                        <td style="font-family: initial;vertical-align: middle">
                            <div>
                                <a title="Revoke Service Providers"
                                   onclick="updateBeanAndPostTo('../view/edit-app-ajaxprocessor.jsp','spName=<%=Encode.forUriComponent(applications.getAppName())%>&consumerkey=<%=Encode.forUriComponent(applications.getConsumerKey())%>&action=revoke');"
                                   class="icon-link"
                                   style="background-image: url(../images/disabled.png)">Revoke</a>
                                <a title="Regenerate Secret Key"
                                   onclick="updateBeanAndPostTo('../view/edit-app-ajaxprocessor.jsp','spName=<%=Encode.forUriComponent(applications.getAppName())%>&consumerkey=<%=Encode.forUriComponent(applications.getConsumerKey())%>&action=regenerate');"
                                   class="icon-link"
                                   style="background-image: url(../images/enabled.png)">Regenerate Secret</a>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>
    <!-- sectionSub Div -->
    <div style="margin-left: 40px">
        <input type="button" class="btn btn-default btn-sm" value="Update" onclick="updateAppOnclick();"/>
        <input type="button" class="btn btn-default btn-sm" value="Cancel"
               onclick="javascript:location.href='list-service-providers.jsp'"/>
    </div>
</div>