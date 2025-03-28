package com.project.library.constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FilenameTemplate {
    public static String BOOK_EXCEL_NAME = "book_report_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx";
    public static String USER_EXCEL_NAME = "users_excel_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx";
}
