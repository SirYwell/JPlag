package de.jplag.java2.lexer;

public record KeywordToken(JavaKeyword keyword, int start) implements Token {

    @Override
    public int end() {
        return this.start() + keyword().name().length();
    }

    @Override
    public int length() {
        return keyword().name().length();
    }
}
