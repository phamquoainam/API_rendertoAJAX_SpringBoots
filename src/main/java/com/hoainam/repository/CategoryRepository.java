package com.hoainam.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoainam.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategoryCode(String categoryCode);

    boolean existsByCategoryCode(String categoryCode);

    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
    
    Optional<Category> findByCategoryName(String name);
}