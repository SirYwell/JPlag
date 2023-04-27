package de.jplag.java.configurable;

import java.io.File;
import java.util.List;
import java.util.Set;

import de.jplag.Language;
import de.jplag.ParsingException;
import de.jplag.Token;

/**
 * Language for Java 9 and newer.
 */
public class ConfigurableJavaLanguage implements Language {
    private static final String IDENTIFIER = "java-configurable";

    private final Parser parser;
    private final ExtractorGenerator extractorGenerator;

    public ConfigurableJavaLanguage(ExtractorGenerator extractorGenerator) {
        this.extractorGenerator = extractorGenerator;
        this.parser = new Parser(extractorGenerator);
    }

    @Override
    public String[] suffixes() {
        return new String[] {".java", ".JAVA"};
    }

    @Override
    public String getName() {
        return "Javac based AST plugin (configurable)";
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER + "-" + this.extractorGenerator.name();
    }

    @Override
    public int minimumTokenMatch() {
        return 9;
    }

    @Override
    public List<Token> parse(Set<File> files) throws ParsingException {
        return this.parser.parse(files);
    }
}
