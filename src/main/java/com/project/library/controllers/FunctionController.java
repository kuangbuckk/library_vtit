package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.FunctionDTO;
import com.project.library.entities.Function;
import com.project.library.responses.FunctionResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.IFunctionService;
import com.project.library.utils.MessageKeys;
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

    @PostMapping("/")
    public ResponseEntity<?> addNewFunction(
            @RequestBody @Valid FunctionDTO functionDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        FunctionResponse newFunction = functionService.addFunction(functionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(
                MessageKeys.INSERT_FUNCTION_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_FUNCTION_SUCCESSFULLY),
                newFunction));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateFunction(
            @RequestBody @Valid FunctionDTO functionDTO,
            @PathVariable UUID code,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        FunctionResponse existingFunction = functionService.getFunctionByCode(code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_FUNCTION_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_FUNCTION_SUCCESSFULLY),
                existingFunction));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteFunction(@PathVariable UUID code) {
        functionService.deleteFunction(code);
        return ResponseEntity.ok("Deleted function");
    }
}
