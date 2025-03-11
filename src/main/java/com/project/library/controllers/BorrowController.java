package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.BorrowDTO;
import com.project.library.dtos.BorrowSearchDTO;
import com.project.library.entities.Borrow;
import com.project.library.responses.BorrowPageResponse;
import com.project.library.responses.BorrowResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.IBorrowService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
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
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> getBorrows(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("size") int size,
            @RequestBody BorrowSearchDTO borrowSearchDTO,
            HttpServletRequest httpServletRequest
    ) {
        BorrowPageResponse borrowPageResponse = borrowService.getAllBorrows(pageNumber, size, borrowSearchDTO);
        return ResponseEntity.ok(GenericResponse.success(borrowPageResponse));
    }

//    @GetMapping("/{code}")
////    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('LIBRARIAN')")
//    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
//    public ResponseEntity<GenericResponse> getBorrow(
//            @PathVariable String code,
//            HttpServletRequest httpServletRequest
//    ) {
//        BorrowResponse borrowResponse = borrowService.getBorrowByCode(UUID.fromString(code));
//        return ResponseEntity.ok(GenericResponse.success(borrowResponse));
//    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> addBorrow(
            @RequestBody @Valid BorrowDTO borrowDTO,
            HttpServletRequest httpServletRequest
    ) {
        BorrowResponse borrowResponse = borrowService.addBorrow(borrowDTO);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.INSERT_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_BORROW_SUCCESSFULLY),
                borrowResponse));
    }

    @PutMapping("/update/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> updateBorrow(
            @RequestBody @Valid BorrowDTO borrowDTO,
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {
        BorrowResponse updateBorrowResponse = borrowService.updateBorrow(borrowDTO, code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_BORROW_SUCCESSFULLY),
                updateBorrowResponse));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> deleteBorrow(
            @PathVariable String code,
            HttpServletRequest httpServletRequest
    ) {
        borrowService.deleteBorrow(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BORROW_SUCCESSFULLY),
                code));
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> destroyBorrow(
            @PathVariable String code,
            HttpServletRequest httpServletRequest
    ) {
        borrowService.destroyBorrow(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BORROW_SUCCESSFULLY),
                code));
    }
}
