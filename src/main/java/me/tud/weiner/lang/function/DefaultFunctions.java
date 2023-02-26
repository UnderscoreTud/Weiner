package me.tud.weiner.lang.function;

import me.tud.weiner.util.NumberUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultFunctions {

    static {
        Functions.registerFunction(new JavaFunction<>(new Signature("print")) {
            @Override
            public Object run(Object[] parameters) {
                for (Object parameter : parameters)
                    System.out.println(parameter);
                return null;
            }
        });

        Functions.registerFunction(new JavaFunction<Number>(new Signature("pow", "n1", "n2")) {
            @Override
            public Number run(Object[] parameters) {
                Number n1 = get(parameters[0], Number.class).orElse(0L);
                Number n2 = get(parameters[1], Number.class).orElse(0L);
                return Math.pow(n1.doubleValue(), n2.doubleValue());
            }
        });

        Functions.registerFunction(new JavaFunction<Number>(new Signature("rand", "from", "to")) {
            @Override
            public Number run(Object[] parameters) {
                Number n1 = get(parameters[0], Number.class).orElse(0L);
                Number n2 = get(parameters[1], Number.class).orElse(0L);
                if (NumberUtil.isInteger(n1, n2)) {
                    long min = Math.min(n1.longValue(), n2.longValue());
                    long max = Math.max(n1.longValue(), n2.longValue());
                    return ThreadLocalRandom.current().nextLong(min, max + 1);
                } else {
                    double min = Math.min(n1.doubleValue(), n2.doubleValue());
                    double max = Math.max(n1.doubleValue(), n2.doubleValue());
                    return ThreadLocalRandom.current().nextDouble(min, max + 1);
                }
            }
        });

        Functions.registerFunction(new JavaFunction<Number>(new Signature("sqrt", "n1")) {
            @Override
            public Number run(Object[] parameters) {
                Number n = get(parameters[0], Number.class).orElse(0L);
                return Math.sqrt(n.doubleValue());
            }
        });

        Functions.registerFunction(new JavaFunction<Number>(new Signature("square", "n1")) {
            @Override
            public Number run(Object[] parameters) {
                Number n = get(parameters[0], Number.class).orElse(0L);
                if (NumberUtil.isInteger(n)) {
                    long number = n.longValue();
                    return number * number;
                } else {
                    double number = n.doubleValue();
                    return number * number;
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> Optional<T> get(Object object, Class<T> returnType) {
        if (object == null || !returnType.isAssignableFrom(object.getClass()))
            return Optional.empty();
        return Optional.of((T) object);
    }

}
