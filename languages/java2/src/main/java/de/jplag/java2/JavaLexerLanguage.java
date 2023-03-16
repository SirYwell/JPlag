package de.jplag.java2;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.kohsuke.MetaInfServices;

import de.jplag.ParsingException;
import de.jplag.Token;

/**
 * Language for Java.
 */
@MetaInfServices(de.jplag.Language.class)
public class JavaLexerLanguage implements de.jplag.Language {
    private static final String IDENTIFIER = "java2";

    private final JavaLexerAdapter parser;

    public JavaLexerLanguage() {
        parser = new JavaLexerAdapter();
    }

    @Override
    public String[] suffixes() {
        return new String[] {".java", ".JAVA"};
    }

    @Override
    public String getName() {
        return "Javac based AST plugin";
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public int minimumTokenMatch() {
        return 9;
    }

    @Override
    public List<Token> parse(Set<File> files) throws ParsingException {
        return new JavaLexerAdapter().parse(files);
    }
}
