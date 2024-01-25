package ru.andrew.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmptyBodyException extends Exception {
    public EmptyBodyException(String message) {super(message);}
}
