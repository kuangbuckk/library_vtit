package com.project.library.controllers;

import com.project.library.constants.FilenameTemplate;
import com.project.library.utils.LocalizationUtils;
import com.project.library.dtos.BookDTO;
import com.project.library.dtos.search.BookSearchDTO;
import com.project.library.responses.BookPageResponse;
import com.project.library.responses.BookResponse;
import com.project.library.services.BookService;
import com.project.library.utils.MessageKeys;
import com.project.library.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("${api.prefix}/books")
@AllArgsConstructor
public class BookController {
    //tuỳ chọn bean service nào nhưng chỉ dùng dc khi khai báo constructor manually
//    public BookController(@Qualifier("bookServiceImpl") BookService bookService) {
//        this.bookService = bookService;
//    }

    private final BookService bookService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<?> getBooks(
            @RequestParam(defaultValue = "0", name = "page_number") int pageNumber,
            @RequestParam(defaultValue = "5", name = "size") int size,
            @RequestBody BookSearchDTO bookSearchDTO
    ) {
        BookPageResponse bookPageResponse = bookService.getAllBooks(pageNumber, size, bookSearchDTO);
        return ResponseUtil.success(
                MessageKeys.GET_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.GET_BOOK_SUCCESSFULLY),
                bookPageResponse);
    }

//    @GetMapping("/{code}")
//    @PreAuthorize("permitAll()")
//    public ResponseEntity<GenericResponse<BookResponse>> getBookByCode(@PathVariable String code) {
//        BookResponse bookResponse = bookService.getBookByCode(UUID.fromString(code));
//        return ResponseEntity.ok(GenericResponse.success(bookResponse));
//
//    }

    @GetMapping("/excel-report")
    public ResponseEntity<?> downloadExcel() {
        byte[] excelFileData = bookService.exportBookExcelReport();
        return ResponseUtil.download("book_report_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx", excelFileData);
    }

    @PostMapping("/excel/import")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseUtil.download(FilenameTemplate.BOOK_EXCEL_NAME, bookService.importBookExcelData(file));
    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> createBook(
            @RequestBody @Valid BookDTO bookDTO,
            HttpServletRequest httpServletRequest
    ) {
        BookResponse newBookResponse = bookService.addBook(bookDTO);
        return ResponseUtil.success(
                MessageKeys.INSERT_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_BOOK_SUCCESSFULLY),
                newBookResponse
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> updateBook(
            @RequestBody @Valid BookDTO bookDTO,
            @PathVariable Long id,
            HttpServletRequest httpServletRequest
    ) {
        BookResponse updatedBook = bookService.updateBook(bookDTO, id);
        return ResponseUtil.success(
                MessageKeys.UPDATE_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_BOOK_SUCCESSFULLY),
                updatedBook
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> deleteBook(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        bookService.deleteBook(id);
        return ResponseUtil.success(
                MessageKeys.DELETE_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BOOK_SUCCESSFULLY),
                id
        );
    }

    @DeleteMapping("/destroy/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> destroyBook(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        bookService.destroyBook(id);
        return  ResponseUtil.success(
                MessageKeys.DESTROY_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DESTROY_BOOK_SUCCESSFULLY),
                id
        );
    }
}
