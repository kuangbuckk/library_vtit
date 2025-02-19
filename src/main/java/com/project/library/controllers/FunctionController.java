package com.project.library.controllers;

import com.project.library.dtos.FunctionDTO;
import com.project.library.entities.Function;
import com.project.library.services.interfaces.IFunctionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/functions")
@AllArgsConstructor
public class FunctionController {
    private final IFunctionService functionService;

    @GetMapping("/")
    public ResponseEntity<?> getAllFunctions() {
        return ResponseEntity.ok(functionService.getAllFunctions());
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getFunction(@PathVariable String code) {
        Function existingFunction = functionService.getFunctionByCode(code);
        return ResponseEntity.ok(existingFunction);
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
        Function newFunction = functionService.addFunction(functionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFunction);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateFunction(
            @RequestBody @Valid FunctionDTO functionDTO,
            @PathVariable String code,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Function existingFunction = functionService.getFunctionByCode(code);
        return ResponseEntity.ok(existingFunction);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteFunction(@PathVariable String code) {
        functionService.deleteFunction(code);
        return ResponseEntity.ok("Deleted function");
    }
}
