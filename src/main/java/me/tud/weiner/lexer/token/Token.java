package me.tud.weiner.lexer.token;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Token(TokenType type, Object value, int line, int pos) {

    public Token(TokenType type, int line, int pos) {
        this(type, null, line, pos);
    }

    public boolean is(TokenType type) {
        return this.type == type;
    }

    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        if (is(TokenType.STRING))
            return "[" + type + ": \"" + value + "\"]";
        return "[" + type + (value == null ? "" : ": " + value) + ']';
    }

}
