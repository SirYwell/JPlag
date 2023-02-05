package de.jplag.java2.lexer;

/**
 * A token representing a literal (JLS § 3.10)
 */
public record LiteralToken(String literal, int start, int end) implements Token {

}
