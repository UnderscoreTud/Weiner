package me.tud.weiner.parser.node;

import org.jetbrains.annotations.Nullable;

public abstract class StatementNode extends ASTNode {

    public abstract void run();

    @Override
    public final @Nullable Object evaluate() {
        run();
        return null;
    }

}
