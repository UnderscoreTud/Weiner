package me.tud.weiner.lang.variable;

import me.tud.weiner.lang.variable.expection.VariableException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;

public class VariableMap {

    public static final int LENIENT = 0, STRICT = 1;

    @Nullable
    private final VariableMap parentMap;
    private final HashMap<String, Variable<?>> map = new HashMap<>();
    private final int flags;

    public VariableMap() {
        this(STRICT);
    }

    public VariableMap(int flags) {
        this(null, flags);
    }

    public VariableMap(@Nullable VariableMap parentMap) {
        this(parentMap, STRICT);
    }

    public VariableMap(@Nullable VariableMap parentMap, int flags) {
        this.parentMap = parentMap;
        this.flags = flags;
    }

    public Variable<?> declareVariable(String identifier) throws VariableException {
        return declareVariable(identifier, null);
    }

    public Variable<?> declareVariable(String identifier, @Nullable Object value) throws VariableException {
        if (variableExists(identifier))
            throw VariableException.alreadyDeclared(identifier);
        Variable<?> variable = new Variable<>(identifier, Object.class, value);
        map.put(identifier, variable);
        return variable;
    }

    public boolean variableExists(String identifier) {
        return (parentMap != null && flags == STRICT ? map.containsKey(identifier) && parentMap.variableExists(identifier) : map.containsKey(identifier));
    }

    public void clear() {
        map.clear();
    }

    @Nullable
    public Variable<?> getVariable(String identifier) {
        Variable<?> variable = map.get(identifier);
        if (variable == null && parentMap != null)
            return parentMap.getVariable(identifier);
        return variable;
    }

    public Optional<VariableMap> getParentMap() {
        return Optional.ofNullable(parentMap);
    }

    private HashMap<String, Variable<?>> collectMaps() {
        HashMap<String, Variable<?>> map = new HashMap<>(this.map);
        if (parentMap != null)
            map.putAll(parentMap.collectMaps());
        return map;
    }

    @Override
    public String toString() {
        return collectMaps().toString();
    }

}
