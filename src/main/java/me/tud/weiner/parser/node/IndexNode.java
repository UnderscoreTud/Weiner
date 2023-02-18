package me.tud.weiner.parser.node;

import me.tud.weiner.exception.ParseException;
import me.tud.weiner.exception.WienerException;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class IndexNode extends ExpressionNode<Number> {

    private final int level;
    private LoopNode loop;

    public IndexNode() {
        this(0);
    }

    public IndexNode(int level) {
        this.level = level;
    }

    @Override
    public void init() {
        super.init();
        List<LoopNode> loops = new LinkedList<>();
        ASTNode node = getParent();
        do {
            if (node instanceof LoopNode)
                loops.add(0, (LoopNode) node);
            assert node != null;
            node = node.getParent();
        } while (node != null);
        if (level == 0) {
            if (loops.size() == 1) {
                loop = loops.get(0);
                return;
            } else {
                throw new WienerException("Ambiguous loop index, use @1/2/3...");
            }
        }
        if (level > loops.size())
            throw new WienerException(String.format("@%d does not exist", level));
        loop = loops.get(level - 1);
    }

    @Override
    public @Nullable Number get() {
        return loop.getIndex();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

}
