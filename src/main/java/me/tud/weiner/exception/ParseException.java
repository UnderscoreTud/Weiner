package me.tud.weiner.exception;

import me.tud.weiner.lexer.token.Token;
import me.tud.weiner.lexer.token.TokenGroup;
import me.tud.weiner.lexer.token.TokenType;

public class ParseException extends WienerException {

    public ParseException(Token token) {
        this("Unexpected token: " + token, token.line(), token.pos());
    }

    public ParseException(WienerException e, Token token) {
        this(e.getMessage(), token.line(), token.pos());
    }

    public ParseException(String message, Token token) {
        this(message, token.line(), token.pos());
    }

    public ParseException(String message, int line, int pos) {
        super(message + String.format(" @%d:%d", line, pos));
    }

    public static ParseException expected(TokenType expected, Token got) {
        String expectedType = expected.name().toLowerCase();
        String gotType = got.type().name().toLowerCase();
        return new ParseException(String.format("Expected token type '%s', but got '%s' instead", expectedType, gotType), got);
    }

    public static ParseException expected(TokenGroup expected, Token got) {
        String expectedType = expected.name().toLowerCase();
        String gotType = got.type().name().toLowerCase();
        return new ParseException(String.format("Expected token type '%s', but got '%s' instead", expectedType, gotType), got);
    }

    public static ParseException expected(Class<?> expected, Class<?> got, Token token) {
        return new ParseException(String.format("Expected type '%s', but got '%s' instead", expected.getSimpleName(), got.getSimpleName()), token);
    }

    public static ParseException unexpected(Token unexpected) {
        return new ParseException("Unexpected token: " + unexpected.type(), unexpected);
    }

}
