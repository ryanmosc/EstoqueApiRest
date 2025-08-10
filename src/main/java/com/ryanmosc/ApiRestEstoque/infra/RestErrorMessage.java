package com.ryanmosc.ApiRestEstoque.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter

public class RestErrorMessage {
    private String message;
    private HttpStatus status;
    private int statusCode;
    private LocalDateTime date;
}
