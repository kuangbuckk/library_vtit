package com.project.library.services;

import com.project.library.dtos.BookDTO;
import com.project.library.dtos.search.BookSearchDTO;
import com.project.library.responses.BookPageResponse;
import com.project.library.responses.BookResponse;

import java.io.FileNotFoundException;
import java.util.UUID;

public interface BookService {
    BookPageResponse getAllBooks(int pageNumber, int size, BookSearchDTO bookSearchDTO);
    BookResponse getBookByCode(UUID code);
    BookResponse addBook(BookDTO bookDTO);
    BookResponse updateBook(BookDTO bookDTO, UUID code);
    BookResponse deleteBook(UUID code);
    void destroyBook(UUID code);

    byte[] exportBookExcelReport() throws FileNotFoundException;
    boolean isBookExistByTitle(String title);
}
