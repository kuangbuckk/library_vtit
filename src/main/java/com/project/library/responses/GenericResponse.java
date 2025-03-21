package com.project.library.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> GenericResponse<T> success(String code, String message, T data) {
        return GenericResponse.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> GenericResponse<T> success(T data) {
        return GenericResponse.<T>builder()
                .code("200")
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> GenericResponse<T> error(String code, String message) {
        return GenericResponse.<T>builder().code(code).message(message).data(null).build();
    }

    public static <T> GenericResponse<byte[]> download(byte[] file) {
        return GenericResponse.<byte[]>builder()
                .code("200")
                .message("Success")
                .data(file)
                .build();
    }
}
