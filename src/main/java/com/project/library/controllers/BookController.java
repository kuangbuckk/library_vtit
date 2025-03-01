package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.BookDTO;
import com.project.library.entities.Book;
import com.project.library.responses.BookPageResponse;
import com.project.library.responses.BookResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.IBookService;
import com.project.library.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/books")
@AllArgsConstructor
public class BookController {
    private final IBookService bookService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<GenericResponse<BookPageResponse>> getBooks(
            @RequestParam(defaultValue = "0", name = "page_number") int pageNumber,
            @RequestParam(defaultValue = "5", name = "size") int size,
            @RequestParam(defaultValue = "") String keyword
    ) {
        BookPageResponse bookPageResponse = bookService.getAllBooks(pageNumber, size, keyword);
        return ResponseEntity.ok(GenericResponse.success(bookPageResponse));
    }

    @GetMapping("/{code}")
    public ResponseEntity<GenericResponse<BookResponse>> getBookByCode(@PathVariable String code) {
        BookResponse bookResponse = bookService.getBookByCode(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(bookResponse));

    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('LIBRARIAN')")
    public ResponseEntity<?> createBook(
            @RequestBody @Valid BookDTO bookDTO
    ) {
        BookResponse newBookResponse = bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericResponse.success(
                        MessageKeys.INSERT_BOOK_SUCCESSFULLY,
                        localizationUtils.getLocalizedMessage(MessageKeys.INSERT_BOOK_SUCCESSFULLY),
                        newBookResponse));
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('LIBRARIAN')")
    public ResponseEntity<GenericResponse> updateBook(
            @RequestBody @Valid BookDTO bookDTO,
            @PathVariable String code
    ) {
        BookResponse updatedBook = bookService.updateBook(bookDTO, UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_BOOK_SUCCESSFULLY),
                updatedBook));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')")
    public ResponseEntity<?> deleteBook(@PathVariable String code) {
        bookService.deleteBook(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BOOK_SUCCESSFULLY),
                code));
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> destroyBook(@PathVariable String code) {
        bookService.destroyBook(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BOOK_SUCCESSFULLY),
                code));
    }
}
