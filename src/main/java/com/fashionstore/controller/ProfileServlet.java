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

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute(AuthConstants.SESSION_USER_ID);

        User user = userDAO.getUserById(userId);

        if (user == null) {

            session.removeAttribute(AuthConstants.SESSION_USER_ID);
            session.removeAttribute(AuthConstants.SESSION_USER);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        session.setAttribute(AuthConstants.SESSION_USER, user);

        Object flash = session.getAttribute("profileFlash");

        if (flash instanceof String s && !s.isBlank()) {
            request.setAttribute("flashMessage", s);
            session.removeAttribute("profileFlash");
        }

        request.setAttribute("profileUser", user);
        request.setAttribute("activeNav", "profile");

        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute(AuthConstants.SESSION_USER_ID);

        if (userId == null) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User existing = userDAO.getUserById(userId);

        if (existing == null) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        existing.setFullName(fullName != null ? fullName.trim() : "");
        existing.setPhone(phone != null ? phone.trim() : "");
        existing.setGender(gender != null ? gender.trim() : "");
        existing.setAddress(address != null ? address.trim() : "");

        if (!userDAO.updateUser(existing)) {

            session.setAttribute("profileFlash", "Could not update profile.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        if (newPassword != null && !newPassword.isBlank()) {

            if (!newPassword.equals(confirmPassword)) {

                session.setAttribute("profileFlash", "New passwords do not match.");
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }

            if (!userDAO.updatePassword(userId, newPassword)) {

                session.setAttribute("profileFlash", "Profile saved, but password change failed.");
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }
        }

        User refreshed = userDAO.getUserById(userId);

        if (refreshed != null) {
            session.setAttribute(AuthConstants.SESSION_USER, refreshed);
        }

        session.setAttribute("profileFlash", "Profile updated successfully.");
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
