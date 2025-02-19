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

    public static <T> GenericResponse<T> success(T data) {
        return GenericResponse.<T>builder()
                .code("200")
                .message("success")
                .data(data)
                .build();
    }

    public static <T> GenericResponse<T> error(String message) {
        return GenericResponse.<T>builder().code("500").message(message).data(null).build();
    }
}
