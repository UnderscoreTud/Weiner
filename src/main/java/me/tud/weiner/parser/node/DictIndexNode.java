package me.tud.weiner.parser.node;

import me.tud.weiner.util.Dictionary;
import org.jetbrains.annotations.Nullable;

public class DictIndexNode extends ExpressionNode<Object> implements Settable<Object> {

    private final ExpressionNode<Dictionary> dictionary;
    private final ExpressionNode<String> key;

    public DictIndexNode(ExpressionNode<Dictionary> dictionary, ExpressionNode<String> key) {
        this.dictionary = dictionary;
        this.key = key;
        setChildren(dictionary, key);
    }

    @Override
    public @Nullable Object get() {
        Dictionary dictionary = this.dictionary.get();
        if (dictionary == null)
            return null;

        String key = this.key.get();
        if (key == null)
            return null;

        return dictionary.get(key);
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public void prepareSet(ExpressionNode<Object> expression) {}

    @Override
    public void set(Object value) {
        Dictionary dictionary = this.dictionary.get();
        if (dictionary == null)
            return;

        dictionary.put(key.get(), value);
    }

}
