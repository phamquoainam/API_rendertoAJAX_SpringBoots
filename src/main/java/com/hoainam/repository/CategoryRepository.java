package com.hoainam.repository;

import org.springframework.data.jpa.repository.JpaRepository; 

import com.hoainam.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategoryCode(String categoryCode);

    boolean existsByCategoryCode(String categoryCode);
}
