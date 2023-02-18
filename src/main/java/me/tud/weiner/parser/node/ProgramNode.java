package me.tud.weiner.parser.node;

import org.jetbrains.annotations.Nullable;

public class ProgramNode extends StatementNode {

    @Nullable
    private final StatementNode statement;
    @Nullable
    private final ProgramNode program;

    public ProgramNode() {
        this(null);
    }

    public <T extends StatementNode> ProgramNode(@Nullable T statement) {
        this(statement, null);
    }

    public <T extends StatementNode> ProgramNode(@Nullable T statement, @Nullable ProgramNode program) {
        this.statement = statement;
        this.program = program;
        setChildren(statement, program);
    }

    @Override
    public void run() {
        if (statement != null)
            statement.run();
        if (program != null)
            program.run();
    }

}
