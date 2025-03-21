package com.project.library.utils;

import com.project.library.responses.GenericResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static <T> ResponseEntity<GenericResponse<T>> success(String code, String message, T data) {
        GenericResponse<T> response = GenericResponse.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<GenericResponse> error(String code, String message) {
        GenericResponse response = GenericResponse.builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public static <T> ResponseEntity<?> download(String filename, byte[] fileData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileData);
    }
}
