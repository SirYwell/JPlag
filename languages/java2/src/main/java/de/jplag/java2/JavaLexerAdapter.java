package de.jplag.java2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.jplag.AbstractParser;
import de.jplag.ParsingException;
import de.jplag.Token;
import de.jplag.TokenType;

public class JavaLexerAdapter extends AbstractParser {
    private List<Token> tokens;

    public List<Token> parse(Set<File> files) throws ParsingException {
        tokens = new ArrayList<>();
        for (File file : files) {
            new LexerExtractor(file).parse(this);
        }
        return tokens;
    }

    public void add(TokenType type, File file, long line, long column, long length) {
        add(new Token(type, file, (int) line, (int) column, (int) length));
    }

    public void add(Token token) {
        tokens.add(token);
    }
}
