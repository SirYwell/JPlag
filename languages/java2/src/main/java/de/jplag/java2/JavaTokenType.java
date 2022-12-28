package de.jplag.java2;

import de.jplag.TokenType;

public enum JavaTokenType implements TokenType {
    J_BLOCK_BEGIN("BLOCK{"),
    J_BLOCK_END("}BLOCK"),
    J_QUESTIONMARK("COND"),
    J_ANNO_T("ANNO_T"),
    J_ASSIGN("ASSIGN"),
    J_METHOD("METHOD")
    ;

    private final String description;

    public String getDescription() {
        return this.description;
    }

    JavaTokenType(String description) {
        this.description = description;
    }
}
