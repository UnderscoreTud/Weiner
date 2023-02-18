package me.tud.weiner.parser.node;

import me.tud.weiner.lang.variable.Variable;
import me.tud.weiner.lang.variable.VariableMap;
import me.tud.weiner.lang.variable.expection.VariableException;
import org.jetbrains.annotations.Nullable;

public class VariableNode extends ExpressionNode<Object> implements Settable<Object> {

    private final String identifier;
    private final VariableMap variableMap;
    @SuppressWarnings("rawtypes")
    private Variable variable;

    public VariableNode(IdentifierNode identifier) {
        this.identifier = identifier.get();
        setChildren(identifier);
        variableMap = getParser().getVariableMap();
    }

    @Override
    public void init() {
        variable = variableMap.getVariable(identifier);
        if (variable == null)
            throw new VariableException("The variable '" + identifier + "' does not exist");
    }

    @Override
    public @Nullable Object get() {
        return variable.get();
    }

    @Override
    public Class<?> getReturnType() {
        return variable.getReturnType();
    }

    @Override
    public void prepareSet(ExpressionNode<Object> expression) {
        if (expression == null) {
            variable = variableMap.declareVariable(identifier);
        } else {
            variable = variableMap.getVariable(identifier);
            if (variable == null)
                variable = variableMap.declareVariable(identifier);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(Object value) {
        variable.setValue(value);
    }

}
