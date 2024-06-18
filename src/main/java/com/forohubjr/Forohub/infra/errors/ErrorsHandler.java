package com.forohubjr.Forohub.infra.errors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity error404Handler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity error400Handler(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors().stream().map(ValidationErrorData::new).toList();
        return ResponseEntity.badRequest().body(errors);
    }

    private record ValidationErrorData(String field, String error){
        public ValidationErrorData(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
