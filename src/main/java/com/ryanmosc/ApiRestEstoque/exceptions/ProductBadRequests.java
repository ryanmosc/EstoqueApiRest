package com.ryanmosc.ApiRestEstoque.exceptions;

public class ProductBadRequests extends RuntimeException{
    public ProductBadRequests(){
        super("Erro inesperado");
    }

    public ProductBadRequests(String message){
        super(message);
    }
}
