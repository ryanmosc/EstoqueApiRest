package com.ryanmosc.ApiRestEstoque.exceptions;

public class ProductError extends  RuntimeException{
    public ProductError(){
        super("Erro inesperado");
    }

    public ProductError(String message){
        super(message);
    }
}
