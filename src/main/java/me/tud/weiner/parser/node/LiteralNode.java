package me.tud.weiner.parser.node;

import hu.webarticum.treeprinter.text.ConsoleText;
import org.jetbrains.annotations.NotNull;

public class LiteralNode<T> extends ExpressionNode<T> {

    @NotNull
    private final T value;

    public LiteralNode(@NotNull T value) {
        this.value = value;
    }

    @Override
    public final T get() {
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends T> getReturnType() {
        return (Class<? extends T>) value.getClass();
    }

    @Override
    public ConsoleText content() {
        return super.content().concat(ConsoleText.of(": " + value));
    }

}
