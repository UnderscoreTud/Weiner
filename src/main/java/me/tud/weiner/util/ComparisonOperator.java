package me.tud.weiner.util;

import org.jetbrains.annotations.Nullable;

public enum ComparisonOperator {

    EQUAL("==") {
        @Override
        public boolean test(Number leftOperand, Number rightOperand) {
            return NumberUtil.compare(leftOperand, rightOperand) == 0;
        }
    },
    NOT_EQUAL("!=") {
        @Override
        public boolean test(Number leftOperand, Number rightOperand) {
            return NumberUtil.compare(leftOperand, rightOperand) != 0;
        }
    },
    LESS_THAN("<") {
        @Override
        public boolean test(Number leftOperand, Number rightOperand) {
            return NumberUtil.compare(leftOperand, rightOperand) < 0;
        }
    },
    LESS_THAN_EQUAL("<=") {
        @Override
        public boolean test(Number leftOperand, Number rightOperand) {
            return NumberUtil.compare(leftOperand, rightOperand) <= 0;
        }
    },
    GREATER_THAN(">") {
        @Override
        public boolean test(Number leftOperand, Number rightOperand) {
            return NumberUtil.compare(leftOperand, rightOperand) > 0;
        }
    },
    GREATER_THAN_EQUAL(">=") {
        @Override
        public boolean test(Number leftOperand, Number rightOperand) {
            return NumberUtil.compare(leftOperand, rightOperand) >= 0;
        }
    };

    private final String sign;

    ComparisonOperator(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public abstract boolean test(Number leftOperand, Number rightOperand);

    @Nullable
    public static ComparisonOperator bySign(String sign) {
        for (ComparisonOperator value : values()) {
            if (value.getSign().equals(sign))
                return value;
        }
        return null;
    }
}
