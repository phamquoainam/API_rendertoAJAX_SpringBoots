package com.hoainam.controller.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hoainam.entity.Category;
import com.hoainam.models.Response;
import com.hoainam.repository.CategoryRepository;
import com.hoainam.service.CategoryService;
import com.hoainam.service.UploadService;

@RestController
@RequestMapping(path = "/api/category")
public class CategoryAPIController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository; // Inject thêm để check trùng tên

    @Autowired
    private UploadService uploadService; // Dùng Service upload của bạn

    // 1. Lấy tất cả Category
    @GetMapping
    public ResponseEntity<Response> getAllCategory() {
        return new ResponseEntity<>(
            new Response(true, "Thành công", categoryService.findAll()), 
            HttpStatus.OK
        );
    }

    // 2. Thêm Category (Có upload ảnh)
    @PostMapping(path = "/addCategory", consumes = {"multipart/form-data"})
    public ResponseEntity<Response> addCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam("categoryCode") String categoryCode,
            @RequestParam("status") Boolean status,
            @RequestParam(value = "image", required = false) MultipartFile icon) { // "image" là tên field trong Postman

        // Check trùng tên
        Optional<Category> optCategory = categoryRepository.findByCategoryName(categoryName);
        if (optCategory.isPresent()) {
            return new ResponseEntity<>(
                new Response(false, "Category đã tồn tại", null), 
                HttpStatus.BAD_REQUEST
            );
        }

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCategoryCode(categoryCode);
        category.setStatus(status);

        // Xử lý upload ảnh bằng UploadService của bạn
        if (icon != null && !icon.isEmpty()) {
            String savedFilename = uploadService.handleSaveUploadFile(icon, "category");
            category.setImages(savedFilename);
        }

        categoryService.create(category);
        return new ResponseEntity<>(
            new Response(true, "Thêm thành công", category), 
            HttpStatus.OK
        );
    }

    // 3. Cập nhật Category
    @PutMapping(path = "/updateCategory", consumes = {"multipart/form-data"})
    public ResponseEntity<Response> updateCategory(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("categoryName") String categoryName,
            @RequestParam("status") Boolean status,
            @RequestParam(value = "image", required = false) MultipartFile icon) {

        Category currentCategory = categoryService.findById(categoryId);
        
        if (currentCategory == null) {
            return new ResponseEntity<>(
                new Response(false, "Không tìm thấy Category ID: " + categoryId, null), 
                HttpStatus.NOT_FOUND
            );
        }

        // Cập nhật thông tin
        currentCategory.setCategoryName(categoryName);
        currentCategory.setStatus(status);

        // Nếu có upload ảnh mới thì thay thế, không thì giữ nguyên ảnh cũ
        if (icon != null && !icon.isEmpty()) {
            String savedFilename = uploadService.handleSaveUploadFile(icon, "category");
            currentCategory.setImages(savedFilename);
        }

        categoryService.update(currentCategory);
        return new ResponseEntity<>(
            new Response(true, "Cập nhật thành công", currentCategory), 
            HttpStatus.OK
        );
    }

    // 4. Xóa Category
    @DeleteMapping(path = "/deleteCategory/{id}")
    public ResponseEntity<Response> deleteCategory(@PathVariable("id") Long id) {
        if (categoryService.findById(id) == null) {
            return new ResponseEntity<>(
                new Response(false, "Không tìm thấy Category", null), 
                HttpStatus.NOT_FOUND
            );
        }

        categoryService.delete(id);
        return new ResponseEntity<>(
            new Response(true, "Xóa thành công", null), 
            HttpStatus.OK
        );
    }
}