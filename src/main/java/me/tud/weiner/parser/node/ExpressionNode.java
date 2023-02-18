package me.tud.weiner.parser.node;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ExpressionNode<T> extends ASTNode {

    @Override
    public final @Nullable T evaluate() {
        return get();
    }

    public abstract @Nullable T get();

    public final Optional<T> getOptional() {
        return Optional.ofNullable(get());
    }

    public abstract  Class<? extends T> getReturnType();

}
