package me.tud.weiner.parser.node;

import org.jetbrains.annotations.Nullable;

public class IfStatementNode extends StatementNode {

    private final ConditionNode condition;
    private final BlockNode thenBlock;
    @Nullable
    private final IfStatementNode elseIfStatement;
    @Nullable
    private final BlockNode elseBlock;

    public IfStatementNode(ConditionNode condition, BlockNode thenBlock) {
        this(condition, thenBlock, null, null);
    }

    public IfStatementNode(ConditionNode condition, BlockNode thenBlock, @Nullable BlockNode elseBlock) {
        this(condition, thenBlock, null, elseBlock);
    }

    public IfStatementNode(ConditionNode condition, BlockNode thenBlock, @Nullable IfStatementNode elseIfStatement) {
        this(condition, thenBlock, elseIfStatement, null);
    }

    public IfStatementNode(ConditionNode condition, BlockNode thenBlock, @Nullable IfStatementNode elseIfStatement, @Nullable BlockNode elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseIfStatement = elseIfStatement;
        this.elseBlock = elseBlock;
        setChildren(condition, thenBlock, elseIfStatement, elseBlock);
    }

    @Override
    public void run() {
        if (condition.get()) {
            thenBlock.evaluate();
        } else if (elseIfStatement != null) {
            elseIfStatement.evaluate();
        } else if (elseBlock != null) {
            elseBlock.evaluate();
        }

    }

}
