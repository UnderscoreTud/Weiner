package me.tud.weiner.lang.operation;

public class OperationInfo<L, R, T> {

    private final Class<L> leftClass;
    private final Class<R> rightClass;
    private final Class<T> returnClass;
    private final Operation<L, R, T> operation;

    public OperationInfo(Class<L> leftClass, Class<R> rightClass, Class<T> returnClass, Operation<L, R, T> operation) {
        this.leftClass = leftClass;
        this.rightClass = rightClass;
        this.returnClass = returnClass;
        this.operation = operation;
    }

    public Class<L> getLeftClass() {
        return leftClass;
    }

    public Class<R> getRightClass() {
        return rightClass;
    }

    public Class<T> getReturnClass() {
        return returnClass;
    }

    public Operation<L, R, T> getOperation() {
        return operation;
    }

}
