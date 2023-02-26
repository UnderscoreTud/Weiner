package me.tud.weiner.lang.operation;

import org.jetbrains.annotations.Nullable;

public enum Operator {

    ADDITION("+", Order.EXPRESSION),
    SUBTRACTION("-", Order.EXPRESSION),
    MULTIPLICATION("*", Order.TERM),
    DIVISION("/", Order.TERM),
    MODULO("%", Order.TERM),

    EQUALS("==", Order.EXPRESSION),
    NOT_EQUAL("!=", Order.EXPRESSION),
    LESS_THAN("<", Order.EXPRESSION),
    LESS_THAN_EQUAL("<=", Order.EXPRESSION),
    GREATER_THAN(">", Order.EXPRESSION),
    GREATER_THAN_EQUAL(">=", Order.EXPRESSION),

    AND("&&", Order.EXPRESSION),
    OR("||", Order.EXPRESSION),
    XOR("^", Order.EXPRESSION),
    ;

    public final String sign;
    public final Order order;

    Operator(String sign, Order order) {
        this.sign = sign;
        this.order = order;
    }

    public static @Nullable Operator bySign(String sign) {
        for (Operator operator : values()) {
            if (operator.sign.equals(sign))
                return operator;
        }
        return null;
    }

    public enum Order {
        EXPRESSION,
        TERM,
        FACTOR
    }

}
