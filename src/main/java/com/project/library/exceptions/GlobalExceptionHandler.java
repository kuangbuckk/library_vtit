package com.project.library.exceptions;

import com.project.library.utils.LocalizationUtils;
import com.project.library.responses.GenericResponse;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<Object>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericResponse.error(MessageKeys.ILLEGAL_INPUT_ARGUMENT, errors.toString()));
    }

    //BindingResult exception
    @ExceptionHandler(BindException.class)
    public ResponseEntity<GenericResponse<Object>> handleBindException(final BindException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toString();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericResponse.error(MessageKeys.ILLEGAL_INPUT_ARGUMENT, errors));
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

    @ExceptionHandler(InvalidOwnerException.class)
    public ResponseEntity<GenericResponse<Object>> handleInvalidOwnerException(final InvalidOwnerException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(GenericResponse.error(e.getMessage(), localizationUtils.getLocalizedMessage(e.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Object>> exception(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GenericResponse.error(e.getMessage(), e.getMessage()));
    }
}
