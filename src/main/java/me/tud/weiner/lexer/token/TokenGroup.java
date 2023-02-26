package me.tud.weiner.lexer.token;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public enum TokenGroup {

    ARITHMETIC_OPERATORS(TokenType.PLUS, TokenType.MINUS, TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MODULO),
    COMPARISON_OPERATORS(TokenType.COMPARISON),
    BOOLEAN_OPERATORS(TokenType.AND, TokenType.OR, TokenType.XOR),
    OPERATORS(ARITHMETIC_OPERATORS, COMPARISON_OPERATORS, BOOLEAN_OPERATORS),

    STATEMENT(TokenType.IF, TokenType.STATEMENT, TokenType.EOL),

    PAREN(TokenType.L_PAREN, TokenType.R_PAREN),
    BRACE(TokenType.L_BRACE, TokenType.R_BRACE),
    CURLY_BRACE(TokenType.L_CURLY_BRACE, TokenType.R_CURLY_BRACE),
    PUNCTUATION(PAREN, BRACE, CURLY_BRACE)
    ;

    @NotNull
    private final TokenType[] tokenTypes;

    TokenGroup(@NotNull TokenGroup tokenGroup, TokenGroup @NotNull ... tokenGroups) {
        List<TokenType> tokenTypes = new LinkedList<>();
        Collections.addAll(tokenTypes, tokenGroup.tokenTypes);
        for (TokenGroup group : tokenGroups)
            Collections.addAll(tokenTypes, group.tokenTypes);
        this.tokenTypes = tokenTypes.toArray(new TokenType[0]);
    }

    TokenGroup(TokenType @NotNull ... tokenTypes) {
        this.tokenTypes = tokenTypes;
    }

    public TokenType[] getTokenTypes() {
        return tokenTypes;
    }

    public boolean has(TokenType tokenType) {
        if (tokenType == null)
            return false;
        for (TokenType type : tokenTypes) {
            if (type == tokenType)
                return true;
        }
        return false;
    }

}
