package me.tud.weiner.lang.function;

public abstract class Function<T> {

    private final Signature signature;

    public Function(Signature signature) {
        this.signature = signature;
    }

    public abstract T run(Object[] parameters);

    public Signature getSignature() {
        return signature;
    }

}
