package me.tud.weiner.parser.node;

import hu.webarticum.treeprinter.text.ConsoleText;
import me.tud.weiner.exception.ParseException;
import me.tud.weiner.lang.operation.OperationInfo;
import me.tud.weiner.lang.operation.Operations;
import me.tud.weiner.lang.operation.Operator;
import me.tud.weiner.util.NumberUtil;
import org.jetbrains.annotations.Nullable;

public class OperationNode extends ExpressionNode<Object> {

    private final ExpressionNode<?> leftOperand;
    private final Operator operator;
    private final ExpressionNode<?> rightOperand;

    @SuppressWarnings("rawtypes")
    private OperationInfo operationInfo;
    private Class<?> returnType = Object.class;

    public OperationNode(ExpressionNode<?> leftOperand, Operator operator, ExpressionNode<?> rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        setChildren(leftOperand, rightOperand);
    }

    @Override
    public void init() {
        super.init();
        Class<?> leftClass = leftOperand.getReturnType(), rightClass = rightOperand.getReturnType();
        if ((!hasHandlers(leftClass, rightClass))
                || (leftClass != Object.class
                && rightClass != Object.class
                && (operationInfo = Operations.findOperation(operator, leftClass, rightClass)) == null)) {
            System.out.println(Operations.findOperation(operator, leftClass, rightClass));
            throw new ParseException('\'' + operator.sign + "' can't be performed on " + leftClass.getSimpleName() + " and " + rightClass.getSimpleName(),
                    getParser().getCurrentToken());
        }

        returnType = operationInfo == null ? Object.class : operationInfo.getReturnClass();

        if (Number.class.isAssignableFrom(returnType)) {
            if (operator == Operator.DIVISION) {
                returnType = Double.class;
            } else if (Number.class.isAssignableFrom(leftClass) && Number.class.isAssignableFrom(rightClass)) {
                //noinspection unchecked
                returnType = NumberUtil.isInteger((Class<? extends Number>) leftClass, (Class<? extends Number>) rightClass) ? Long.class : Double.class;
            }
        }
    }

    private boolean hasHandlers(Class<?>... classes) {
        for (Class<?> type : classes) {
            if (type != Object.class && Operations.getOperations(operator, type).size() == 0)
                return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable Object get() {
        Object left = leftOperand.get();
        Object right = rightOperand.get();

        Class<?> leftClass = left == null ? leftOperand.getReturnType() : left.getClass();
        Class<?> rightClass = right == null ? rightOperand.getReturnType() : right.getClass();

        if (left == null && leftClass == Object.class && right == null && rightClass == Object.class)
            return null;

        if (right == null) {
            if (rightClass == Object.class) {
                rightClass = Operations.lookupClass(operator, leftClass);
                if (rightClass == null)
                    return null;
            }

            right = Operations.getDefaultValue(rightClass);
        }

        if (left == null) {
            if (leftClass == Object.class) {
                leftClass = Operations.lookupClass(operator, rightClass);
                if (leftClass == null)
                    return null;
            }

            left = Operations.getDefaultValue(leftClass);
        }

        if (left == null)
            return right;
        if (right == null)
            return left;

        if (operationInfo == null) {
            operationInfo = Operations.findOperation(operator, leftClass, rightClass);
            if (operationInfo == null)
                return null;
        }

        return operationInfo.getOperation().calculate(left, right);
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public ConsoleText content() {
        return super.content().concat(ConsoleText.of(": '" + operator.sign + '\''));
    }

}
