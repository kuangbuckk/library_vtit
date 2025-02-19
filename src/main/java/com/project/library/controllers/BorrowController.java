package com.project.library.controllers;

import com.project.library.dtos.BorrowDTO;
import com.project.library.entities.Borrow;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.IBorrowService;
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
@RequestMapping("/api/v1/borrows")
@AllArgsConstructor
public class BorrowController {
    private final IBorrowService borrowService;

    @GetMapping("/")
    public ResponseEntity<GenericResponse<List<Borrow>>> getBorrows() {
        List<Borrow> borrowList = borrowService.getAllBorrows();
        return ResponseEntity.ok(GenericResponse.success(borrowList));
    }

    @GetMapping("/{code}")
    public ResponseEntity<GenericResponse<Borrow>> getBorrow(@PathVariable String code) {
        Borrow existingBorrow = borrowService.getBorrowByCode(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(existingBorrow));
    }

    @PostMapping("/")
    public ResponseEntity<GenericResponse<Borrow>> addBorrow(
            @RequestBody @Valid BorrowDTO borrowDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponse.error(errors.toString()));
        }
        Borrow newBorrow = borrowService.addBorrow(borrowDTO);
        return ResponseEntity.ok(GenericResponse.success(newBorrow));
    }

    //TODO: add update and delete

}
