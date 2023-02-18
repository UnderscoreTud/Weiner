package me.tud.weiner.parser;

import me.tud.weiner.lang.Scope;
import me.tud.weiner.lang.variable.VariableMap;
import me.tud.weiner.lexer.token.Token;
import me.tud.weiner.parser.node.ASTNode;
import me.tud.weiner.parser.node.BlockNode;
import me.tud.weiner.parser.node.LoopNode;
import me.tud.weiner.script.Script;
import org.w3c.dom.Node;

public class ParserInstance {

    private static final ThreadLocal<ParserInstance> PARSER_INSTANCES = ThreadLocal.withInitial(ParserInstance::new);

    public static ParserInstance get() {
        return PARSER_INSTANCES.get();
    }

    private boolean active = false;
    private Script script = null;

    public void setInactive() {
        active = false;
        script = null;
    }

    public void setActive(Script script) {
        this.active = true;
        this.script = script;
    }

    public boolean isActive() {
        return active;
    }

    public Script getScript() {
        return script;
    }

    public SyntaxParser getParser() {
        return script.getSyntaxParser();
    }

    public Token getCurrentToken() {
        return getParser().getCurrentToken();
    }

    public Scope getScope() {
        return getParser().getScope();
    }

    public VariableMap getVariableMap() {
        return getScope().get();
    }

    @SuppressWarnings("unchecked")
    public <T extends ASTNode> T getClosestNode(ASTNode node, Class<T> nodeClass) {
        node = node.getParent();
//        if (node != null)
//            node = node.getParent();
        while (node != null) {
            if (nodeClass.isInstance(node))
                return (T) node;
            node = node.getParent();
        }
        return null;
    }

}
