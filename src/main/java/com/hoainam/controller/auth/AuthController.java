package com.hoainam.controller.auth;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.hoainam.entity.User;
import com.hoainam.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    // LOGIN
    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        if ("true".equals(error)) {
            model.addAttribute("alert", "Vui lòng đăng nhập để tiếp tục");
        }
        return "/auth/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
            @RequestParam String password,
            @RequestParam(value = "remember", required = false) String remember,
            HttpSession session,
            HttpServletResponse response,
            Model model) {

        boolean isRemember = "on".equals(remember);

        if (username.isBlank() || password.isBlank()) {
            model.addAttribute("alert", "username or password must be not empty!");
            model.addAttribute("username", username);
            return "/auth/login";
        }

        User user = userService.findById(username);

        if (user == null || !password.equals(user.getPassword())) {
            model.addAttribute("alert", "username or password is not correct!");
            model.addAttribute("username", username);
            return "/auth/login";
        }

        if (user.getActive().equals(false)) {
            model.addAttribute("alert", "Tài khoản của bạn đã bị khoá");
            model.addAttribute("username", username);
            return "/auth/login";
        }
        session.setAttribute("currentUser", user);

        if (isRemember) {
            Cookie cookie = new Cookie("SESSION_USERNAME", user.getUsername());
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60);
            response.addCookie(cookie);
        }

        if (Boolean.TRUE.equals(user.getAdmin())) {
            return "redirect:/admin";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/logout")
    public String logout(
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response) {

        session.invalidate();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("SESSION_USERNAME".equals(c.getName())) {
                    c.setValue("");
                    c.setPath("/");
                    c.setMaxAge(0);
                    response.addCookie(c);
                }
            }
        }

        return "redirect:/auth/login";
    }

    // REGISTER
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("newUser", new User());
        return "/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("newUser") User user,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        // Check rỗng
        if (user.getUsername().isBlank() ||
                user.getPassword().isBlank() ||
                confirmPassword.isBlank() ||
                user.getEmail().isBlank()) {

            model.addAttribute("alert", "Vui lòng nhập đầy đủ thông tin");
            return "/auth/register";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("alert", "Mật khẩu nhập lại không khớp");
            return "/auth/register";
        }

        if (userService.checkExistUsername(user.getUsername())) {
            model.addAttribute("alert", "Tên đăng nhập đã tồn tại");
            return "/auth/register";
        }

        if (userService.checkExistEmail(user.getEmail())) {
            model.addAttribute("alert", "Email đã được sử dụng");
            return "/auth/register";
        }

        user.setAdmin(false);
        user.setActive(true);

        userService.create(user);

        model.addAttribute("success", "Đăng ký thành công. Vui lòng đăng nhập.");
        return "/auth/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPage() {
        return "/auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgot(@RequestParam String email,
            Model model, HttpSession session) {

        if (email.isBlank()) {
            model.addAttribute("alert", "Vui lòng nhập email");
            return "/auth/forgot-password";
        }

        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("alert", "Email không tồn tại trong hệ thống");
            return "/auth/forgot-password";
        }

        session.setAttribute("resetEmail", email);
        return "redirect:/auth/reset-password";
    }

    // RESET - PASSWORD

    @GetMapping("/reset-password")
    public String getResetPassword(Model model, HttpSession session) {
        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            model.addAttribute("alert", "Vui lòng nhập email để đặt lại mật khẩu");
            return "/auth/forgot-password";
        }

        return "/auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam String password,
            @RequestParam String confirmPassword,
            HttpSession session,
            Model model) {

        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            model.addAttribute("alert", "Phiên đặt lại mật khẩu đã hết hạn. Vui lòng thử lại.");
            return "/auth/forgot-password";
        }

        if (password == null || password.isBlank()
                || confirmPassword == null || confirmPassword.isBlank()) {
            model.addAttribute("alert", "Vui lòng nhập đầy đủ mật khẩu");
            return "/auth/reset-password";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("alert", "Mật khẩu nhập lại không khớp");
            return "/auth/reset-password";
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("alert", "Email không tồn tại trong hệ thống");
            return "/auth/forgot-password";
        }

        user.setPassword(password);
        userService.update(user);

        session.removeAttribute("resetEmail");

        model.addAttribute("alert", "Đặt lại mật khẩu thành công. Bạn có thể đăng nhập với mật khẩu mới.");
        return "/auth/login";
    }

}
