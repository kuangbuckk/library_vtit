package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.CategoryDTO;
import com.project.library.entities.Category;
import com.project.library.responses.CategoryResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.CategoryServiceImpl;
import com.project.library.services.interfaces.ICategoryService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/categories")
@AllArgsConstructor
public class CategoryController {
    //SpringBoot sẽ tự động tìm implementation của interface này và inject vào
    private final ICategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    @PreAuthorize("permitAll()")
    public ResponseEntity<GenericResponse> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(GenericResponse.success(categories));
    }

    @GetMapping("/{code}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<GenericResponse> getCategory(@PathVariable UUID code) {
        CategoryResponse existingCategory = categoryService.getCategoryById(code);
        return ResponseEntity.ok(GenericResponse.success(existingCategory));
    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> addCategory(
            @RequestBody @Valid CategoryDTO categoryDTO,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        CategoryResponse newCategory = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(
                MessageKeys.INSERT_CATEGORY_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY),
                newCategory));
    }

    @PutMapping("/update/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> updateCategory(
            @PathVariable UUID code,
            @RequestBody @Valid CategoryDTO categoryDTO,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        CategoryResponse updatedCategory = categoryService.updateCategory(categoryDTO, code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY),
                updatedCategory));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> deleteCategory(
            @PathVariable String code,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        categoryService.deleteCategory(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_CATEGORY_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY),
                code));
    }
}
