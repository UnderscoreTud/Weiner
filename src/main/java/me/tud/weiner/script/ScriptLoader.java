package me.tud.weiner.script;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ScriptLoader {

    private static final Map<File, Script> SCRIPT_MAP = new HashMap<>();

    private ScriptLoader() {
        throw new UnsupportedOperationException();
    }

    public static boolean isScriptLoaded(File file) {
        return SCRIPT_MAP.containsKey(file);
    }

    public static boolean isScriptLoaded(Script script) {
        return SCRIPT_MAP.containsValue(script);
    }

    public static @Nullable Script getScript(File file) {
        return SCRIPT_MAP.get(file);
    }

    public static Script loadScript(File file) throws IOException {
        if (isScriptLoaded(file))
            return getScript(file);
        Script script = new Script(file);
        script.parse();
        SCRIPT_MAP.put(file, script);
        return script;
    }

}
