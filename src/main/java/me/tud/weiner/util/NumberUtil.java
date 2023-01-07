package me.tud.weiner.util;

public class NumberUtil {

    public static boolean isDigit(char c, int radix) {
        return Character.digit(c, radix) >= 0;
    }

}
