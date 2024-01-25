package ru.andrew;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.andrew.exceptions.BadEncodingException;
import ru.andrew.exceptions.BadInputException;
import ru.andrew.exceptions.EmptyBodyException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionController {

    record Response(String message) {}

    @ExceptionHandler({
            BadEncodingException.class, BadInputException.class, EmptyBodyException.class
    })
    public ResponseEntity<Response> handleException(Exception e) {
        String message = String.format("%s %s", LocalDateTime.now(), e.getMessage());
        Response response = new Response(message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Response> handleException(HttpMediaTypeNotSupportedException e) {
        String message = String.format("%s %s", LocalDateTime.now(),
                "Неверный Content-Type, должен быть application/json");
        Response response = new Response(message);
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleException(HttpMessageNotReadableException e) {
        if (e.getMessage().contains("exceeds the maximum length")) {
            String message = String.format("%s %s", LocalDateTime.now(),
                    "Ваш запрос слишком длинный, максимальная длина запроса - 20000000 символов");
            Response response = new Response(message);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            String message = String.format("%s %s", LocalDateTime.now(),
                    "С вашим запросом что-то не так.");
            Response response = new Response(message);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
