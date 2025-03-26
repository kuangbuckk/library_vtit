package com.project.library.controllers;

import com.project.library.utils.LocalizationUtils;
import com.project.library.dtos.BorrowDTO;
import com.project.library.dtos.search.BorrowSearchDTO;
import com.project.library.responses.BorrowPageResponse;
import com.project.library.responses.BorrowResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.BorrowService;
import com.project.library.utils.MessageKeys;
import com.project.library.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/borrows")
@AllArgsConstructor
public class BorrowController {
    private final BorrowService borrowService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<BorrowPageResponse>> getBorrows(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("size") int size,
            @RequestBody BorrowSearchDTO borrowSearchDTO,
            HttpServletRequest httpServletRequest
    ) {
        BorrowPageResponse borrowPageResponse = borrowService.getAllBorrows(pageNumber, size, borrowSearchDTO);
        return ResponseUtil.success(HttpStatus.FOUND.toString(), "success", borrowPageResponse);
    }

//    @GetMapping("/user/{id}")
//    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
//    public ResponseEntity<GenericResponse<BorrowPageResponse>> getBorrows(
//            @RequestParam("page_number") int pageNumber,
//            @RequestParam("size") int size,
//            @PathVariable("id") Long userId,
//            @RequestBody BorrowSearchDTO borrowSearchDTO,
//            HttpServletRequest httpServletRequest
//    ) {
//        BorrowPageResponse borrowPageResponse = borrowService.getAllBorrows(pageNumber, size, borrowSearchDTO);
//        return ResponseUtil.success(HttpStatus.FOUND.toString(), "success", borrowPageResponse);
//    }

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
    public ResponseEntity<GenericResponse<BorrowResponse>> addBorrow(
            @RequestBody @Valid BorrowDTO borrowDTO,
            Authentication authentication,
            HttpServletRequest httpServletRequest
    ) {
        BorrowResponse borrowResponse = borrowService.addBorrow(authentication, borrowDTO);
        return ResponseUtil.success(
                MessageKeys.INSERT_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_BORROW_SUCCESSFULLY),
                borrowResponse
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<BorrowResponse>> updateBorrow(
            @RequestBody @Valid BorrowDTO borrowDTO,
            @PathVariable Long id,
            HttpServletRequest httpServletRequest
    ) {
        BorrowResponse updateBorrowResponse = borrowService.updateBorrow(borrowDTO, id);
        return ResponseUtil.success(
                MessageKeys.UPDATE_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_BORROW_SUCCESSFULLY),
                updateBorrowResponse
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<Long>> deleteBorrow(
            @PathVariable Long id,
            HttpServletRequest httpServletRequest
    ) {
        borrowService.deleteBorrow(id);
        return ResponseUtil.success(
                MessageKeys.DELETE_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BORROW_SUCCESSFULLY),
                id
        );
    }

    @DeleteMapping("/destroy/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<Long>> destroyBorrow(
            @PathVariable Long id,
            HttpServletRequest httpServletRequest
    ) {
        borrowService.destroyBorrow(id);
        return ResponseUtil.success(
                MessageKeys.DESTROY_BORROW_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DESTROY_BORROW_SUCCESSFULLY),
                id
        );
    }
}
