package com.project.library.services.interfaces;

import com.project.library.dtos.BookDTO;
import com.project.library.entities.Book;
import com.project.library.exceptions.DataNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IBookService {
    List<Book> getAllBooks();
    Book getBookByCode(UUID code);
    Book addBook(BookDTO bookDTO);
    Book updateBook(BookDTO bookDTO, UUID code);
    void deleteBook(UUID code);
}
