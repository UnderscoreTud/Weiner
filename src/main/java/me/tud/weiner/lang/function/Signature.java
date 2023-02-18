package me.tud.weiner.lang.function;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Signature {

    private static final String LIST_ARGUMENT = "";

    private final @NotNull String identifier;
    private final String @NotNull [] parameters;
    private final boolean listArgument;

    Signature(@NotNull String identifier) {
        this(identifier, LIST_ARGUMENT);
    }

    @SuppressWarnings("StringEquality")
    Signature(@NotNull String identifier, String @NotNull ... parameters) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.listArgument = (parameters.length == 1 && parameters[0] == LIST_ARGUMENT);
    }

    public @NotNull String getIdentifier() {
        return identifier;
    }

    public String[] getParameters() {
        return parameters;
    }

    public int getParameterLength() {
        return listArgument ? Integer.MAX_VALUE :  parameters.length;
    }

    public boolean isListArgument() {
        return listArgument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Signature signature))
            return false;

        if (!identifier.equals(signature.identifier))
            return false;

        if (listArgument || signature.listArgument)
            return true;

        return parameters.length == signature.parameters.length;
    }

    @Override
    public int hashCode() {
        int result = identifier.hashCode();
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }

}
