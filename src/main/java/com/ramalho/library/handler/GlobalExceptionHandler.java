package com.ramalho.library.handler;

import com.ramalho.library.exception.ErroResponse;
import com.ramalho.library.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErroResponse> handleNotFoundException(NotFoundException e) {
        ErroResponse erroResponse = ErroResponse.builder()
        .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
        .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErroResponse> handleException(Exception e) {
        ErroResponse erroResponse = ErroResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroResponse);
    }
}
