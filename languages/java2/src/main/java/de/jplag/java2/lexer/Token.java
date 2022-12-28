package de.jplag.java2.lexer;

public sealed interface Token permits IdentifierToken, KeywordToken, LiteralToken, OperatorToken, SeparatorToken {

    int start();

    int end();

    default int length() {
        return end() - start();
    }
}
