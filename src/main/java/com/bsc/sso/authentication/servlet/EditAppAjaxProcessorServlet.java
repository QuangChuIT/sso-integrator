package com.bsc.sso.authentication.servlet;

import com.bsc.sso.authentication.client.application.ApplicationManagementServiceClient;
import com.bsc.sso.authentication.client.constants.Constants;
import com.bsc.sso.authentication.model.OauthConsumerApp;
import com.bsc.sso.authentication.model.OauthState;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;

@WebServlet("/EditAppAjaxProcessor")
public class EditAppAjaxProcessorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        ApplicationManagementServiceClient serviceClient = null;
        try {
            serviceClient = new ApplicationManagementServiceClient();
            app = serviceClient.getConsumerApp(spName);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                } else if (Constants.ACTION_REVOKE.equalsIgnoreCase(action)) {
                    String oauthAppState = serviceClient.getOauthApplicationState(consumerkey);
                    if (OauthState.INACTIVE.name().equalsIgnoreCase(oauthAppState)) {
                    } else {
                        serviceClient.updateOauthApplicationState(consumerkey, OauthState.INACTIVE.name());
                    }
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
                return;
            }
        }

        if ((action != null) && ("revoke".equalsIgnoreCase(action) || "regenerate".equalsIgnoreCase(action))) {
            String returnString = "../view/load-service-provider.jsp?spName=" + spName;
            response.addHeader("redirectUrl", returnString);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
