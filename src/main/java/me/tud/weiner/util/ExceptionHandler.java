package me.tud.weiner.util;

import me.tud.weiner.exception.ParseException;
import me.tud.weiner.exception.WienerException;
import me.tud.weiner.script.Script;

public class ExceptionHandler {

    private final Script script;

    public ExceptionHandler(Script script) {
        this.script = script;
    }

    public void handle(Throwable throwable) {
        if (throwable instanceof WienerException) {
            handle((WienerException) throwable);
        }
    }

    public void handle(WienerException exception) {
        String errorMessage = exception.getMessage();
        System.err.println(errorMessage);
    }

}
