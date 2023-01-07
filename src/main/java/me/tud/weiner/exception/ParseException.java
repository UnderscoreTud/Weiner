package me.tud.weiner.exception;

public class ParseException extends RuntimeException {

    public ParseException(String message, int line, int pos) {
        super(message + String.format(" @%d:%d", line, pos));
    }

}
