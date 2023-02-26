package me.tud.weiner.lang.operation;

import me.tud.weiner.util.NumberUtil;

public final class DefaultOperations {

    static {

        Operations.registerOperation(Operator.ADDITION, Number.class, (left, right) -> {
            if (NumberUtil.isInteger(left, right))
                return left.longValue() + right.longValue();
            return left.doubleValue() + right.doubleValue();
        });
        Operations.registerOperation(Operator.SUBTRACTION, Number.class, (left, right) -> {
            if (NumberUtil.isInteger(left, right))
                return left.longValue() - right.longValue();
            return left.doubleValue() - right.doubleValue();
        });
        Operations.registerOperation(Operator.MULTIPLICATION, Number.class, (left, right) -> {
            if (NumberUtil.isInteger(left, right))
                return left.longValue() * right.longValue();
            return left.doubleValue() * right.doubleValue();
        });
        Operations.registerOperation(Operator.DIVISION, Number.class, (left, right) -> left.doubleValue() / right.doubleValue());

        Operations.registerOperation(Operator.EQUALS, Number.class, Number.class, Boolean.class, (left, right) -> NumberUtil.compare(left, right) == 0);
        Operations.registerOperation(Operator.NOT_EQUAL, Number.class, Number.class, Boolean.class, (left, right) -> NumberUtil.compare(left, right) != 0);
        Operations.registerOperation(Operator.LESS_THAN, Number.class, Number.class, Boolean.class, (left, right) -> NumberUtil.compare(left, right) < 0);
        Operations.registerOperation(Operator.LESS_THAN_EQUAL, Number.class, Number.class, Boolean.class, (left, right) -> NumberUtil.compare(left, right) <= 0);
        Operations.registerOperation(Operator.GREATER_THAN, Number.class, Number.class, Boolean.class, (left, right) -> NumberUtil.compare(left, right) > 0);
        Operations.registerOperation(Operator.GREATER_THAN_EQUAL, Number.class, Number.class, Boolean.class, (left, right) -> NumberUtil.compare(left, right) >= 0);
        Operations.registerDefaultValue(Number.class, () -> 0L);

        Operations.registerOperation(Operator.ADDITION, String.class, Object.class, (left, right) -> left + right, (left, right) -> left + right);
        Operations.registerOperation(Operator.SUBTRACTION, String.class, Long.class, (left, right) -> left.substring(0, Math.max(left.length() - right.intValue(), 0)));
        Operations.registerOperation(Operator.MULTIPLICATION, String.class, Long.class, (left, right) -> left.repeat(right.intValue()));
        Operations.registerDefaultValue(String.class, () -> "null");

        Operations.registerOperation(Operator.AND, Boolean.class, (left, right) -> left && right);
        Operations.registerOperation(Operator.OR, Boolean.class, (left, right) -> left || right);
        Operations.registerOperation(Operator.XOR, Boolean.class, (left, right) -> left ^ right);
        Operations.registerDefaultValue(Boolean.class, () -> false);
    }

}
