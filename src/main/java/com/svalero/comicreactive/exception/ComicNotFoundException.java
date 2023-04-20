package com.svalero.comicreactive.exception;

public class ComicNotFoundException extends Exception{

    public ComicNotFoundException() {
        super("Comic not found");
    }

    public ComicNotFoundException(String message) {
        super(message);
    }
}
