package com.project.library.exceptions;

import com.project.library.utils.LocalizationUtils;
import com.project.library.responses.GenericResponse;
import com.project.library.utils.MessageKeys;
import com.project.library.utils.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final LocalizationUtils localizationUtils;

    //BindingResult exception
    @ExceptionHandler(BindException.class)
    public ResponseEntity<GenericResponse> handleBindException(final BindException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toString();
        return ResponseUtil.error(
                MessageKeys.ILLEGAL_INPUT_ARGUMENT,
                errors,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GenericResponse> handleUsernameNotFoundExceptionException(final UsernameNotFoundException e) {
        return ResponseUtil.error(
                e.getMessage(),
                localizationUtils.getLocalizedMessage(e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<GenericResponse> handleDataNotFoundException(final DataNotFoundException e) {
        return ResponseUtil.error(
                e.getMessage(),
                localizationUtils.getLocalizedMessage(e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DataOutOfBoundException.class)
    public ResponseEntity<GenericResponse> handleDataOutOfBoundException(final DataOutOfBoundException e) {
        return ResponseUtil.error(
                e.getMessage(),
                localizationUtils.getLocalizedMessage(e.getMessage()),
                HttpStatus.BAD_GATEWAY
        );
    }

    @ExceptionHandler(InvalidOwnerException.class)
    public ResponseEntity<GenericResponse> handleInvalidOwnerException(final InvalidOwnerException e) {
        return ResponseUtil.error(
                e.getMessage(),
                localizationUtils.getLocalizedMessage(e.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<GenericResponse> handleBusinessException(final BusinessException e) {
        return ResponseUtil.error(
                e.getMessage(),
                localizationUtils.getLocalizedMessage(e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ImportExcelException.class)
    public ResponseEntity<byte[]> handleImportExcelException(final ImportExcelException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "error_excel.xlsx");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(e.getErrorFile());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> exception(Exception e) {
        return ResponseUtil.error(
                e.getMessage(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
