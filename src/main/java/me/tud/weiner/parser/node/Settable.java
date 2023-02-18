package me.tud.weiner.parser.node;

public interface Settable<T> {

    void prepareSet(ExpressionNode<T> expression);

    void set(T value);

}
