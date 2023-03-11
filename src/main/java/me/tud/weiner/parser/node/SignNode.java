package me.tud.weiner.parser.node;

import me.tud.weiner.util.NumberUtil;
import org.jetbrains.annotations.Nullable;

public class SignNode extends ExpressionNode<Number> {

    private final ExpressionNode<Number> number;
    private final boolean negate;

    public SignNode(ExpressionNode<Number> number, boolean negate) {
        this.number = number;
        this.negate = negate;
        setChildren(number);
    }

    @Override
    public @Nullable Number get() {
        Number object = number.get();

        if (object == null)
            return null;

        if (!negate)
            return object;

        if (NumberUtil.isInteger(object))
            return -object.longValue();

        return -object.doubleValue();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

}
