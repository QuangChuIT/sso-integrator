package com.bsc.sso.authentication.servlet;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/authentication/login")
public class LoginContextServlet extends HttpServlet {

//    private static final Log log = LogFactory.getLog(LoginContextServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        log.info("Login Context Servlet doGet got Hit !");
        try {
            getRSAKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.setContentType("text/plain");
        resp.getWriter().write("You are inside the servlet 1!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        log.info("Login Context Servlet doPost got Hit !");

        resp.setContentType("text/plain");
        resp.getWriter().write("You are inside the servlet !");
    }

    public Map<String, Object> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Map<String, Object> keys = new HashMap<>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
    }
}
