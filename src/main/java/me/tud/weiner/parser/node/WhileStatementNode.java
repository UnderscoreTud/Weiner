package me.tud.weiner.parser.node;

public class WhileStatementNode extends LoopNode {

    private final ConditionNode condition;
    private final BlockNode block;

    public WhileStatementNode(ConditionNode condition, BlockNode block) {
        this.condition = condition;
        this.block = block;
        setChildren(condition, block);
    }

    @Override
    protected boolean iterate() {
        return condition.test();
    }

    @Override
    protected void runLoop() {
        block.evaluate();
    }

    @Override
    protected Number incrementIndex() {
        return 1;
    }

}
