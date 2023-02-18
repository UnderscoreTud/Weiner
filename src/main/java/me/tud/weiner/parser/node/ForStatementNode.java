package me.tud.weiner.parser.node;

import me.tud.weiner.util.Range;

public class ForStatementNode extends StatementNode {

    private final LiteralNumberNode first, second;
    private final BlockNode block;

    public ForStatementNode(LiteralNumberNode first, LiteralNumberNode second, BlockNode block) {
        this.first = first;
        this.second = second;
        this.block = block;
        setChildren(first, second, block);
    }

    @Override
    public void run() {

    }

}
