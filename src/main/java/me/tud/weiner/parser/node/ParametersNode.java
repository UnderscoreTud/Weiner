package me.tud.weiner.parser.node;

import me.tud.weiner.lang.variable.Variable;
import me.tud.weiner.lang.variable.VariableMap;

import java.util.Arrays;

public class ParametersNode extends ASTNode {

    private final String[] identifiers;
    private final VariableMap variableMap;

    public ParametersNode() {
        this(new IdentifierNode[0]);
    }

    public ParametersNode(IdentifierNode[] identifiers) {
        this.identifiers = Arrays.stream(identifiers)
                .map(LiteralNode::get)
                .toArray(String[]::new);;
        setChildren(identifiers);
        variableMap = getParser().getVariableMap();
    }

    @Override
    public void preLoad() {
        super.preLoad();
        for (String identifier : identifiers)
            variableMap.declareVariable(identifier);
    }

    public Variable<?> getParameter(int parameter) {
        return variableMap.getVariable(identifiers[parameter]);
    }

    public Variable<?> getParameter(String identifier) {
        return variableMap.getVariable(identifier);
    }

    @Override
    public String[] evaluate() {
        return identifiers;
    }

}
