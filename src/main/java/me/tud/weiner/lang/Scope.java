package me.tud.weiner.lang;

import me.tud.weiner.lang.variable.VariableMap;

public class Scope {

    private int level;
    private VariableMap variableMap;

    public Scope() {
        this(new VariableMap());
    }

    public Scope(VariableMap variableMap) {
        this(0, variableMap);
    }

    public Scope(int level, VariableMap variableMap) {
        this.level = level;
        this.variableMap = variableMap;
    }

    public void enter() {
        level++;
        variableMap = new VariableMap(variableMap);
    }

    public void enter(int flags) {
        level++;
        variableMap = new VariableMap(variableMap, flags);
    }

    public void exit() {
        level--;
        variableMap.clear();
        variableMap = variableMap.getParentMap().orElseThrow(IllegalAccessError::new);
    }

    public VariableMap get() {
        return variableMap;
    }

    public int getLevel() {
        return level;
    }

}
