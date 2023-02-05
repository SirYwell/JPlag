package de.jplag.java2.lexer;

/**
 * A token representing a keyword (JLS § 3.9)
 */
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
