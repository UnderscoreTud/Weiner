package me.tud.weiner.parser.node;

import me.tud.weiner.exception.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockNode extends ASTNode {

    public static final int NO_RETURN = 0;
    public static final int OPTIONAL_RETURN = 1;
    public static final int REQUIRED_RETURN = 2;
    public static final int RETURNABLE = OPTIONAL_RETURN | REQUIRED_RETURN;

    private final StatementNode[] statements;
    private final int flags;
    private ExpressionNode<?> returnExpression;

    public BlockNode() {
        this(new StatementNode[0]);
    }

    public BlockNode(StatementNode @NotNull [] statements) {
        this(statements, NO_RETURN);
    }

    public BlockNode(StatementNode @NotNull [] statements, int flags) {
        this.statements = statements;
        this.flags = flags;
        setChildren(statements);
    }

    public int getFlags() {
        return flags;
    }

    public void setReturnExpression(ExpressionNode<?> returnExpression) {
        if ((flags & RETURNABLE) == 0)
            throw new ParseException("You can't return here", getParser().getCurrentToken());
        this.returnExpression = returnExpression;
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
