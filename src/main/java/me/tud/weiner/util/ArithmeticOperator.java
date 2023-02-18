package me.tud.weiner.util;

import org.jetbrains.annotations.Nullable;

public enum ArithmeticOperator {

    PLUS('+') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            boolean[] isInteger = {NumberUtil.isInteger(leftOperand), NumberUtil.isInteger(rightOperand)};
            if (isInteger[0] && isInteger[1]) {
                return leftOperand.longValue() + rightOperand.longValue();
            } else if (isInteger[0]) {
                return leftOperand.longValue() + rightOperand.doubleValue();
            } else if (isInteger[1]) {
                return leftOperand.doubleValue() + rightOperand.longValue();
            } else {
                return leftOperand.doubleValue() + rightOperand.doubleValue();
            }
        }
    },
    MINUS('-') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            boolean[] isInteger = {NumberUtil.isInteger(leftOperand), NumberUtil.isInteger(rightOperand)};
            if (isInteger[0] && isInteger[1]) {
                return leftOperand.longValue() - rightOperand.longValue();
            } else if (isInteger[0]) {
                return leftOperand.longValue() - rightOperand.doubleValue();
            } else if (isInteger[1]) {
                return leftOperand.doubleValue() - rightOperand.longValue();
            } else {
                return leftOperand.doubleValue() - rightOperand.doubleValue();
            }
        }
    },
    MULTIPLICATION('*') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            boolean[] isInteger = {NumberUtil.isInteger(leftOperand), NumberUtil.isInteger(rightOperand)};
            if (isInteger[0] && isInteger[1]) {
                return leftOperand.longValue() * rightOperand.longValue();
            } else if (isInteger[0]) {
                return leftOperand.longValue() * rightOperand.doubleValue();
            } else if (isInteger[1]) {
                return leftOperand.doubleValue() * rightOperand.longValue();
            } else {
                return leftOperand.doubleValue() * rightOperand.doubleValue();
            }
        }
    },
    DIVISION('/') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            return leftOperand.doubleValue() / rightOperand.doubleValue();
        }
    },
    MODULO('%') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            boolean[] isInteger = {NumberUtil.isInteger(leftOperand), NumberUtil.isInteger(rightOperand)};
            if (isInteger[0] && isInteger[1]) {
                return leftOperand.longValue() % rightOperand.longValue();
            } else if (isInteger[0]) {
                return leftOperand.longValue() % rightOperand.doubleValue();
            } else if (isInteger[1]) {
                return leftOperand.doubleValue() % rightOperand.longValue();
            } else {
                return leftOperand.doubleValue() % rightOperand.doubleValue();
            }
        }
    };

    private final char sign;

    ArithmeticOperator(char sign) {
        this.sign = sign;
    }

    public char getSign() {
        return sign;
    }

    public abstract Number calculate(Number leftOperand, Number rightOperand);

    @Nullable
    public static ArithmeticOperator bySign(String sign) {
        for (ArithmeticOperator value : values()) {
            if (sign.charAt(0) == value.getSign())
                return value;
        }
        return null;
    }

}
