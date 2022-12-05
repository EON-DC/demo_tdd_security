package com.example.demo_tdd_security.order.exception;

public class NoSuchOrderException extends RuntimeException{

    public NoSuchOrderException(String message) { super(message);}
}
