package com.bsc.sso.authentication.servlet;

import com.bsc.sso.authentication.dao.UserDao;
import com.bsc.sso.authentication.util.cryptWithMD5Util;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDao userDao = new UserDao();

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // get user
        HttpSession session = request.getSession(false);
        String user = session.getAttribute("user").toString();

        // get new password
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");

        String err = null;
        if(newPassword == null || newPassword.isEmpty()) {
            err = "New Password is required!";
        }
        else if(confirmNewPassword == null || confirmNewPassword.isEmpty()) {
            err = "Confirm New Password is required!";
        }
        else if (!newPassword.equals(confirmNewPassword)) {
            err = "Password and Confirm Password do not match!";
        }

        String newPass = null;
        try {
            newPass = cryptWithMD5Util.cryptWithMD5(newPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/changing-password");
        if (err != null) {
            request.setAttribute("err", err);
        } else {
            // change password success
            userDao.changePassword(user, newPass);
            request.setAttribute("msg", "Change Password success!");
        }

        rd.forward(request, response);
    }
}
