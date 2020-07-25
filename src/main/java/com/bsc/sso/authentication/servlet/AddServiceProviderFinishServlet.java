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

@WebServlet("/AddServiceProviderFinish")
public class AddServiceProviderFinishServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            response.sendRedirect(request.getContextPath() + "/add-service");
        } else {
            try {
                app = new OauthConsumerApp();
                app.setAppName(request.getParameter("spName"));
                app.setDescription(request.getParameter("spDescription"));
                app.setCallbackUrl(request.getParameter("spCallbackUrl"));
                app.setTokenExpireTime(Long.parseLong(request.getParameter("spTokenExpiryTime")));
                app.setRefreshTokenExpireTime(Long.parseLong(request.getParameter("spRefreshTokenExpiryTime")));
                serviceClient.registerApplicationData(app);
                response.sendRedirect(request.getContextPath() + "/service-providers");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error when register service provider!");
                response.sendRedirect(request.getContextPath() + "/add-service");
            }
        }
    }
}
