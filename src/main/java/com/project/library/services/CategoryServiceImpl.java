package com.project.library.services;

import com.project.library.dtos.CategoryDTO;
import com.project.library.entities.Category;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.CategoryRepository;
import com.project.library.services.interfaces.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    public Category getCategoryById(UUID code) {
        Category category = categoryRepository
                .findById(code)
                .orElseThrow(() -> new DataNotFoundException("Category not found with code " + code));
        return category;
    }

    @Override
    public Category addCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .categoryName(categoryDTO.getCategoryName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category updateCategory(CategoryDTO categoryDTO, UUID code) {
        Category existingCategory = this.getCategoryById(code);
        existingCategory.setCategoryName(categoryDTO.getCategoryName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(UUID code) {
        Category existingCategory = this.getCategoryById(code);
        categoryRepository.delete(existingCategory);
    }
}
