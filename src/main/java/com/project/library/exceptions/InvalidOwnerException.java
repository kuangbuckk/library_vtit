package com.project.library.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InvalidOwnerException extends RuntimeException {
    private Long id;
    public InvalidOwnerException(String message) {
        super(message);
    }
}
