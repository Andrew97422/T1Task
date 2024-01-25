package ru.andrew.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadEncodingException extends Exception {
    public BadEncodingException(String message) { super(message); }
}
