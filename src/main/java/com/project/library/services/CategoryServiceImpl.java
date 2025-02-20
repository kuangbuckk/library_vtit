package com.project.library.services;

import com.project.library.dtos.CategoryDTO;
import com.project.library.entities.Category;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.CategoryRepository;
import com.project.library.responses.CategoryResponse;
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
    public List<CategoryResponse> getAllCategories() {
        List<CategoryResponse> categories = categoryRepository.findAll()
                .stream()
                .map(category -> CategoryResponse.fromCategory(category))
                .toList();
        return categories;
    }

    @Override
    public CategoryResponse getCategoryById(UUID code) {
        Category category = categoryRepository
                .findById(code)
                .orElseThrow(() -> new DataNotFoundException("Category not found with code " + code));
        return CategoryResponse.fromCategory(category);
    }

    @Override
    public CategoryResponse addCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .categoryName(categoryDTO.getCategoryName())
                .build();
        categoryRepository.save(newCategory);
        return CategoryResponse.fromCategory(newCategory);
    }

    @Override
    public CategoryResponse updateCategory(CategoryDTO categoryDTO, UUID code) {
        Category existingCategory = categoryRepository.findById(code)
                .orElseThrow(() -> new DataNotFoundException("Category not found with code " + code));
        existingCategory.setCategoryName(categoryDTO.getCategoryName());
        categoryRepository.save(existingCategory);
        return CategoryResponse.fromCategory(existingCategory);
    }

    @Override
    public void deleteCategory(UUID code) {
        Category existingCategory = categoryRepository.findById(code)
                .orElseThrow(() -> new DataNotFoundException("Category not found with code " + code));
        categoryRepository.delete(existingCategory);
    }
}
