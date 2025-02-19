package com.project.library.services.interfaces;

import com.project.library.dtos.CategoryDTO;
import com.project.library.entities.Category;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(UUID code);
    Category addCategory(CategoryDTO categoryDTO);
    Category updateCategory(CategoryDTO categoryDTO, UUID code);
    void deleteCategory(UUID code);
}
