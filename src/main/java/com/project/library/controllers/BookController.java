package com.project.library.controllers;

import com.project.library.dtos.BookDTO;
import com.project.library.entities.Book;
import com.project.library.responses.BookPageResponse;
import com.project.library.responses.BookResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.IBookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/")
    public ResponseEntity<GenericResponse<BookPageResponse>> getAllBooks(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("size") int size
    ) {
        BookPageResponse bookPageResponse = bookService.getAllBooks(PageRequest.of(pageNumber, size));
        return ResponseEntity.ok(GenericResponse.success(bookPageResponse));
    }

    @GetMapping("/{code}")
    public ResponseEntity<GenericResponse<BookResponse>> getBookByCode(@PathVariable String code) {
        BookResponse bookResponse = bookService.getBookByCode(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(bookResponse));

    }

    @PostMapping("/")
    public ResponseEntity<?> createBook(
            @RequestBody @Valid BookDTO bookDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        BookResponse newBookResponse = bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(newBookResponse));
    }

    @PutMapping("/{code}")
    public ResponseEntity<GenericResponse> updateBook(
            @RequestBody @Valid BookDTO bookDTO,
            BindingResult result,
            @PathVariable String code
    ) {
        if (result.hasErrors()) {
            List<String> errors = result
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        BookResponse updatedBook = bookService.updateBook(bookDTO, UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(updatedBook));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteBook(@PathVariable String code) {
        bookService.deleteBook(UUID.fromString(code));
        return ResponseEntity.ok(GenericResponse.success(code));
    }
}
