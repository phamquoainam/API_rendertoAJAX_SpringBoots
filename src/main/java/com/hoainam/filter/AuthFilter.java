package com.hoainam.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.hoainam.entity.User;

@Component
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        // 1. Cho phép các tài nguyên tĩnh và trang Auth đi qua
        if (uri.startsWith("/auth")
                || uri.startsWith("/css")
                || uri.startsWith("/js")
                || uri.startsWith("/images")
                || uri.startsWith("/assets")
                || uri.startsWith("/uploads")) {

            chain.doFilter(request, response);
            return;
        }

        // 2. Kiểm tra các đường dẫn bắt đầu bằng /admin
        if (uri.startsWith("/admin")) {
            HttpSession session = req.getSession(false);
            User currentUser = (session == null) ? null : (User) session.getAttribute("currentUser");

            // A. Chưa đăng nhập -> Đá về trang Login
            if (currentUser == null) {
                res.sendRedirect(req.getContextPath() + "/auth/login?error=true");
                return;
            }

            // B. Đã đăng nhập nhưng KHÔNG PHẢI ADMIN -> Đá về trang Home hoặc báo lỗi
            // (ĐÂY LÀ ĐOẠN BẠN BỊ THIẾU)
            if (Boolean.FALSE.equals(currentUser.getAdmin())) {
                // Chuyển hướng về trang chủ User (hoặc trang 403 Access Denied tùy ý)
                res.sendRedirect(req.getContextPath() + "/home"); 
                return;
            }
        }

        chain.doFilter(request, response);
    }
}