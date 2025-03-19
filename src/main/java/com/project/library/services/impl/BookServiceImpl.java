package com.project.library.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library.dtos.BookDTO;
import com.project.library.dtos.search.BookSearchDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Category;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.CategoryRepository;
import com.project.library.responses.BookPageResponse;
import com.project.library.responses.BookResponse;
import com.project.library.services.interfaces.IBookService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookServiceImpl implements IBookService {
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
    public BookResponse getBookByCode(UUID code) {
        Book book = bookRepository
                .findById(code)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, code));
        return BookResponse.fromBook(book);
    }

    @Override
    @Transactional
    public BookResponse addBook(BookDTO bookDTO) {
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
        bookRepository.save(newBook);
        return BookResponse.fromBook(newBook);
    }

    @Override
    @Transactional
    public BookResponse updateBook(BookDTO bookDTO, UUID code) {
        List<UUID> categoryCodes = bookDTO.getCategoryCodes()
                .stream()
                .map(categoryCode -> UUID.fromString(categoryCode))
                .toList();
        List<Category> categories = categoryRepository.findCategoriesByCodeIn(categoryCodes);

        Book existingBook = bookRepository.findById(code)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, code));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setAmount(bookDTO.getAmount());
        existingBook.setCategories(categories);
        bookRepository.save(existingBook);

        return BookResponse.fromBook(existingBook);
    }

    @Override
    public BookResponse deleteBook(UUID code) {
        Book book = bookRepository.findById(code)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, code));
        book.setIsDeleted(true);
        bookRepository.save(book);
        return BookResponse.fromBook(book);
    }

    @Override
    @Transactional
    public void destroyBook(UUID code) {
        Book book = bookRepository.findById(code)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, code));
        bookRepository.delete(book);
    }

    @Override
    public byte[] exportBookExcelReport() throws FileNotFoundException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream() ) {
            Sheet sheet = workbook.createSheet("Data Sheet");
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = getHeaderStyle(workbook);

            List<Book> books = bookRepository.findAll();
            Field[] fields = books.get(0).getClass().getFields();

            for (int i = 0; i < fields.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fields[i].getName());  // Dynamically set column names
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowNum++);
                int columnNum = 0;
                row.createCell(columnNum++).setCellValue(book.getCode().toString());
                row.createCell(columnNum++).setCellValue(book.getTitle());
                row.createCell(columnNum++).setCellValue(book.getAuthor());
                row.createCell(columnNum++).setCellValue(book.getCreatedAt().toString());
                row.createCell(columnNum++).setCellValue(book.getUpdatedAt().toString());
                row.createCell(columnNum).setCellValue(book.getAuthor());
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel file", e);
        }
    }

    @Override
    public boolean isBookExistByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }

    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
