package me.tud.weiner.lexer.token;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Token(TokenType type, Object value, int line, int pos, boolean whitespace) {

    public Token(TokenType type, int line, int pos, boolean whitespace) {
        this(type, null, line, pos, whitespace);
    }

    public boolean is(TokenType type, TokenType... types) {
        if (this.type == type)
            return true;
        for (TokenType otherType : types) {
            if (this.type == otherType)
                return true;
        }
        return false;
    }

    public boolean is(TokenGroup group, TokenGroup... groups) {
        if (group.has(type))
            return true;
        for (TokenGroup otherGroup : groups) {
            if (otherGroup.has(type))
                return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T value(Class<T> returnType) {
        return (T) value;
    }

    public boolean hasWhitespace() {
        return whitespace;
    }

    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        if (is(TokenType.STRING))
            return "[" + type + ": \"" + value + "\"]";
        return "[" + type + (value == null ? "" : ": " + value) + ']';
    }

}
