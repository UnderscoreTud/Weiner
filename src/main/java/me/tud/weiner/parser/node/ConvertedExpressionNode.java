package me.tud.weiner.parser.node;

import hu.webarticum.treeprinter.text.ConsoleText;
import me.tud.weiner.exception.ParseException;
import org.jetbrains.annotations.Nullable;

public class ConvertedExpressionNode<T> extends ExpressionNode<T> implements Settable<Object> {

    private final ExpressionNode<?> expression;
    private final Class<T> to;

    public ConvertedExpressionNode(ExpressionNode<?> expression, Class<T> to) {
        this.expression = expression;
        this.to = to;
        setChildren(expression.getChildren());
    }

    @Override
    public void preLoad() {
        expression.preLoad();
    }

    @Override
    public void load() {
        expression.load();
    }

    @Override
    public void init() {
        expression.init();
        Class<?> returnType = expression.getReturnType();
        if (returnType != Object.class && !to.isAssignableFrom(returnType))
            throw ParseException.expected(to, returnType, getParser().getCurrentToken());
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable T get() {
        Object object = expression.get();
        if (object == null || !to.isAssignableFrom(object.getClass()))
            return null;
        return (T) object;
    }

    @Override
    public Class<? extends T> getReturnType() {
        return to;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void prepareSet(ExpressionNode<Object> expression) {
        if (!(expression instanceof Settable<?>))
            throw ParseException.unexpected(getParser().getCurrentToken());
        ((Settable) expression).prepareSet(expression);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void set(Object value) {
        ((Settable) expression).set(value);
    }

    @Override
    protected void setParent(@Nullable ASTNode parent) {
        expression.setParent(parent);
    }

    @Override
    public ConsoleText content() {
        return expression.content();
    }

    @Override
    protected String getName() {
        return expression.getName();
    }

}
