package com.microservice.articlesservice.exception;

public class InvalidArticlePriceValue extends RuntimeException{
    public InvalidArticlePriceValue(String s) {
        super(s);
    }
}
