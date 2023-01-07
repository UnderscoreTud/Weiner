package me.tud.weiner.script;

import me.tud.weiner.lexer.LexicalAnalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Script {

    private final File file;
    private final LexicalAnalyzer lexer;

    public Script(File file) throws IOException {
        this.file = file;
        this.lexer = new LexicalAnalyzer(new FileInputStream(file));
    }

    public File getFile() {
        return file;
    }

    public void init() {
        lexer.lex().forEach(token -> System.out.print(token + " "));
        System.out.println();
    }

    public void run() {

    }

}
