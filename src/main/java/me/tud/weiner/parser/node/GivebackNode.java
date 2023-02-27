package me.tud.weiner.parser.node;

import me.tud.weiner.exception.ParseException;

public class GivebackNode extends StatementNode {

    private final ExpressionNode<?> returnExpression;
    private BlockNode block;

    public GivebackNode(ExpressionNode<?> returnExpression) {
        this.returnExpression = returnExpression;
        setChildren(returnExpression);
    }

    @Override
    public void init() {
        super.init();
        ASTNode node = getParent();
        while (node != null) {
            if (node instanceof BlockNode block) {
                this.block = block;
                break;
            }
            node = node.getParent();
        }
        if (block == null)
            throw new ParseException("You can only return inside a returnable block", getParser().getCurrentToken());
    }

    @Override
    public void run() {
        block.setReturnExpression(returnExpression);
    }

}
