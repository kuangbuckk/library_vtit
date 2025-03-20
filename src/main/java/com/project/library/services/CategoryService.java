package com.project.library.services;

import com.project.library.dtos.CategoryDTO;
import com.project.library.responses.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse addCategory(CategoryDTO categoryDTO);
    CategoryResponse updateCategory(CategoryDTO categoryDTO, Long id);
    void deleteCategory(Long id);
}
