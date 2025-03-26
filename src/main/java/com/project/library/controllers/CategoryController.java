package com.project.library.controllers;

import com.project.library.utils.LocalizationUtils;
import com.project.library.dtos.CategoryDTO;
import com.project.library.responses.CategoryResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.CategoryService;
import com.project.library.utils.MessageKeys;
import com.project.library.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/categories")
@AllArgsConstructor
public class CategoryController {
    //SpringBoot sẽ tự động tìm implementation của interface này và inject vào
    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    @PreAuthorize("permitAll()")
    public ResponseEntity<GenericResponse<List<CategoryResponse>>> getAllCategories() {
        return ResponseUtil.success(
                MessageKeys.GET_CATEGORY_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.GET_CATEGORY_SUCCESSFULLY),
                categoryService.getAllCategories()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<GenericResponse> getCategory(@PathVariable Long id) {
        CategoryResponse existingCategory = categoryService.getCategoryById(id);
        return ResponseEntity.ok(GenericResponse.success(existingCategory));
    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> addCategory(
            @RequestBody @Valid CategoryDTO categoryDTO,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        CategoryResponse newCategory = categoryService.addCategory(categoryDTO);
        return ResponseUtil.success(
                MessageKeys.INSERT_CATEGORY_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY),
                newCategory
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryDTO categoryDTO,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        CategoryResponse updatedCategory = categoryService.updateCategory(categoryDTO, id);
        return ResponseUtil.success(
                MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY),
                updatedCategory
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        categoryService.deleteCategory(id);
        return ResponseUtil.success(
                MessageKeys.DELETE_CATEGORY_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY),
                id
        );
    }
}
