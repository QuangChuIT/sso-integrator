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

@WebServlet("/LoadServiceProviderServlet")
public class LoadServiceProviderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OauthConsumerApp applications = null;
        ApplicationManagementServiceClient serviceClient = null;
        try {
            serviceClient = new ApplicationManagementServiceClient();
            applications = serviceClient.getConsumerApp(request.getParameter("spName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String name = request.getParameter("spName");
        if (applications != null || applications.getAppName() != null)
            request.getRequestDispatcher("/view/load-service-provider.jsp?spName=" + name).forward(request, response);
        if (applications == null || applications.getAppName() == null) {
            JOptionPane.showMessageDialog(null, "Load service provider fail!");
            response.sendRedirect(request.getContextPath() + "/service-providers");
            return;
        }
    }
}
