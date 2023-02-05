package de.jplag.java2.lexer;

/**
 * A token representing an identifier (JLS § 3.8)
 */
public record IdentifierToken(int start, int end) implements Token {

}
