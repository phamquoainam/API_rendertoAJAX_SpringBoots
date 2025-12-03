package com.hoainam.controller.admin;

import java.io.IOException; 
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

import com.hoainam.entity.Category;
import com.hoainam.service.CategoryService;
import com.hoainam.service.UploadService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UploadService uploadService;

    @GetMapping("")
    public String index(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories/list";
    }

    @RequestMapping("/create")
    public String getCreateCategoryPage(Model model) {
        model.addAttribute("newCategory", new Category());
        return "/admin/categories/create";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("newCategory") Category category,
            @RequestParam("imageFile") MultipartFile file)
            throws IOException {

        try {
            if (file != null && !file.isEmpty()) {
                // Lưu file vào thư mục /resources/images/avatars
                String fileName = uploadService.handleSaveUploadFile(file, "category");
                category.setImages(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        categoryService.create(category);
        return "redirect:/admin/categories";
    }

    @RequestMapping("/update/{id}")
    public String getUpdateCategoryPage(Model model, @PathVariable("id") Long id) {
        Category currentCategory = categoryService.findById(id);
        model.addAttribute("newCategory", currentCategory);
        return "/admin/categories/update";
    }

    @PostMapping("/update")
    public String updateCategory(
            Model model,
            @ModelAttribute("newCategory") Category category,
            @RequestParam("imageFile") MultipartFile file) throws IOException {

        Category currentCategory = categoryService.findById(category.getCategoryId());
        if (currentCategory != null) {

            currentCategory.setCategoryName(category.getCategoryName());
            currentCategory.setCategoryCode(category.getCategoryCode());
            currentCategory.setImages(category.getImages());
            currentCategory.setStatus(category.getStatus());

            try {
                if (file != null && !file.isEmpty()) {
                    String fileName = uploadService.handleSaveUploadFile(file, "category");
                    currentCategory.setImages(fileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            categoryService.update(currentCategory);
        }

        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String getDeleteCategoryPage(Model model, @PathVariable Long id) {
        model.addAttribute("id", id);
        model.addAttribute("newCategory", new Category());
        return "/admin/categories/delete";
    }

    @PostMapping("/delete")
    public String deleteCategory(Model model, @ModelAttribute("newCategory") Category category) {
        categoryService.delete(category.getCategoryId());
        return "redirect:/admin/categories";
    }
}
