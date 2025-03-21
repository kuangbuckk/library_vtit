package com.project.library.services.impl;

import com.project.library.dtos.BookDTO;
import com.project.library.dtos.search.BookSearchDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Category;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.CategoryRepository;
import com.project.library.responses.BookPageResponse;
import com.project.library.responses.BookResponse;
import com.project.library.services.BookService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Primary
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public BookPageResponse getAllBooks(int pageNumber, int size, BookSearchDTO bookSearchDTO) {
        Pageable pageable = PageRequest.of(pageNumber, size); //Sort.by("code").ascending()
        Page<Book> books = bookRepository.findAll(pageable, bookSearchDTO);
        int totalPage = books.getTotalPages();
        List<BookResponse> bookResponses = books.getContent()
                .stream()
                .map(book -> BookResponse.fromBook(book))
                .toList();

        return BookPageResponse.builder()
                .bookResponses(bookResponses)
                .totalPages(totalPage)
                .build();
    }

    @Override
    public BookResponse getBookByCode(Long id) {
        Book book = bookRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, id));
        return BookResponse.fromBook(book);
    }

    @Override
    @Transactional
    public BookResponse addBook(BookDTO bookDTO) {
        List<Category> categories = categoryRepository.findCategoriesByIdIn(bookDTO.getCategoryIds());
        Book newBook = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .amount(bookDTO.getAmount())
                .language(bookDTO.getLanguage())
                .description(bookDTO.getDescription())
                .pageCount(bookDTO.getPageCount())
                .categories(categories)
                .build();
        bookRepository.save(newBook);
        return BookResponse.fromBook(newBook);
    }

    @Override
    @Transactional
    public BookResponse updateBook(BookDTO bookDTO, Long id) {
        List<Category> categories = categoryRepository.findCategoriesByIdIn(bookDTO.getCategoryIds());

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, id));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setAmount(bookDTO.getAmount());
        existingBook.setCategories(categories);
        bookRepository.save(existingBook);

        return BookResponse.fromBook(existingBook);
    }

    @Override
    public BookResponse deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, id));
        book.setIsDeleted(true);
        bookRepository.save(book);
        return BookResponse.fromBook(book);
    }

    @Override
    @Transactional
    public void destroyBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, id));
        bookRepository.delete(book);
    }

    @Override
    public byte[] exportBookExcelReport() {
        List<Book> books = bookRepository.findAll();
        try (InputStream is = new ClassPathResource("templates/book-template.xlsx").getInputStream()) {
            Context context = new Context();
            context.putVar("books", books);
            context.putVar("createdAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JxlsHelper.getInstance().processTemplate(is, outputStream, context);
            byte[] excelContent = outputStream.toByteArray();
            return excelContent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isBookExistByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }
}
