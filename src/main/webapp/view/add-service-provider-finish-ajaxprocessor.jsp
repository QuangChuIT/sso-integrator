<%@ page import="com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient" %>
<%@ page import="com.bsc.sso.authentication.model.OauthConsumerApp" %>
<%@ page import="javax.swing.*" %>

<link href="../css/app.css" rel="stylesheet" type="text/css">

<%
    String httpMethod = request.getMethod();
    if (!"post".equalsIgnoreCase(httpMethod)) {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return;
    }

    String spName = request.getParameter("spName");
    OauthConsumerApp app = null;
    ApplicationManagementServiceClient serviceClient = null;
    try {
        serviceClient = new ApplicationManagementServiceClient();
        app = serviceClient.getConsumerApp(spName);
    } catch (Exception e) {
        e.printStackTrace();
    }
    if (app != null) {
        JOptionPane.showMessageDialog(null, "The SP with name " + spName + " is already exists!");
%>
<script>
    location.href = "add-service-provider.jsp";
</script>
<%
} else {
    try {
        app = new OauthConsumerApp();
        app.setAppName(spName);
        app.setDescription(request.getParameter("spDescription"));
        app.setCallbackUrl(request.getParameter("spCallbackUrl"));
        app.setTokenExpireTime(Long.parseLong(request.getParameter("spTokenExpiryTime")));
        app.setRefreshTokenExpireTime(Long.parseLong(request.getParameter("spRefreshTokenExpiryTime")));
        serviceClient.registerApplicationData(app);

%>
<script>
    location.href = 'list-service-providers.jsp';
</script>
<%
} catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Error when register service provider!");
%>
<script>
    location.href = 'add-service-provider.jsp';
</script>
<%
        }
    }
%>




