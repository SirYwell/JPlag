package de.jplag.java2.lexer;

public record SeparatorToken(String separator, int start, int end) implements Token {

}
