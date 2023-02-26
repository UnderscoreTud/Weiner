package me.tud.weiner.util;

public class NumberUtil {

    public static boolean isDigit(char c, int radix) {
        return Character.digit(c, radix) >= 0;
    }

    public static int compare(Number leftOperand, Number rightOperand) {
        boolean[] isInteger = {isInteger(leftOperand), isInteger(rightOperand)};
        if (isInteger[0] && isInteger[1]) {
            return Long.compare(leftOperand.longValue(), rightOperand.longValue());
        } else if (isInteger[0]) {
            return (leftOperand.longValue() < rightOperand.doubleValue()) ? -1 : ((leftOperand.longValue() == rightOperand.doubleValue()) ? 0 : 1);
        } else if (isInteger[1]) {
            return (leftOperand.doubleValue() < rightOperand.longValue()) ? -1 : ((leftOperand.doubleValue() == rightOperand.longValue()) ? 0 : 1);
        } else {
            return Double.compare(leftOperand.doubleValue(), rightOperand.doubleValue());
        }
    }

    public static boolean isInteger(Number... numbers) {
        for (Number number : numbers) {
            if (number.longValue() != number.doubleValue())
                return false;
        }
        return true;
    }

    @SafeVarargs
    public static boolean isInteger(Class<? extends Number>... numbers) {
        for (Class<? extends Number> number : numbers) {
            if (Double.class.isAssignableFrom(number) || Float.class.isAssignableFrom(number))
                return false;
        }
        return true;
    }

}
