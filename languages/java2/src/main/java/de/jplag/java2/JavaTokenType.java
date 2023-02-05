package de.jplag.java2;

import de.jplag.TokenType;

/**
 * Additional token types that aren't covered by {@link de.jplag.java2.lexer.JavaKeyword}.
 */
public enum JavaTokenType implements TokenType {
    BLOCK_BEGIN("BLOCK{"),
    BLOCK_END("}BLOCK"),
    QUESTIONMARK("COND"),
    ANNO_T("ANNO_T"),
    ASSIGN("ASSIGN"),;

    private final String description;

    public String getDescription() {
        return this.description;
    }

    JavaTokenType(String description) {
        this.description = description;
    }
}
