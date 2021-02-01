package com.bsc.sso.authentication.servlet;

import com.bsc.sso.authentication.dao.UserDao;
import com.bsc.sso.authentication.model.User;
import com.bsc.sso.authentication.util.cryptWithMD5Util;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static MessageDigest md;

    private final UserDao userDao = new UserDao();

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // get request parameters for userID and password
        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");
        //verify username password
        User authUser = userDao.getByUsername(user);
        String pass = null;
        try {
            pass = cryptWithMD5Util.cryptWithMD5(pwd);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(authUser != null && authUser.getPassword().equals(pass)){
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect( "/service-providers");
        } else {
            request.setAttribute("err", "Either user name or password is wrong.");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
            rd.forward(request, response);
        }
    }
}