package com.project.library.controllers;

import com.project.library.dtos.CategoryDTO;
import com.project.library.entities.Category;
import com.project.library.responses.CategoryResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.CategoryServiceImpl;
import com.project.library.services.interfaces.ICategoryService;
import com.project.library.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoryController {
    //SpringBoot sẽ tự động tìm implementation của interface này và inject vào
    private final ICategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<GenericResponse> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(GenericResponse.success(categories));
    }

    @GetMapping("/{code}")
    public ResponseEntity<GenericResponse> getCategory(@PathVariable UUID code) {
        CategoryResponse existingCategory = categoryService.getCategoryById(code);
        return ResponseEntity.ok(GenericResponse.success(existingCategory));
    }

    @PostMapping("/")
    public ResponseEntity<?> addCategory(
            @RequestBody @Valid CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        CategoryResponse newCategory = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(newCategory));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateCategory(
            @PathVariable UUID code,
            @RequestBody @Valid CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        CategoryResponse updatedCategory = categoryService.updateCategory(categoryDTO, code);
        return ResponseEntity.ok(GenericResponse.success(updatedCategory));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteCategory(@PathVariable String code) {
        categoryService.deleteCategory(UUID.fromString(code));
        return ResponseEntity.ok("Deleted " + code + " category");
    }
}
