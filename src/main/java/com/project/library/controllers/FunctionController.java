package com.project.library.controllers;

import com.project.library.utils.LocalizationUtils;
import com.project.library.dtos.FunctionDTO;
import com.project.library.responses.FunctionResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.FunctionService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/functions")
@AllArgsConstructor
public class FunctionController {
    private final FunctionService functionService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<?> getAllFunctions() {
        return ResponseEntity.ok(GenericResponse.success(functionService.getAllFunctions()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFunction(@PathVariable Long id) {
        FunctionResponse existingFunction = functionService.getFunctionByCode(id);
        return ResponseEntity.ok(GenericResponse.success(existingFunction));
    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> addNewFunction(
            @RequestBody @Valid FunctionDTO functionDTO,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        FunctionResponse newFunction = functionService.addFunction(functionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(
                MessageKeys.INSERT_FUNCTION_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_FUNCTION_SUCCESSFULLY),
                newFunction));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> updateFunction(
            @RequestBody @Valid FunctionDTO functionDTO,
            @PathVariable Long id,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        FunctionResponse existingFunction = functionService.getFunctionByCode(id);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_FUNCTION_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_FUNCTION_SUCCESSFULLY),
                existingFunction));
    }

    @DeleteMapping("/destroy/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> deleteFunction(
            @PathVariable Long id,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        functionService.deleteFunction(id);
        return ResponseEntity.ok("Deleted function");
    }
}
