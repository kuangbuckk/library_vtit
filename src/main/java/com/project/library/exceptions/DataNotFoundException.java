package com.project.library.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Getter
public class DataNotFoundException extends RuntimeException {
    public UUID code;

    public DataNotFoundException(String message, UUID code) {
        super(message);
        this.code = code;
    }
}
