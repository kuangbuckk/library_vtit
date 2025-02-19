package com.project.library.services;

import com.project.library.dtos.BookDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Category;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.CategoryRepository;
import com.project.library.services.interfaces.IBookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookServiceImpl implements IBookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    @Override
    public Book getBookByCode(UUID code) {
        Book book = bookRepository
                .findById(code)
                .orElseThrow(()-> new DataNotFoundException("Can't find book with code: " + code));
        return book;
    }

    @Override
    public Book addBook(BookDTO bookDTO) {
        List<UUID> categoryCodes = bookDTO.getCategoryCodes()
                .stream()
                .map(categoryCode -> UUID.fromString(categoryCode))
                .toList();
        List<Category> categories = categoryRepository.findCategoriesByCodeIn(categoryCodes);

        Book newBook = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .amount(bookDTO.getAmount())
                .categories(categories)
                .build();
        return bookRepository.save(newBook);
    }

    @Override
    public Book updateBook(BookDTO bookDTO, UUID code) {
        List<UUID> categoryCodes = bookDTO.getCategoryCodes()
                .stream()
                .map(categoryCode -> UUID.fromString(categoryCode))
                .toList();
        List<Category> categories = categoryRepository.findCategoriesByCodeIn(categoryCodes);

        Book existingBook = getBookByCode(code);
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setAmount(bookDTO.getAmount());
        existingBook.setCategories(categories);

        return existingBook;
    }

    @Override
    public void deleteBook(UUID code) {
        bookRepository.deleteById(code);
    }
}
