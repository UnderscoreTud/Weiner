package me.tud.weiner.lang.variable.expection;

import me.tud.weiner.exception.WienerException;

public class VariableException extends WienerException {

    public VariableException() {
        super();
    }

    public VariableException(String message) {
        super(message);
    }

    public VariableException(Throwable cause) {
        super(cause);
    }

    public VariableException(String message, Throwable cause) {
        super(message, cause);
    }

    public static VariableException alreadyDeclared(String identifier) {
        return new VariableException(String.format("The variable '%s' is already declared", identifier));
    }

}
