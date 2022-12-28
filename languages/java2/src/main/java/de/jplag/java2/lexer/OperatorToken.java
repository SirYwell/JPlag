package de.jplag.java2.lexer;

public record OperatorToken(String operator, int start, int end) implements Token {

}
