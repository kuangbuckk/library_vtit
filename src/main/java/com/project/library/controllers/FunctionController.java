package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.FunctionDTO;
import com.project.library.entities.Function;
import com.project.library.responses.FunctionResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.IFunctionService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("${api.prefix}/functions")
@AllArgsConstructor
public class FunctionController {
    private final IFunctionService functionService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<?> getAllFunctions() {
        return ResponseEntity.ok(GenericResponse.success(functionService.getAllFunctions()));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getFunction(@PathVariable UUID code) {
        FunctionResponse existingFunction = functionService.getFunctionByCode(code);
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

    @PutMapping("/update/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> updateFunction(
            @RequestBody @Valid FunctionDTO functionDTO,
            @PathVariable UUID code,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        FunctionResponse existingFunction = functionService.getFunctionByCode(code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_FUNCTION_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_FUNCTION_SUCCESSFULLY),
                existingFunction));
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> deleteFunction(
            @PathVariable UUID code,
            @P("httpServletRequest") HttpServletRequest httpServletRequest
    ) {
        functionService.deleteFunction(code);
        return ResponseEntity.ok("Deleted function");
    }
}
