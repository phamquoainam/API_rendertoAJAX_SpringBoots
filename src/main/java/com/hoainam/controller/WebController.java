package com.hoainam.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hoainam.models.Info; 
import jakarta.servlet.http.HttpSession;
import com.hoainam.entity.User;

@Controller
public class WebController {

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        List<Info> profile = new ArrayList<>();
        profile.add(new Info("Họ và tên", currentUser.getFullname()));
        profile.add(new Info("Email", currentUser.getEmail()));
        profile.add(new Info("Số điện thoại", currentUser.getPhone()));
        profile.add(new Info("Quyền hạn", currentUser.getAdmin() ? "Quản trị viên" : "Người dùng"));
        
        model.addAttribute("profile", profile);

        return "user/profile"; 
    }
}