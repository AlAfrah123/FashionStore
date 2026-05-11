package com.fashionstore.filter;

import com.fashionstore.util.AuthConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/profile"})
public class AuthFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(AuthConstants.SESSION_USER_ID) == null) {

            String ctx = request.getContextPath();
            String target = ctx + "/login?returnUrl=" + java.net.URLEncoder.encode(ctx + "/profile", java.nio.charset.StandardCharsets.UTF_8);

            response.sendRedirect(target);
            return;
        }

        chain.doFilter(request, response);
    }
}
