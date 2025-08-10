package com.ryanmosc.ApiRestEstoque.exceptions;

public class ProductNameNotFound extends  RuntimeException {
    public ProductNameNotFound(){
        super("Product with name not found");
    }

    public ProductNameNotFound(String message){
        super(message);
    }
}
