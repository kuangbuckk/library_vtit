package com.project.library.controllers;

import com.project.library.dtos.BookDTO;
import com.project.library.entities.Book;
import com.project.library.services.interfaces.IBookService;
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
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookController {
    private final IBookService bookService;

    @GetMapping("/")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getBookByCode(@PathVariable String code) {
        Book existingBook = bookService.getBookByCode(UUID.fromString(code));
        return ResponseEntity.ok(existingBook);

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
        Book newBook = bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateBook(
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
            return ResponseEntity.badRequest().body(errors);
        }
        Book updatedBook = bookService.updateBook(bookDTO, UUID.fromString(code));
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteBook(@PathVariable String code) {
        bookService.deleteBook(UUID.fromString(code));
        return ResponseEntity.ok("Deleted Book with code: " + code);
    }
}
