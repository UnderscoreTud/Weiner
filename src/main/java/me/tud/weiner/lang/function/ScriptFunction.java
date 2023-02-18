package me.tud.weiner.lang.function;

import me.tud.weiner.parser.node.BlockNode;
import me.tud.weiner.parser.node.ParametersNode;

public class ScriptFunction extends Function<Object> {

    private final ParametersNode parameters;
    private final BlockNode block;

    public ScriptFunction(Signature signature, ParametersNode parameters, BlockNode block) {
        super(signature);
        this.parameters = parameters;
        this.block = block;
    }

    public ParametersNode getParameters() {
        return parameters;
    }

    @Override
    public Object run(Object[] parameters) {
        return block.evaluate();
    }

}
