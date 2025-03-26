package com.project.library.services.impl;

import com.project.library.dtos.BookDTO;
import com.project.library.dtos.search.BookSearchDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Category;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.exceptions.ImportExcelException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.CategoryRepository;
import com.project.library.repositories.UserRepository;
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
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Primary
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

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
    @Transactional
    public byte[] importBookExcelData(MultipartFile file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            boolean hasError = false;
            Sheet currentSheet = workbook.getSheetAt(0);
            Iterator<Row> rows = currentSheet.iterator();
            int startingRow = 4;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (currentRow.getRowNum() < startingRow) { //iterate until data rows
                    continue;
                }
                Book newBook = new Book();
                List<String> errors = new ArrayList<>();
                int cellIndex = 0;
                try {
                    newBook.setTitle(currentRow.getCell(cellIndex++).getStringCellValue());
                    newBook.setAuthor(currentRow.getCell(cellIndex++).getStringCellValue());

                    List<String> categoryNames = Arrays.stream(currentRow.getCell(cellIndex++)
                                    .getStringCellValue().split(","))
                            .toList();
                    List<Category> categories = categoryRepository.findByCategoryNameIn(categoryNames);
                    newBook.setCategories(categories);

                    newBook.setAmount((int) currentRow.getCell(cellIndex++).getNumericCellValue());
                    newBook.setLanguage(currentRow.getCell(cellIndex++).getStringCellValue());
                    newBook.setDescription(currentRow.getCell(cellIndex++).getStringCellValue());
                    newBook.setPageCount((int) currentRow.getCell(cellIndex++).getNumericCellValue());
                } catch (Exception e) {
                    errors.add(e.getMessage());
                    Cell errorCell = currentRow.getCell(cellIndex - 1);//if not empty but has error then set at the same index
                    if (errorCell == null) {
                        errorCell = currentRow.createCell(cellIndex - 1); //if empty cell then create cell at the same index and set error
                    }
                    errorCell.setCellValue(e.getMessage());
                    hasError = true;
                }
                if (errors.isEmpty()) {
                    bookRepository.save(newBook);
                }
            }
            if (hasError) {
                ByteArrayOutputStream os = new ByteArrayOutputStream(); //if has error then return file with http status of error
                workbook.write(os);
                throw new ImportExcelException("Import failed", os.toByteArray());
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os); //success
            return os.toByteArray();
        }
    }

    @Override
    public boolean isBookExistByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }
}
