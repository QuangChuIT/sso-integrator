package com.bsc.sso.authentication.servlet;

import com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient;
import com.bsc.sso.authentication.model.OauthConsumerApp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;

@WebServlet("/RemoveServiceProvider")
public class RemoveServiceProviderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                response.sendRedirect(request.getContextPath() + "/service-providers");
            }
        }
    }
}
