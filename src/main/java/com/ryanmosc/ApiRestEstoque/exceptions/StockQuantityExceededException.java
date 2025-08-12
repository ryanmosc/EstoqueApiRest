package com.ryanmosc.ApiRestEstoque.exceptions;

public class StockQuantityExceededException extends RuntimeException {

    public StockQuantityExceededException( ) {
        super("Insufficient products quantity");
    }

    public StockQuantityExceededException(String message) {
        super(message);
    }
}
