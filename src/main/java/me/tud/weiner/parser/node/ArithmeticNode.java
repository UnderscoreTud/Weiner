package me.tud.weiner.parser.node;

import hu.webarticum.treeprinter.text.ConsoleText;
import me.tud.weiner.util.ArithmeticOperator;
import org.jetbrains.annotations.Nullable;

public class ArithmeticNode extends ExpressionNode<Number> {

    private final ExpressionNode<Number> leftOperand;
    private final ArithmeticOperator operator;
    private final ExpressionNode<Number> rightOperand;

    public ArithmeticNode(ExpressionNode<Number> leftOperand, ArithmeticOperator operator, ExpressionNode<Number> rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        setChildren(leftOperand, rightOperand);
    }

    @Override
    public @Nullable Number get() {
        Number left = leftOperand.getOptional().orElse(0);
        Number right = rightOperand.getOptional().orElse(0);

        return operator.calculate(left, right);
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public ConsoleText content() {
        return super.content().concat(ConsoleText.of(": '" + operator.getSign() + '\''));
    }
}
