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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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

        request.setAttribute("activeNav", "register");
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");

        HttpSession session = request.getSession();

        if (fullName == null || fullName.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {

            session.setAttribute(AuthConstants.FLASH_LOGIN, "Name, email, and password are required.");
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        if (confirm == null || !confirm.equals(password)) {

            session.setAttribute(AuthConstants.FLASH_LOGIN, "Passwords do not match.");
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        if (userDAO.emailExists(email.trim())) {

            session.setAttribute(AuthConstants.FLASH_LOGIN, "An account with this email already exists.");
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        if (phone != null && !phone.isBlank() && userDAO.phoneExists(phone.trim())) {

            session.setAttribute(AuthConstants.FLASH_LOGIN, "An account with this phone already exists.");
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPhone(phone != null ? phone.trim() : "");
        user.setPassword(password);
        user.setGender(gender != null ? gender.trim() : "");
        user.setAddress(address != null ? address.trim() : "");

        if (!userDAO.registerUser(user)) {

            session.setAttribute(AuthConstants.FLASH_LOGIN, "Registration failed. Try again.");
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        User created = userDAO.getUserByEmail(user.getEmail());

        if (created != null) {

            session.setAttribute(AuthConstants.SESSION_USER_ID, created.getUserId());
            session.setAttribute(AuthConstants.SESSION_USER, created);
            session.setAttribute(AuthConstants.FLASH_LOGIN, "Welcome! Your account is ready.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        session.setAttribute(AuthConstants.FLASH_LOGIN, "Account created. Please sign in.");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
