package me.tud.weiner.lang.function;

import me.tud.weiner.parser.node.BlockNode;
import me.tud.weiner.parser.node.ParametersNode;

import java.util.HashMap;

public class Functions {

    private static final HashMap<Signature, Function<?>> functions = new HashMap<>();

    public static Signature parseSignature(String identifier, String[] parameters) {
        Signature signature = new Signature(identifier, parameters);
        if (signatureExists(identifier, parameters))
            return null;
        functions.put(signature, null);
        return signature;
    }

    public static boolean signatureExists(String identifier, String[] parameters) {
        return signatureExists(new Signature(identifier, parameters));
    }

    private static boolean signatureExists(Signature signature) {
        return functions.containsKey(signature);
    }

    public static Function<?> registerFunction(Signature signature, ParametersNode parameters, BlockNode block) {
        Function<?> function = new ScriptFunction(signature, parameters, block);
        functions.put(signature, function);
        return function;
    }

    public static <T> JavaFunction<T> registerFunction(JavaFunction<T> function) {
        functions.put(function.getSignature(), function);
        return function;
    }

    public static Function<?> getFunction(String identifier, int parameterLength) {
        return functions.get(functions.keySet().stream()
                .filter(signature -> signature.getIdentifier().equals(identifier) && (signature.isListArgument() || signature.getParameterLength() == parameterLength))
                .findAny().orElse(null));
    }

}
