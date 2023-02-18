package me.tud.weiner.parser.node;

import org.jetbrains.annotations.NotNull;

public abstract class ConditionNode extends ExpressionNode<Boolean> {

    public abstract boolean test();

    @Override
    public final @NotNull Boolean get() {
        return test();
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return boolean.class;
    }

}
