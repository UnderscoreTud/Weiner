package me.tud.weiner.lang.operation;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class Operations {

    private static final Map<Operator, List<OperationInfo<?, ?, ?>>> REGISTERED_OPERATIONS = new HashMap<>();
    private static final Map<Class<?>, Object> DEFAULT_VALUES = new HashMap<>();

    private Operations() {
        throw new UnsupportedOperationException();
    }

    public static <T> void registerOperation(Operator operator, Class<T> type, Operation<T, T, T> operation) {
        registerOperation(operator, type, type, operation);
    }

    public static <L, R> void registerOperation(Operator operator, Class<L> left, Class<R> right, Operation<L, R, L> operation) {
        registerOperation(operator, left, right, left, operation);
    }

    public static <L, R> void registerOperation(Operator operator, Class<L> left, Class<R> right, Operation<L, R, L> operation, Operation<R, L, L> commutativeOperation) {
        registerOperation(operator, left, right, left, operation, commutativeOperation);
    }

    public static <L, R, T> void registerOperation(Operator operator, Class<L> left, Class<R> right, Class<T> returnType, Operation<L, R, T> operation, Operation<R, L, T> commutativeOperation) {
        registerOperation(operator, left, right, returnType, operation);
        registerOperation(operator, right, left, returnType, commutativeOperation);
    }

    public static <L, R, T> void registerOperation(Operator operator, Class<L> left, Class<R> right, Class<T> returnType, Operation<L, R, T> operation) {
        if (findOperation(operator, left, right) != null)
            throw new IllegalArgumentException("An operator is already registered with the types '" + left + "' and '" + right + '\'');
        REGISTERED_OPERATIONS.computeIfAbsent(operator, op -> Collections.synchronizedList(new ArrayList<>()))
                .add(new OperationInfo<>(left, right, returnType, operation));
    }

    public static List<OperationInfo<?, ?, ?>> getOperations(Operator operator) {
        return REGISTERED_OPERATIONS.getOrDefault(operator, Collections.emptyList());
    }

    public static List<OperationInfo<?, ?, ?>> getOperations(Operator operator, Class<?> type) {
        return getOperations(operator).stream()
                .filter(handler -> handler.getLeftClass().isAssignableFrom(type))
                .collect(Collectors.toList());
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <L, R> OperationInfo<L, R, ?> findOperation(Operator operator, Class<L> left, Class<R> right) {
        return (OperationInfo<L, R, ?>) getOperations(operator).stream()
                .filter(handler -> handler.getLeftClass().isAssignableFrom(left) && handler.getRightClass().isAssignableFrom(right))
                .findFirst().orElse(null);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <L, R, T> OperationInfo<L, R, T> findOperation(Operator operator, Class<L> left, Class<R> right, Class<T> returnType) {
        return (OperationInfo<L, R, T>) getOperations(operator).stream()
                .filter(handler ->
                        handler.getLeftClass().isAssignableFrom(left)
                                && handler.getRightClass().isAssignableFrom(right)
                                && handler.getReturnClass().isAssignableFrom(returnType))
                .findFirst().orElse(null);
    }

    @Nullable
    public static Class<?> lookupClass(Operator operator, Class<?> to) {
        List<OperationInfo<?, ?, ?>> operationInfos = getOperations(operator, to);
        if (operationInfos.size() == 0)
            return null;
        OperationInfo<?, ?, ?> operation = findOperation(operator, to, to);

        if (operation == null) {
            operation = operationInfos.get(0);
            return operation.getRightClass();
        } else {
            return to;
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <L, R, T> T calculate(Operator operator, L left, R right, Class<T> expectedReturnType) {
        Operation<L, R, T> operation = (Operation<L, R, T>) findOperation(operator, left.getClass(), right.getClass(), expectedReturnType);
        if (operation == null)
            return null;
        return operation.calculate(left, right);
    }

    public static <T> void registerDefaultValue(Class<T> type, Supplier<T> supplier) {
        if (DEFAULT_VALUES.containsKey(type))
            throw new IllegalArgumentException("A default value is already registered for type '" + type + '\'');
        DEFAULT_VALUES.put(type, supplier);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDefaultValue(Class<? extends T> type) {
        for (Class<?> c : DEFAULT_VALUES.keySet()) {
            if (c.isAssignableFrom(type))
                return ((Supplier<T>) DEFAULT_VALUES.get(c)).get();
        }
        return null;
    }


}
