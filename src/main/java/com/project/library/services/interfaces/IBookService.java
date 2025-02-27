package com.project.library.services.interfaces;

import com.project.library.dtos.BookDTO;
import com.project.library.entities.Book;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.responses.BookPageResponse;
import com.project.library.responses.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IBookService {
    BookPageResponse getAllBooks(int pageNumber, int size, String keyword);
    BookResponse getBookByCode(UUID code);
    BookResponse addBook(BookDTO bookDTO);
    BookResponse updateBook(BookDTO bookDTO, UUID code);
    void deleteBook(UUID code);
}
