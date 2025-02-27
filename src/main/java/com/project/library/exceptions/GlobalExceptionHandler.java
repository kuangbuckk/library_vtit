package com.project.library.exceptions;

import com.project.library.components.LocalizationUtils;
import com.project.library.responses.GenericResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final LocalizationUtils localizationUtils;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<Object>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericResponse.error(e.getMessage(), localizationUtils.getLocalizedMessage(e.getMessage())));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GenericResponse<Object>> handleUsernameNotFoundExceptionException(final UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericResponse.error(e.getMessage(), localizationUtils.getLocalizedMessage(e.getMessage())));
    }


    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<GenericResponse<Object>> handleDataNotFoundException(final DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GenericResponse.error(e.getMessage(), localizationUtils.getLocalizedMessage(e.getMessage(), e.getCode())));
    }

    @ExceptionHandler(DataOutOfBoundException.class)
    public ResponseEntity<GenericResponse<Object>> handleDataOutOfBoundException(final DataOutOfBoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericResponse.error(e.getMessage(), localizationUtils.getLocalizedMessage(e.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Object>> exception(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GenericResponse.error(e.getMessage(), localizationUtils.getLocalizedMessage(e.getMessage())));
    }
}
