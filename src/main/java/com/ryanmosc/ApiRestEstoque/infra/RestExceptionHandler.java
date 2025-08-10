package com.ryanmosc.ApiRestEstoque.infra;

import com.ryanmosc.ApiRestEstoque.exceptions.ProductBadRequests;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductError;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductNameNotFound;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductNotfound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ProductNotfound.class)
    private ResponseEntity<RestErrorMessage> produtNotFound(ProductNotfound exception){
        RestErrorMessage threatResponse = new RestErrorMessage(
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(ProductNameNotFound.class)
    private ResponseEntity<RestErrorMessage> produtNameNotFound(ProductNameNotFound exception){
        RestErrorMessage threatResponse = new RestErrorMessage(
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(ProductError.class)
    private ResponseEntity<RestErrorMessage> productError(ProductError exception){
        RestErrorMessage threatResponse = new RestErrorMessage(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(threatResponse);
    }

    @ExceptionHandler(ProductBadRequests.class)
    private ResponseEntity<RestErrorMessage> handleBadRequest(ProductBadRequests exception) {
        RestErrorMessage threatResponse = new RestErrorMessage(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }
}




