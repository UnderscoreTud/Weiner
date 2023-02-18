package me.tud.weiner.parser.node;

import me.tud.weiner.exception.ParseException;
import me.tud.weiner.lang.function.Function;
import me.tud.weiner.lang.function.Functions;
import me.tud.weiner.lang.function.ScriptFunction;
import me.tud.weiner.lang.function.Signature;
import me.tud.weiner.lang.variable.Variable;
import me.tud.weiner.lang.variable.VariableMap;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class FunctionCallNode extends StatementExpression<Object> {

    private final String identifier;
    private final ArgumentsNode arguments;
    private Function<?> function;

    public FunctionCallNode(IdentifierNode identifier) {
        this(identifier, new ArgumentsNode());
    }

    public FunctionCallNode(IdentifierNode identifier, ArgumentsNode arguments) {
        this.identifier = identifier.get();
        this.arguments = arguments;
        setChildren(identifier, arguments);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void init() {
        super.init();
        if ((function = Functions.getFunction(identifier, arguments.getParameterLength())) == null)
            throw new ParseException(String.format("Function '%s' doesn't exist", identifier), getParser().getCurrentToken());
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public @Nullable Object run() {
        Object[] parameters = Arrays.stream(arguments.evaluate())
                .map(ExpressionNode::evaluate)
                .toArray();
        if (function instanceof ScriptFunction scriptFunction) {
            for (int i = 0; i < parameters.length; i++)
                ((Variable) scriptFunction.getParameters().getParameter(i)).setValue(parameters[i]);
        }
        return function.run(parameters);
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

}
