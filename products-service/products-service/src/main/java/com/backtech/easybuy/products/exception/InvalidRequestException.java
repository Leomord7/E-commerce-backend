package com.backtech.easybuy.products.exception;

public class InvalidRequestException extends  RuntimeException{
    public InvalidRequestException(String msg){
        super(msg);
    }
}
