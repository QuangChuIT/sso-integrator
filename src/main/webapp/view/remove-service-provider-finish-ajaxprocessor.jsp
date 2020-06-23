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

    String appid = request.getParameter("appid");
    if (appid != null && !"".equals(appid)) {
        try {
            OauthConsumerApp applications = null;
            ApplicationManagementServiceClient serviceClient = new ApplicationManagementServiceClient();
            applications = serviceClient.getConsumerApp(appid);
            if (applications != null) {
                serviceClient.deleteApplication(applications);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error when delete service provider!");
        }
    }
%>

<script>
    location.href = 'list-service-providers.jsp';
</script>
