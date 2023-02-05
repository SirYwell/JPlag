package de.jplag.java2.lexer;

/**
 * A token representing an operator (JLS § 3.12)
 */
public record OperatorToken(String operator, int start, int end) implements Token {

}
