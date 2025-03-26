package com.project.library.services;

import com.project.library.dtos.BookDTO;
import com.project.library.dtos.search.BookSearchDTO;
import com.project.library.responses.BookPageResponse;
import com.project.library.responses.BookResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface BookService {
    BookPageResponse getAllBooks(int pageNumber, int size, BookSearchDTO bookSearchDTO);
    BookResponse getBookByCode(Long id);
    BookResponse addBook(BookDTO bookDTO);
    BookResponse updateBook(BookDTO bookDTO, Long id);
    BookResponse deleteBook(Long id);
    void destroyBook(Long id);

    byte[] exportBookExcelReport();
    byte[] importBookExcelData(MultipartFile file) throws IOException;
    boolean isBookExistByTitle(String title);
}
