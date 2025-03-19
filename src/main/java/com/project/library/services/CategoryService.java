package com.project.library.services.interfaces;

import com.project.library.dtos.CategoryDTO;
import com.project.library.entities.Category;
import com.project.library.responses.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(UUID code);
    CategoryResponse addCategory(CategoryDTO categoryDTO);
    CategoryResponse updateCategory(CategoryDTO categoryDTO, UUID code);
    void deleteCategory(UUID code);
}
