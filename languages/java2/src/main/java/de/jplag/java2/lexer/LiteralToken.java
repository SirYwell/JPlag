package de.jplag.java2.lexer;

public record LiteralToken(String literal, int start, int end) implements Token {

}
