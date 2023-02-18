package me.tud.weiner.parser.node;

import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.decorator.PadTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.boxing.BoxingTreePrinter;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;
import hu.webarticum.treeprinter.text.ConsoleText;
import me.tud.weiner.parser.ParserInstance;
import me.tud.weiner.parser.SyntaxParser;
import me.tud.weiner.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ASTNode implements TreeNode {

    @Nullable
    private ASTNode parent = null;
    private List<ASTNode> children = Collections.emptyList();

    public void init() {
        children.forEach(ASTNode::init);
    }

    public void preLoad() {
        children.forEach(ASTNode::preLoad);
    }

    public void load() {
        children.forEach(ASTNode::load);
    }

    public abstract @Nullable Object evaluate();

    public @Nullable ASTNode getParent() {
        return parent;
    }

    protected void setParent(@Nullable ASTNode parent) {
        this.parent = parent;
    }

    public List<ASTNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    protected void setChildren(List<ASTNode> children) {
        this.children = CollectionUtils.listOfNonNullables(children);
    }

    protected void setChildren(ASTNode... children) {
        this.children = CollectionUtils.listOfNonNullables(children);
        for (ASTNode node : this.children)
            node.setParent(this);
    }

    public final ParserInstance getParser() {
        return ParserInstance.get();
    }

    public void visualize() {
        new TraditionalTreePrinter().print(new BorderTreeNodeDecorator(new PadTreeNodeDecorator(this, new Insets(0, 2))));
    }

    @Override
    public ConsoleText content() {
        return ConsoleText.of(getName());
    }

    @Override
    public List<TreeNode> children() {
        return new ArrayList<>(getChildren());
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

}
