package com.hoainam.controller.admin;

import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hoainam.entity.User;
import com.hoainam.service.UploadService;
import com.hoainam.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UploadService uploadService;

    @GetMapping("")
    public String index(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users/list";
    }

    @GetMapping("/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/users/create";
    }

    @PostMapping("/create")
    public String createUserPage(@ModelAttribute("newUser") User user,
            @RequestParam("avatarFile") MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                // Lưu file vào thư mục /resources/images/avatars
                String fileName = uploadService.handleSaveUploadFile(file, "avatars");
                user.setImages(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        userService.create(user);
        return "redirect:/admin/users";
    }

    @RequestMapping("/update/{username}")
    public String getUpdateUserPage(Model model, @PathVariable String username) {
        User currentUser = this.userService.findById(username);
        model.addAttribute("newUser", currentUser);
        return "admin/users/update";
    }

    @PostMapping("/update")
    public String updateUser(Model model,
            @ModelAttribute("newUser") User user,
            @RequestParam("avatarFile") MultipartFile file) {

        User currentUser = this.userService.findById(user.getUsername());
        if (currentUser != null) {
            currentUser.setPhone(user.getPhone());
            currentUser.setEmail(user.getEmail());
            currentUser.setFullname(user.getFullname());
            currentUser.setActive(user.getActive());
            currentUser.setAdmin(user.getAdmin());

            try {
                if (file != null && !file.isEmpty()) {
                    String fileName = uploadService.handleSaveUploadFile(file, "avatars");
                    currentUser.setImages(fileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.userService.update(currentUser);
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{username}")
    public String getDeleteUserPage(Model model, @PathVariable String username) {
        User user = userService.findById(username);
        model.addAttribute("newUser", user);
        return "admin/users/delete";
    }

    @PostMapping("/delete")
    public String getDeleteUser(Model model, @ModelAttribute("newUser") User user) {
        this.userService.delete(user.getUsername());
        return "redirect:/admin/users";
    }
}
