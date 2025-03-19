package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.BookDTO;
import com.project.library.dtos.search.BookSearchDTO;
import com.project.library.responses.BookPageResponse;
import com.project.library.responses.BookResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.BookService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<GenericResponse<BookPageResponse>> getBooks(
            @RequestParam(defaultValue = "0", name = "page_number") int pageNumber,
            @RequestParam(defaultValue = "5", name = "size") int size,
//            , @RequestParam(defaultValue = "", name = "author") String author,
            @RequestBody BookSearchDTO bookSearchDTO
    ) {
        BookPageResponse bookPageResponse = bookService.getAllBooks(pageNumber, size, bookSearchDTO);
        return ResponseEntity.ok(GenericResponse.success(bookPageResponse));
    }

//    @GetMapping("/{code}")
//    @PreAuthorize("permitAll()")
//    public ResponseEntity<GenericResponse<BookResponse>> getBookByCode(@PathVariable String code) {
//        BookResponse bookResponse = bookService.getBookByCode(UUID.fromString(code));
//        return ResponseEntity.ok(GenericResponse.success(bookResponse));
//
//    }

    @GetMapping("/excel-report")
    public ResponseEntity<byte[]> downloadExcel() throws FileNotFoundException {
        byte[] excelFile = bookService.exportBookExcelReport();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_report.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> createBook(
            @RequestBody @Valid BookDTO bookDTO,
            HttpServletRequest httpServletRequest
    ) {
        BookResponse newBookResponse = bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericResponse.success(
                        MessageKeys.INSERT_BOOK_SUCCESSFULLY,
                        localizationUtils.getLocalizedMessage(MessageKeys.INSERT_BOOK_SUCCESSFULLY),
                        newBookResponse));
    }

    @PutMapping("/update/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> updateBook(
            @RequestBody @Valid BookDTO bookDTO,
            @PathVariable String code,
            HttpServletRequest httpServletRequest
    ) {
        BookResponse updatedBook = bookService.updateBook(bookDTO, UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_BOOK_SUCCESSFULLY),
                updatedBook));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> deleteBook(@PathVariable String code, HttpServletRequest httpServletRequest) {
        bookService.deleteBook(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BOOK_SUCCESSFULLY),
                code));
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> destroyBook(@PathVariable String code, HttpServletRequest httpServletRequest) {
        bookService.destroyBook(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BOOK_SUCCESSFULLY),
                code));
    }
}
