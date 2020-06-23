<%@ page import="com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient" %>
<%@ page import="com.bsc.sso.authentication.model.OauthConsumerApp" %>
<%@ page import="com.bsc.sso.authentication.model.OauthState" %>
<%@ page import="javax.swing.*" %>

<link href="../css/app.css" rel="stylesheet" type="text/css">
<%
    String httpMethod = request.getMethod();
    if (!"post".equalsIgnoreCase(httpMethod)) {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return;
    }

    String spName = request.getParameter("spName");
    String oldSPName = request.getParameter("oldSPName");
    OauthConsumerApp app = null;
    ApplicationManagementServiceClient serviceClient = new ApplicationManagementServiceClient();
    app = serviceClient.getConsumerApp(spName);
    if (!spName.equals(oldSPName) && app != null) {
        JOptionPane.showMessageDialog(null, "The SP with name " + spName + " is already exists!");
        request.getRequestDispatcher("/view/load-service-provider.jsp?spName=" + oldSPName).forward(request, response);
    } else {
        try {
            app.setAppName(spName);
            app.setAppState(OauthState.valueOf(request.getParameter("spAppState")));
            app.setConsumerKey(request.getParameter("spClientKey"));
            app.setConsumerSecret(request.getParameter("spClientSecret"));
            app.setDescription(request.getParameter("spDescription"));
            app.setCallbackUrl(request.getParameter("spCallbackUrl"));
            app.setTokenExpireTime(Long.parseLong(request.getParameter("spTokenExpiryTime")));
            app.setRefreshTokenExpireTime(Long.parseLong(request.getParameter("spRefreshTokenExpiryTime")));
            serviceClient.updateApplicationData(app);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error when update service provider!");
            response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
            return;
        } finally {
        }
    }
%>

<script>
    location.href = "list-service-providers.jsp";
</script>
