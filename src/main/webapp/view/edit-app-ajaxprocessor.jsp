<%@ page import="com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient" %>
<%@ page import="com.bsc.sso.authentication.model.OauthConsumerApp" %>
<%@ page import="javax.swing.*" %>
<%@ page import="com.bsc.sso.authentication.model.OauthState" %>
<%@ page import="com.bsc.sso.authentication.client.constants.Constants" %>

<%
    String httpMethod = request.getMethod();
    if (!"post".equalsIgnoreCase(httpMethod)) {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return;
    }

    String consumerkey = request.getParameter("consumerkey");

    OauthConsumerApp app = null;
    String action = null;

    String spName = request.getParameter("spName");
    action = request.getParameter("action");

    ApplicationManagementServiceClient serviceClient = new ApplicationManagementServiceClient();
    app = serviceClient.getConsumerApp(spName);
    if (app == null) {
        JOptionPane.showMessageDialog(null, "The SP with name " + spName + " is not exists!");
        request.getRequestDispatcher("/view/load-service-provider.jsp?spName=" + spName).forward(request, response);
    } else {
        try {
            if (Constants.ACTION_REGENERATE.equalsIgnoreCase(action)) {
                String oauthAppState = serviceClient.getOauthApplicationState(consumerkey);
                serviceClient.regenerateAndRetrieveOauthSecretKey(spName, consumerkey);
                if (OauthState.INACTIVE.name().equalsIgnoreCase(oauthAppState)) {
                    serviceClient.updateOauthApplicationState(consumerkey, OauthState.ACTIVE.name());
                }
                JOptionPane.showMessageDialog(null, "Client Secret successfully updated for Client ID: " + consumerkey);
            } else if (Constants.ACTION_REVOKE.equalsIgnoreCase(action)) {
                String oauthAppState = serviceClient.getOauthApplicationState(consumerkey);
                if (OauthState.INACTIVE.name().equalsIgnoreCase(oauthAppState)) {
                    JOptionPane.showMessageDialog(null, "Application is already revoked.");
                } else {
                    serviceClient.updateOauthApplicationState(consumerkey, OauthState.INACTIVE.name());
                    JOptionPane.showMessageDialog(null, "Application successfully revoked.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error when update service provider!");
            response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
            return;
        } finally {
        }
    }

    if ((action != null) && ("revoke".equalsIgnoreCase(action) || "regenerate".equalsIgnoreCase(action))) {
        String returnString = "../view/load-service-provider.jsp?spName=" + spName;
        response.addHeader("redirectUrl", returnString);

    }
%>