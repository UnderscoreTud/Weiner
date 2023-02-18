package me.tud.weiner.parser.node;

import org.jetbrains.annotations.Nullable;

public class NegateNode extends ExpressionNode<Boolean> {

    private final ExpressionNode<?> expression;

    public NegateNode(ExpressionNode<?> expression) {
        this.expression = expression;
        setChildren(expression);
    }

    @Override
    public @Nullable Boolean get() {
        return Boolean.FALSE.equals(expression.get());
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

}
