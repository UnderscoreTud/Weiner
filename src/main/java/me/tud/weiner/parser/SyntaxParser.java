package me.tud.weiner.parser;

import me.tud.weiner.exception.ParseException;
import me.tud.weiner.lang.Scope;
import me.tud.weiner.lang.operation.Operator;
import me.tud.weiner.lang.variable.VariableMap;
import me.tud.weiner.lexer.token.Token;
import me.tud.weiner.lexer.token.TokenGroup;
import me.tud.weiner.lexer.token.TokenType;
import me.tud.weiner.parser.node.*;
import me.tud.weiner.util.Dictionary;
import me.tud.weiner.util.NumberUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SyntaxParser {

    private final LinkedList<Token> tokens;
    private final Scope scope;
    private Token currentToken;
    private int pos = 0;
    private int mark = pos;

    public SyntaxParser(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.scope = new Scope();
        next();
    }

    // Grammar:
    // program: statement | statement program
    public ProgramNode program() {
        if (currentToken.is(TokenType.EOF))
            return new ProgramNode();

        StatementNode statement = statement();

        if (currentToken.is(TokenType.EOF))
            return new ProgramNode(statement);

        return new ProgramNode(statement, program());
    }

    // Grammar:
    // statement: control | function_declaration | assignment | statement_expression
    private StatementNode statement() {
        if (!currentToken.is(TokenType.IF, TokenType.EOL))
            eat(TokenType.STATEMENT);
        Token token = currentToken;

        if (token.is(TokenType.EOL)) {
            eat(TokenType.EOL);
            return new EmptyNode();
        }

        if (token.is(TokenType.IF) || (token.is(TokenType.KEYWORD) && (token.value(String.class).equals("when") || token.value(String.class).equals("for")))) {
            return control();
        } else if (token.is(TokenType.KEYWORD) && token.value(String.class).equals("function")) {
            return functionDeclaration();
        } else if (token.is(TokenType.KEYWORD) && token.value(String.class).equals("giveback")) {
            eat(TokenType.KEYWORD);
            mark();
            ExpressionNode<?> node = expression();
            eat(TokenType.EOL);
            return new GivebackNode(node);
        } else {
            mark();
            ExpressionNode<?> node = expression();
            if (currentToken.is(TokenType.ASSIGN, TokenType.ARROW)) {
                AssignmentNode assignment = assignment(node);
                eat(TokenType.EOL);
                return assignment;
            }
            if (!(node instanceof StatementExpression expr)) {
                reset();
                throw new ParseException(token);
            }
            eat(TokenType.EOL);
            return expr.asStatement();
        }
    }

    // Grammar:
    // control: "?" condition block ["else" "?" condition block] ["else" block]
    //          | "while" "(" condition ")" block
    //          | "for" "(" assignment ";" condition ";" expression ")" block
    private StatementNode control() {
        Token token = currentToken;
        if (currentToken.is(TokenType.KEYWORD)) {
            eat(TokenType.KEYWORD);
            if (token.value(String.class).equals("when")) {
                ConditionNode condition = condition();
                BlockNode block = block();
                return new WhileStatementNode(condition, block);
            } else {
                mark();
                ExpressionNode<?> first = expression();
                if (!(first instanceof LiteralNumberNode)) {
                    reset();
                    throw ParseException.unexpected(currentToken);
                }

                eat(TokenType.DOUBLE_DOT, true);
                if (currentToken.hasWhitespace())
                    throw ParseException.unexpected(currentToken);

                mark();
                ExpressionNode<?> second = expression();
                if (!(second instanceof LiteralNumberNode)) {
                    reset();
                    throw ParseException.unexpected(currentToken);
                }

                BlockNode block = block();

                return new ForStatementNode((LiteralNumberNode) first, (LiteralNumberNode) second, block);
            }
        } else {
            eat(TokenType.IF);
            ConditionNode condition = condition();
            BlockNode thenBlock = block();
            if (currentToken.is(TokenType.KEYWORD) && currentToken.value(String.class).equals("else")) {
                eat(TokenType.KEYWORD);

                if (currentToken.is(TokenType.IF))
                    return new IfStatementNode(condition, thenBlock, (IfStatementNode) control());

                BlockNode elseBlock = block();
                return new IfStatementNode(condition, thenBlock, elseBlock);
            } else {
                return new IfStatementNode(condition, thenBlock);
            }
        }
    }

    // Grammar:
    // condition: comparison | expression (boolean)
    public ConditionNode condition() {
        mark();
        ExpressionNode<?> expression = expression();
        if (expression instanceof ConditionNode)
            return (ConditionNode) expression;

        return new ConditionNode() {

            {
                setChildren(expression);
            }

            @Override
            public boolean test() {
                Object object = expression.get();
                if (object == null || !Boolean.class.isAssignableFrom(object.getClass()))
                    return false;
                return (boolean) object;
            }
        };
    }

    // Grammar:
    // condition: "{" [program] "}"
    public BlockNode block() {
        return block(true);
    }

    public BlockNode block(boolean increaseScope) {
        eat(TokenType.L_CURLY_BRACE);
        if (currentToken.is(TokenType.R_CURLY_BRACE)) {
            eat(TokenType.R_CURLY_BRACE);
            return new BlockNode();
        }

        if (increaseScope)
            scope.enter();

        List<StatementNode> statements = new LinkedList<>();
        while (!currentToken.is(TokenType.R_CURLY_BRACE))
            statements.add(statement());
        BlockNode block = new BlockNode(statements.toArray(new StatementNode[0]));

        if (increaseScope)
            scope.exit();

        eat(TokenType.R_CURLY_BRACE);
        return block;
    }

    // Grammar:
    // function_declaration: "function" identifier "(" [parameters] ")" block
    public FunctionDeclarationNode functionDeclaration() {
        eat(TokenType.KEYWORD);
        IdentifierNode identifier = identifier();

        eat(TokenType.L_PAREN, true);
        ParametersNode parameters;
        scope.enter(VariableMap.LENIENT);
        parameters = currentToken.is(TokenType.R_PAREN) ? new ParametersNode() : parameters();
        eat(TokenType.R_PAREN);

        BlockNode block = block(false);
        block.setReturnable(true);
        scope.exit();
        return new FunctionDeclarationNode(identifier, parameters, block);
    }

    // Grammar:
    // parameters: identifier | identifier "," parameters
    public ParametersNode parameters() {
        List<IdentifierNode> identifiers = new LinkedList<>();
        identifiers.add(identifier());
        while (currentToken.is(TokenType.COMMA)) {
            eat(TokenType.COMMA);
            identifiers.add(identifier());
        }
        return new ParametersNode(identifiers.toArray(new IdentifierNode[0]));
    }

    // Grammar:
    // function_call: identifier "(" [arguments] ")"
    public FunctionCallNode functionCall() {
        IdentifierNode identifier = identifier();

        eat(TokenType.L_PAREN, true);
        if (currentToken.is(TokenType.R_PAREN)) {
            eat(TokenType.R_PAREN);
            return new FunctionCallNode(identifier);
        }
        ArgumentsNode arguments = arguments();
        eat(TokenType.R_PAREN);
        return new FunctionCallNode(identifier, arguments);
    }

    // Grammar:
    // arguments: expression | expression "," arguments
    public ArgumentsNode arguments() {
        List<ExpressionNode<?>> expressions = new LinkedList<>();
        expressions.add(expression());
        while (currentToken.is(TokenType.COMMA)) {
            eat(TokenType.COMMA);
            expressions.add(expression());
        }
        return new ArgumentsNode(expressions.toArray(new ExpressionNode[0]));
    }

    // Grammar:
    // assignment: expression "<-" expression
    public AssignmentNode assignment(ExpressionNode<?> expression) {
        if (!(expression instanceof Settable<?>)) {
            reset();
            throw ParseException.unexpected(currentToken);
        }
        if (currentToken.is(TokenType.ARROW)) {
            eat(TokenType.ARROW);
            return new AssignmentNode((Settable<?>) expression, null);
        }
        eat(TokenType.ASSIGN);
        ExpressionNode<?> changeTo = expression();
        return new AssignmentNode((Settable<?>) expression, changeTo);
    }

    // Grammar:
    // expression: term | expression "+" term | expression "-" term
    public ExpressionNode<?> expression() {
        ExpressionNode<?> expression = term();
        while (currentToken.is(TokenGroup.OPERATORS)) {
            Operator operator = Operator.bySign(currentToken.value(String.class));
            assert operator != null;
            if (operator.order != Operator.Order.EXPRESSION)
                break;
            Token token = currentToken;
            eat(token.type());
            expression = new OperationNode(expression, operator, term());
        }

        while (currentToken.is(TokenType.L_BRACE)) {
            eat(TokenType.L_BRACE, true);
            mark();
            ExpressionNode<String> key = convert(expression(), String.class);
            eat(TokenType.R_BRACE);
            expression = new DictIndexNode(convert(expression, Dictionary.class), key);
        }

        return expression;
    }

    // Grammar:
    // term: factor | term "*" factor | term "/" factor | term "%" factor
    public ExpressionNode<?> term() {
        ExpressionNode<?> term = factor();
        while (currentToken.is(TokenGroup.OPERATORS)) {
            Operator operator = Operator.bySign(currentToken.value(String.class));
            assert operator != null;
            if (operator.order != Operator.Order.TERM) {
                break;
            }
            Token token = currentToken;
            eat(token.type());
            term = new OperationNode(term, Operator.bySign(token.value(String.class)), factor());
        }
        return term;
    }

    // Grammar:
    // factor: "+" factor | "-" factor | "!" factor | "(" expression ")" | number | identifier | function_call
    public ExpressionNode<?> factor() {
        Token token = currentToken;

        if (token.is(TokenType.PLUS, TokenType.MINUS) && !token.hasWhitespace()) {
            eat(token.type());
            return new SignNode(convert(factor(), Number.class), token.is(TokenType.MINUS));
        } else if (token.is(TokenType.NEGATE)) {
            eat(TokenType.NEGATE);
            return new NegateNode(convert(factor(), Boolean.class));
        } else if (token.is(TokenType.L_PAREN)) {
            eat(TokenType.L_PAREN);
            ExpressionNode<?> expression = expression();
            eat(TokenType.R_PAREN);
            return expression;
        } else if (token.is(TokenType.NUMBER)) {
            eat(TokenType.NUMBER);
            return new LiteralNumberNode(token.value(Number.class));
        } else if (token.is(TokenType.STRING)) {
            eat(TokenType.STRING);
            return new LiteralStringNode(token.value(String.class));
        } else if (token.is(TokenType.BOOLEAN)) {
            eat(TokenType.BOOLEAN);
            return new LiteralBooleanNode(token.value(boolean.class));
        } else if (token.is(TokenType.IDENTIFIER)) {
            if (peekTokenType() == TokenType.L_PAREN && !peek().hasWhitespace()) {
                return functionCall();
            } else {
                return new VariableNode(identifier());
            }
        } else if (token.is(TokenType.INDEX)) {
            eat(TokenType.INDEX);
            if (currentToken.is(TokenType.NUMBER)) {
                Number number = currentToken.value(Number.class);
                if (!NumberUtil.isInteger(number) || number.longValue() < 1)
                    throw ParseException.unexpected(currentToken);
                eat(TokenType.NUMBER);
                return new IndexNode(number.intValue());
            } else {
                return new IndexNode();
            }
        } else if (token.is(TokenType.MODULO)) {
            eat(TokenType.MODULO);
            eat(TokenType.L_CURLY_BRACE, true);
            HashMap<String, ExpressionNode<?>> map = new HashMap<>();
            while (true) {
                eat(TokenType.EOL);
                mark();
                String key = currentToken.value(String.class);
                eat(TokenType.STRING);
                if (map.containsKey(key)) {
                    reset();
                    throw new ParseException(String.format("Key '%s' already exists", key), currentToken);
                }
                eat(TokenType.COLON);
                ExpressionNode<?> expression = expression();
                map.put(key, expression);
                if (!currentToken.is(TokenType.COMMA))
                    break;
                eat(TokenType.COMMA);
            }
            eat(TokenType.EOL);
            eat(TokenType.R_CURLY_BRACE);
            return new DictionaryNode(map);
        } else {
            reset();
            throw ParseException.unexpected(currentToken);
        }
    }

    private TokenType peekTokenType() {
        return peek().type();
    }

    private Object peekTokenValue() {
        return peek().value();
    }

    @SuppressWarnings("unchecked")
    private <T> T peekTokenValue(Class<T> expectedReturnType) {
        return (T) peek().value();
    }

    private TokenType previousTokenType() {
        Token token = tokens.get(pos - 1);
        return token == null ? null : token.type();
    }

    private IdentifierNode identifier() {
        Token token = currentToken;
        eat(TokenType.IDENTIFIER);
        return new IdentifierNode(token.value(String.class));
    }

    private void eat(TokenType tokenType) {
        eat(tokenType, false);
    }

    private void eat(TokenType tokenType, boolean noWhitespace) {
        if (currentToken.is(tokenType)) {
            if (noWhitespace && currentToken.hasWhitespace())
                throw ParseException.unexpected(currentToken);
            next();
        } else {
            throw ParseException.expected(tokenType, currentToken);
        }
    }

    private void eat(TokenGroup tokenGroup) {
        if (currentToken.is(tokenGroup)) {
            next();
        } else {
            throw ParseException.expected(tokenGroup, currentToken);
        }
    }

    private void next() {
        currentToken = tokens.get(pos++);
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private void mark() {
        mark = pos;
    }

    private void reset() {
        pos = mark;
        currentToken = tokens.get(pos - 1);
    }

    private <T> ExpressionNode<T> convert(ExpressionNode<?> expression, Class<T> to) {
        return new ConvertedExpressionNode<>(expression, to);
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public LinkedList<Token> getTokens() {
        return tokens;
    }

    public Scope getScope() {
        return scope;
    }

    public int getPos() {
        return pos;
    }

    public int getMark() {
        return mark;
    }

}
