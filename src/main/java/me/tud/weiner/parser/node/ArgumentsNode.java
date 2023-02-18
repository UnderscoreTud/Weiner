package me.tud.weiner.parser.node;

import me.tud.weiner.lang.variable.VariableMap;

public class ArgumentsNode extends ASTNode {

    private final ExpressionNode<?>[] expressions;

    public ArgumentsNode() {
        this(new ExpressionNode[0]);
    }

    public ArgumentsNode(ExpressionNode<?>[] expressions) {
        this.expressions = expressions;
        setChildren(expressions);
    }

    public int getParameterLength() {
        return expressions.length;
    }

    @Override
    public ExpressionNode<?>[] evaluate() {
        return expressions;
    }

}
