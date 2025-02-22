package com.project.library.controllers;

import com.project.library.dtos.BorrowDTO;
import com.project.library.entities.Borrow;
import com.project.library.responses.BorrowPageResponse;
import com.project.library.responses.BorrowResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.IBorrowService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/borrows")
@AllArgsConstructor
public class BorrowController {
    private final IBorrowService borrowService;

    @GetMapping("/")
    public ResponseEntity<GenericResponse> getBorrows(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("size") int size
    ) {
        BorrowPageResponse borrowPageResponse = borrowService.getAllBorrows(PageRequest.of(pageNumber, size));
        return ResponseEntity.ok(GenericResponse.success(borrowPageResponse));
    }

    @GetMapping("/{code}")
    public ResponseEntity<GenericResponse> getBorrow(@PathVariable String code) {
        BorrowResponse borrowResponse = borrowService.getBorrowByCode(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(borrowResponse));
    }

    @PostMapping("/")
    public ResponseEntity<GenericResponse> addBorrow(
            @RequestBody @Valid BorrowDTO borrowDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponse.error(errors.toString()));
        }
        BorrowResponse borrowResponse = borrowService.addBorrow(borrowDTO);
        return ResponseEntity.ok(GenericResponse.success(borrowResponse));
    }

    @PutMapping("/{code}")
    public ResponseEntity<GenericResponse> updateBorrow(
            @RequestBody @Valid BorrowDTO borrowDTO,
            @PathVariable UUID code,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponse.error(errors.toString()));
        }
        BorrowResponse updateBorrowResponse = borrowService.updateBorrow(borrowDTO, code);
        return ResponseEntity.ok(GenericResponse.success(updateBorrowResponse));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<GenericResponse> deleteBorrow(@PathVariable String code) {
        borrowService.deleteBorrow(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(code));
    }
}
