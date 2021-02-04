package com.microservice.articlesservice.exception;

public class ArticleIntrouvableException extends RuntimeException {
    public ArticleIntrouvableException(String s) {
        super(s);
    }
}
