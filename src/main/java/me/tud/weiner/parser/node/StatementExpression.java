package me.tud.weiner.parser.node;

import org.jetbrains.annotations.Nullable;

public abstract class StatementExpression<T> extends ExpressionNode<T> {

    public abstract @Nullable T run();

    @Override
    public final @Nullable T get() {
        return run();
    }

    public abstract Class<? extends T> getReturnType();

    public final StatementNode asStatement() {
        return new Statement();
    }

    private class Statement extends StatementNode {

        private Statement() {
            setChildren(StatementExpression.this.getChildren());
        }

        @Override
        public void preLoad() {
            StatementExpression.this.preLoad();
        }

        @Override
        public void load() {
            StatementExpression.this.load();
        }

        @Override
        public void init() {
            StatementExpression.this.init();
        }

        @Override
        public void run() {
            StatementExpression.this.run();
        }

        @Override
        protected void setParent(@Nullable ASTNode parent) {
            StatementExpression.this.setParent(parent);
        }

        @Override
        protected String getName() {
            return StatementExpression.this.getName();
        }

    }
    
}
