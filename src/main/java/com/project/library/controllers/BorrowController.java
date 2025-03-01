package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.BorrowDTO;
import com.project.library.entities.Borrow;
import com.project.library.responses.BorrowPageResponse;
import com.project.library.responses.BorrowResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.IBorrowService;
import com.project.library.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('LIBRARIAN')")
    public ResponseEntity<GenericResponse> getBorrows(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("size") int size
    ) {
        BorrowPageResponse borrowPageResponse = borrowService.getAllBorrows(PageRequest.of(pageNumber, size));
        return ResponseEntity.ok(GenericResponse.success(borrowPageResponse));
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('LIBRARIAN')")
    public ResponseEntity<GenericResponse> getBorrow(@PathVariable String code) {
        BorrowResponse borrowResponse = borrowService.getBorrowByCode(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(borrowResponse));
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('LIBRARIAN') OR hasRole('USER')")
    public ResponseEntity<GenericResponse> addBorrow(
            @RequestBody @Valid BorrowDTO borrowDTO
    ) {
        BorrowResponse borrowResponse = borrowService.addBorrow(borrowDTO);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.INSERT_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_BORROW_SUCCESSFULLY),
                borrowResponse));
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('LIBRARIAN')")
    public ResponseEntity<GenericResponse> updateBorrow(
            @RequestBody @Valid BorrowDTO borrowDTO,
            @PathVariable UUID code) {
        BorrowResponse updateBorrowResponse = borrowService.updateBorrow(borrowDTO, code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_BORROW_SUCCESSFULLY),
                updateBorrowResponse));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('LIBRARIAN')")
    public ResponseEntity<GenericResponse> deleteBorrow(@PathVariable String code) {
        borrowService.deleteBorrow(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BORROW_SUCCESSFULLY),
                code));
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')")
    public ResponseEntity<GenericResponse> destroyBorrow(@PathVariable String code) {
        borrowService.destroyBorrow(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BORROW_SUCCESSFULLY),
                code));
    }
}
