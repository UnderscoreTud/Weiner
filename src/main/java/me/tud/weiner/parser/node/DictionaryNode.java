package me.tud.weiner.parser.node;

import me.tud.weiner.util.Dictionary;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class DictionaryNode extends ExpressionNode<Dictionary> {

    private final HashMap<String, ExpressionNode<?>> nodeMap;

    public DictionaryNode(HashMap<String, ExpressionNode<?>> nodeMap) {
        this.nodeMap = nodeMap;
        setChildren(nodeMap.values().toArray(new ExpressionNode[0]));
    }

    @Override
    public @Nullable Dictionary get() {
        Dictionary dictionary = new Dictionary();
        nodeMap.forEach((id, expression) -> dictionary.put(id, expression.get()));
        return dictionary;
    }

    @Override
    public Class<? extends Dictionary> getReturnType() {
        return Dictionary.class;
    }

}
