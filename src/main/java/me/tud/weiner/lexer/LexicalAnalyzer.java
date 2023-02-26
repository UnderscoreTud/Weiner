package me.tud.weiner.lexer;

import me.tud.weiner.exception.ParseException;
import me.tud.weiner.lexer.token.Token;
import me.tud.weiner.lexer.token.TokenType;
import me.tud.weiner.util.NumberUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class LexicalAnalyzer {

    private static final String[] KEYWORDS = {"else", "when", "giveback", "for", "progress", "leave", "function"};

    private final String data;
    private final LinkedList<String> lines;
    private String line;
    private int lineIndex = 1;
    private int index = 0;
    private int cachedLineIndex = lineIndex;
    private int cachedIndex = index;
    private boolean whitespace = false;

    public LexicalAnalyzer(@NotNull InputStream inputStream) throws IOException {
        this(new String(inputStream.readAllBytes()));
    }

    public LexicalAnalyzer(@NotNull String data) {
        this.data = data;
        this.lines = data.lines().collect(Collectors.toCollection(LinkedList::new));
        this.line = lines.isEmpty() ? "" : lines.get(0);
    }

    public LinkedList<Token> lex() {
        LinkedList<Token> tokenList = new LinkedList<>();
        Token token;
        do {
            token = nextToken();
            whitespace = false;
            tokenList.add(token);
        } while (!token.is(TokenType.EOF));
        tokenList.add(tokenList.size() - 1, newToken(TokenType.EOL));
//        System.out.println(tokenList);
        return tokenList;
    }

    private Token nextToken() {
        if (!hasNext())
            return newToken(TokenType.EOF);

        char current = next();

        while (Character.isWhitespace(current) && current != '\n') {
            whitespace = true;
            current = next();
        }
        mark();

        switch (current) {
            case '\n':
                return newToken(TokenType.EOL);
            case '{':
                return newToken(TokenType.L_CURLY_BRACE, "{");
            case '}':
                return newToken(TokenType.R_CURLY_BRACE, "}");
            case '(':
                return newToken(TokenType.L_PAREN, "(");
            case ')':
                return newToken(TokenType.R_PAREN, ")");
            case '[':
                return newToken(TokenType.L_BRACE, "[");
            case ']':
                return newToken(TokenType.R_BRACE, "]");
            case '+':
            case '-':
            case '*':
            case '/':
            case '=':
            case '<':
            case '>':
            case '^':
            case '&':
            case '|':
            case '%':
            case '!':
                return handleOperator();
            case '?':
                return newToken(TokenType.IF, "?");
            case '$':
                return newToken(TokenType.STATEMENT, "$");
            case ':':
                return newToken(TokenType.COLON, ":");
            case '@':
                return newToken(TokenType.INDEX, "@");
            case '"':
                return handleQuotedString();
            case '.':
                if (peek() == '.') {
                    next();
                    return newToken(TokenType.DOUBLE_DOT);
                }
                break;
            case ',':
                return newToken(TokenType.COMMA);
        }

        if (Character.isDigit(current) || current == '.')
            return handleNumber();

        Token token = null;

        mark();
        if (current == 't' || current == 'f') {
            token = handleBoolean();
        }

        if (token == null) {
            reset();
            token = handleKeyword();
        }

        if (token == null) {
            reset();
            token = handleIdentifier();
        }

        if (token != null)
            return token;

        reset();
        throw parseException();
    }

    private @NotNull Token handleOperator() {
        char current = current();
        char nextChar = peek();

        if (current == nextChar) {
            // Double char
            next();
            switch (current) {
                case '=':
                    return newToken(TokenType.COMPARISON, "==");
                case '&':
                    return newToken(TokenType.AND, "&&");
                case '|':
                    return newToken(TokenType.OR, "||");
                default:
                    previous();
            }
        }

        if ((current == '!' || current == '<' || current == '>') && nextChar == '=') {
            next();
            return newToken(TokenType.COMPARISON, new String(new char[]{current, nextChar}));
        } else if (current == '-' && nextChar == '>') {
            next();
            return newToken(TokenType.ARROW, "->");
        } else if (current == '<' && nextChar == '-') {
            next();
            return newToken(TokenType.ASSIGN, "<-");
        }

        // Single char
        return switch (current) {
            case '+' -> newToken(TokenType.PLUS, "+");
            case '-' -> newToken(TokenType.MINUS, "-");
            case '*' -> newToken(TokenType.MULTIPLY, "*");
            case '/' -> newToken(TokenType.DIVIDE, "/");
            case '<' -> newToken(TokenType.COMPARISON, "<");
            case '>' -> newToken(TokenType.COMPARISON, ">");
            case '^' -> newToken(TokenType.XOR, "^");
            case '%' -> newToken(TokenType.MODULO, "%");
            case '!' -> newToken(TokenType.NEGATE, "!");
            default -> throw parseException();
        };
    }

    private @NotNull Token handleQuotedString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean escaped = false;
        while (hasNext(true)) {
            char current = next();
            if (current == '\\' && !escaped) {
                escaped = true;
                continue;
            }

            if (current == '"' && !escaped)
                return newToken(TokenType.STRING, stringBuilder.toString());

            escaped = false;
            stringBuilder.append(current);
        }
        throw parseException("Unterminated quoted literal");
    }

    private @NotNull Token handleNumber() {
        char current = current();
        StringBuilder stringBuilder = new StringBuilder();
        boolean integer = true;

        while (true) {
            if (current == '.') {
                if (peek() == '.') {
                    previous();
                    break;
                }

                if (integer) {
                    integer = false;
                } else {
                    throw parseException();
                }
            }

            stringBuilder.append(current);

            if (peek() != '.' && !NumberUtil.isDigit(peek(), 10))
                break;

            current = next();
        }

        String string = stringBuilder.toString();

        Number number;
        if (integer) {
            number = Long.parseLong(string);
        } else {
            number = Double.parseDouble(string);
        }
        return newToken(TokenType.NUMBER, number);
    }

    private @Nullable Token handleBoolean() {
        String data = readDelimiter("true", "false");
        if (data == null)
            return null;
        return newToken(TokenType.BOOLEAN, Boolean.parseBoolean(data));
    }

    private @Nullable Token handleKeyword() {
        String data = readDelimiter(KEYWORDS);
        if (data == null)
            return null;
        return newToken(TokenType.KEYWORD, data);
    }

    private @Nullable Token handleIdentifier() {
        String data = readDelimiter((c, pos) -> {
            if (Character.isAlphabetic(c) || c == '_')
                return true;
            return pos != 0 && Character.isDigit(c);
        });
        if (data == null)
            return null;
        return newToken(TokenType.IDENTIFIER, data);
    }

    private String readDelimiter(String... delimiters) {
        return readDelimiter((c, index) -> Character.isAlphabetic(c), delimiters);
    }

    private @Nullable String readDelimiter(@NotNull BiPredicate<Character, Integer> predicate, String... delimiters) {
        char current = current();
        if (!predicate.test(current, 0))
            return null;
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(current));
        current = peek();
        while (hasNext(true) && predicate.test(current, (index - cachedIndex) + 1)) {
            stringBuilder.append(next());
            current = peek();
        }
        String data = stringBuilder.toString();

        if (delimiters.length == 0)
            return data;

        for (String delimiter : delimiters) {
            if (data.equals(delimiter))
                return data;
        }

        return null;
    }

    private char previous() {
        index -= 1;
        if (index < 0) {
            lineIndex--;
            String line = lines.get(lineIndex - 1);
            return line.charAt(line.length() - 1);
        }
        return current();
    }

    private char current() {
        int index = this.index - 1;
        if (index < 0) {
            if (lineIndex <= 1)
                return 0;
            String line = lines.get(lineIndex - 2);
            return line.charAt(line.length() - 1);
        }
        return line.charAt(index);
    }

    private char next() {
        if (!hasNext(true)) {
            handleNewLine();
            return '\n';
        }
        return line.charAt(index++);
    }

    private char peek() {
        if (index >= line.length())
            return 0;
        return line.charAt(index);
    }

    private void handleNewLine() {
        index = 0;
        lineIndex++;
        line = lines.get(lineIndex - 1);
    }

    private boolean hasNext(boolean currentLine) {
        return index < this.line.length() || (!currentLine && lineIndex < lines.size());
    }

    private boolean hasNext() {
        return hasNext(false);
    }

    private void mark() {
        cachedLineIndex = lineIndex;
        cachedIndex = index;
    }

    private void reset() {
        lineIndex = cachedLineIndex;
        index = cachedIndex;
    }

    @Contract(" -> new")
    private @NotNull ParseException parseException() {
        return parseException(String.format("Unexpected character '%s'", current()));
    }

    @Contract("_ -> new")
    private @NotNull ParseException parseException(String message) {
        return new ParseException(message, lineIndex, index + 1);
    }

    @Contract("_, _ -> new")
    private @NotNull Token newToken(TokenType type, Object value) {
        return new Token(type, value, cachedLineIndex, cachedIndex + 1, whitespace);
    }

    @Contract("_ -> new")
    private @NotNull Token newToken(TokenType type) {
        return new Token(type, cachedLineIndex, cachedIndex + 1, whitespace);
    }

    public String getData() {
        return data;
    }

}
