package me.tud.weiner.parser.node;

import me.tud.weiner.util.ComparisonOperator;

public class ComparisonNode extends ConditionNode {

    private final ExpressionNode<Number> leftOperand;
    private final ComparisonOperator operator;
    private final ExpressionNode<Number> rightOperand;

    public ComparisonNode(ExpressionNode<Number> leftOperand, ComparisonOperator operator, ExpressionNode<Number> rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        setChildren(leftOperand, rightOperand);
    }

    @Override
    public boolean test() {
        Number left = leftOperand.get();
        Number right = rightOperand.get();
        if (left == null || right == null)
            return false;
        return operator.test(left, right);
    }

}
