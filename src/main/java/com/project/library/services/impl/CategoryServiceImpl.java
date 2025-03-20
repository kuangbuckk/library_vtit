package com.project.library.services.impl;

import com.project.library.dtos.CategoryDTO;
import com.project.library.entities.Category;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.CategoryRepository;
import com.project.library.responses.CategoryResponse;
import com.project.library.services.CategoryService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoryResponse> categories = categoryRepository.findAll()
                .stream()
                .map(category -> CategoryResponse.fromCategory(category))
                .toList();
        return categories;
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.CATEGORY_NOT_FOUND, id));
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
    public CategoryResponse updateCategory(CategoryDTO categoryDTO, Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.CATEGORY_NOT_FOUND, id));
        existingCategory.setCategoryName(categoryDTO.getCategoryName());
        categoryRepository.save(existingCategory);
        return CategoryResponse.fromCategory(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.CATEGORY_NOT_FOUND, id));
        categoryRepository.delete(existingCategory);
    }
}
