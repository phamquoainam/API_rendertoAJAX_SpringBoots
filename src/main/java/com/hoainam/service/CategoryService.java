package com.hoainam.service;

import java.util.List; 

import com.hoainam.entity.Category;

public interface CategoryService {
    List<Category> findAll();

    Category findById(Long id);

    Category create(Category category);

    Category update(Category category);

    void delete(Long id);
    
}
