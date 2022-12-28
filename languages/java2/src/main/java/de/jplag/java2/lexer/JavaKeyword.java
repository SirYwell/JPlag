package de.jplag.java2.lexer;

import de.jplag.TokenType;

public enum JavaKeyword implements TokenType {
    ABSTRACT,
    ASSERT,
    BOOLEAN,
    BREAK,
    BYTE,
    CASE,
    CATCH,
    CHAR,
    CLASS,
    CONTINUE,
    DEFAULT,
    DO,
    DOUBLE,
    ELSE,
    EXTENDS,
    FALSE,
    FINAL,
    FINALLY,
    FLOAT,
    FOR,
    IF,
    IMPLEMENTS,
    IMPORT,
    INSTANCEOF,
    INT,
    INTERFACE,
    LONG,
    NATIVE,
    NEW,
    NON_SEALED {
        @Override
        public String toString() {
            return "non-sealed";
        }
    },
    NULL,
    PACKAGE,
    PRIVATE,
    PROTECTED,
    PUBLIC,
    RECORD,
    RETURN,
    SEALED,
    SHORT,
    STATIC,
    STRICTFP,
    SUPER,
    SWITCH,
    SYNCHRONIZED,
    THIS,
    THROW,
    THROWS,
    TRANSIENT,
    TRUE,
    TRY,
    VOID,
    VOLATILE,
    WHILE,
    ;

    @Override
    public String getDescription() {
        return name();
    }
}
