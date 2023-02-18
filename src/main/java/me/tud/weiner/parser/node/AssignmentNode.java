package me.tud.weiner.parser.node;

import me.tud.weiner.lang.variable.Variable;
import me.tud.weiner.lang.variable.VariableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssignmentNode extends StatementNode {

    @NotNull
    @SuppressWarnings("rawtypes")
    private final Settable settable;
    @Nullable
    private final ExpressionNode<?> expression;

    public AssignmentNode(@NotNull Settable<?> settable, @Nullable ExpressionNode<?> expression) {
        if (!(settable instanceof ASTNode))
            throw new IllegalArgumentException("settable is not an ASTNode");

        this.settable = settable;
        this.expression = expression;
        setChildren((ASTNode) settable, expression);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load() {
        if (expression != null)
            expression.load();
        settable.prepareSet(expression);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        settable.set(expression == null ? null : expression.get());
    }

}
