package com.bsc.sso.authentication.servlet;

import com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;

@WebServlet("/ConfigureServiceProvider")
public class ConfigureServiceProviderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String httpMethod = request.getMethod();
        if (!"post".equalsIgnoreCase(httpMethod)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        String spName = request.getParameter("spName");
        String oldSPName = request.getParameter("oldSPName");
        OauthConsumerApp app = null;
        ApplicationManagementServiceClient serviceClient = null;
        try {
            serviceClient = new ApplicationManagementServiceClient();
            app = serviceClient.getConsumerApp(spName);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        response.sendRedirect(request.getContextPath() + "/service-providers");
    }
}
