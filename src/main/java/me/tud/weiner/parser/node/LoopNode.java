package me.tud.weiner.parser.node;

import me.tud.weiner.util.ArithmeticOperator;

public abstract class LoopNode extends StatementNode {

    private Number index = 0;

    protected abstract boolean iterate();

    protected abstract void runLoop();

    protected abstract Number incrementIndex();

    public Number getIndex() {
        return index;
    }

    @Override
    public final void run() {
        index = 0;
        while (iterate()) {
            runLoop();
            index = ArithmeticOperator.PLUS.calculate(index, incrementIndex());
        }
    }

}
