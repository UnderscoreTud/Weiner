package me.tud.weiner.parser.node;

import org.jetbrains.annotations.NotNull;

public class IdentifierNode extends LiteralNode<String> {

    public IdentifierNode(@NotNull String value) {
        super(value);
    }

}
