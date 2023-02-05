package de.jplag.java2.lexer;

/**
 * A token representing a separator (JLS § 3.11)
 */
public record SeparatorToken(String separator, int start, int end) implements Token {

}
