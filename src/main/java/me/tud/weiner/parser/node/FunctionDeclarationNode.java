package me.tud.weiner.parser.node;

import me.tud.weiner.exception.ParseException;
import me.tud.weiner.lang.function.Functions;
import me.tud.weiner.lang.function.Signature;

public class FunctionDeclarationNode extends StatementNode {

    private final String identifier;
    private final ParametersNode parameters;
    private final BlockNode block;
    private Signature signature;

    public FunctionDeclarationNode(IdentifierNode identifier, ParametersNode parameters, BlockNode block) {
        this.identifier = identifier.get();
        this.parameters = parameters;
        this.block = block;
        setChildren(identifier, parameters, block);
    }

    @Override
    public void preLoad() {
        super.preLoad();
        if ((signature = Functions.parseSignature(identifier, parameters.evaluate())) == null)
            throw new ParseException(String.format("Function '%s' already exists", identifier), getParser().getCurrentToken());
    }

    @Override
    public void load() {
        super.load();
        Functions.registerFunction(signature, parameters, block);
    }

    @Override
    public void run() {}

}
