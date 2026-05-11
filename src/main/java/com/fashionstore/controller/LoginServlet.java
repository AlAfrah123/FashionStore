package com.fashionstore.controller;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;
import com.fashionstore.util.AuthConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Object flash = session.getAttribute(AuthConstants.FLASH_LOGIN);

        if (flash instanceof String s && !s.isBlank()) {
            request.setAttribute("flashMessage", s);
            session.removeAttribute(AuthConstants.FLASH_LOGIN);
        }

        if (session.getAttribute(AuthConstants.SESSION_USER_ID) != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.setAttribute("activeNav", "login");
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String returnUrl = request.getParameter("returnUrl");

        if (email == null || email.isBlank() || password == null) {

            flash(request.getSession(), "Email and password are required.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = userDAO.loginUser(email.trim(), password);

        if (user == null) {

            flash(request.getSession(), "Invalid email or password.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute(AuthConstants.SESSION_USER_ID, user.getUserId());
        session.setAttribute(AuthConstants.SESSION_USER, user);

        String safeReturn = sanitizeReturnUrl(request, returnUrl);

        response.sendRedirect(safeReturn != null ? safeReturn : request.getContextPath() + "/home");
    }

    private static void flash(HttpSession session, String msg) {
        session.setAttribute(AuthConstants.FLASH_LOGIN, msg);
    }

    private static String sanitizeReturnUrl(HttpServletRequest request, String returnUrl) {

        if (returnUrl == null || returnUrl.isBlank()) {
            return null;
        }

        String ctx = request.getContextPath();

        if (!returnUrl.startsWith(ctx + "/") && !returnUrl.equals(ctx)) {
            return null;
        }

        if (returnUrl.contains("//") || returnUrl.toLowerCase().contains("://")) {
            return null;
        }

        return returnUrl;
    }
}
