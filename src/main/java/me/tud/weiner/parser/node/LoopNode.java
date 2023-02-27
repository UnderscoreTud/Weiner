package me.tud.weiner.parser.node;

import me.tud.weiner.lang.operation.Operations;
import me.tud.weiner.lang.operation.Operator;

public abstract class LoopNode extends StatementNode {

    private boolean stop = false;
    private Number index = 0;

    protected abstract boolean iterate();

    protected abstract void runLoop();

    protected abstract Number incrementBy();

    protected void stop() {
        stop = true;
    }

    public Number getIndex() {
        return index;
    }

    @Override
    public final void run() {
        index = 0;
        while (!stop && iterate()) {
            runLoop();
            index = Operations.calculate(Operator.ADDITION, index, incrementBy(), Number.class);
        }
    }

}
