package me.tud.weiner.lang.variable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Variable<T> {

    @NotNull
    private final String identifier;
    private final Class<? extends T> returnType;
    @Nullable
    private T value;

    public Variable(@NotNull String identifier, Class<? extends T> returnType) {
        this(identifier, returnType, null);
    }

    public Variable(@NotNull String identifier, Class<? extends T> returnType, @Nullable T value) {
        this.identifier = identifier;
        this.returnType = returnType;
        this.value = value;
    }

    public T get() {
        return value;
    }

    public Class<? extends T> getReturnType() {
        return returnType;
    }

    public @NotNull String getIdentifier() {
        return identifier;
    }

    public void setValue(@Nullable T value) {
        this.value = value;
    }

}
