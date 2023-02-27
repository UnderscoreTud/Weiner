package me.tud.weiner.parser.node;

import me.tud.weiner.exception.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockNode extends ASTNode {

    private final StatementNode[] statements;
    private @Nullable BlockNode outerBlock;
    private boolean returnable;
    private ExpressionNode<?> returnExpression;

    public BlockNode() {
        this(new StatementNode[0]);
    }

    public BlockNode(StatementNode @NotNull [] statements) {
        this(statements, false);
    }

    public BlockNode(StatementNode @NotNull [] statements, boolean returnable) {
        this.statements = statements;
        this.returnable = returnable;
        setChildren(statements);
    }

    @Override
    public void init() {
        super.init();
        ASTNode node = getParent();
        while (node != null) {
            if (node instanceof BlockNode block) {
                this.outerBlock = block;
                break;
            }
            node = node.getParent();
        }
    }

    public boolean isReturnable() {
        return outerBlock != null && outerBlock.isReturnable() || returnable;
    }

    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    public void setReturnExpression(ExpressionNode<?> returnExpression) {
        if (!isReturnable())
            throw new ParseException("You can't return here", getParser().getCurrentToken());
        this.returnExpression = returnExpression;
        if (outerBlock != null)
            outerBlock.setReturnExpression(returnExpression);
    }

    public boolean hasReturned() {
        return returnExpression != null;
    }
    public ExpressionNode<?> getReturnExpression() {
        return returnExpression;
    }

    @Override
    public @Nullable Object evaluate() {
        for (StatementNode statement : statements) {
            statement.run();
            if (returnExpression != null)
                return returnExpression.evaluate();
        }
        return null;
    }

}
