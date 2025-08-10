package com.ryanmosc.ApiRestEstoque.exceptions;

public class ProductNotfound extends RuntimeException{
    public ProductNotfound(){
        super("Product with id not found");
    }

    public ProductNotfound(String message){
        super(message);
    }

}
