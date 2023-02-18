package me.tud.weiner.script;

import me.tud.weiner.exception.ParseException;
import me.tud.weiner.exception.WienerException;
import me.tud.weiner.lang.variable.expection.VariableException;
import me.tud.weiner.lexer.LexicalAnalyzer;
import me.tud.weiner.parser.ParserInstance;
import me.tud.weiner.parser.SyntaxParser;
import me.tud.weiner.parser.node.ProgramNode;
import me.tud.weiner.util.ExceptionHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Script {

    private final File file;
    private final LexicalAnalyzer lexer;
    private final ExceptionHandler exceptionHandler;
    private final ParserInstance parserInstance;

    private SyntaxParser parser;
    private ProgramNode rootNode;

    public Script(File file) throws IOException {
        this.file = file;
        this.lexer = new LexicalAnalyzer(new FileInputStream(file));
        this.exceptionHandler = new ExceptionHandler(this);
        this.parserInstance = ParserInstance.get();
    }

    public File getFile() {
        return file;
    }

    public boolean parse() {
//        try {
            parserInstance.setActive(this);
            parser = new SyntaxParser(lexer.lex());
            rootNode = parser.program();
            rootNode.preLoad();
            rootNode.load();
            rootNode.init();
            return true;
//        } catch (WienerException e) {
//            exceptionHandler.handle(e);
//            return false;
//        }
    }

    public void visualize() {
        rootNode.visualize();
    }

    public void run() {
        rootNode.evaluate();
        parserInstance.setInactive();
    }

    public SyntaxParser getSyntaxParser() {
        return parser;
    }

}
