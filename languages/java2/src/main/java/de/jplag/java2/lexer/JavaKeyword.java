package de.jplag.java2.lexer;

import de.jplag.TokenType;

/**
 * Java keywords (JLS § 3.9), excluding
 * <ul>
 * <li>_ (underscore)</li>
 * <li>other reserved but unused keywords</li>
 * <li>var</li>
 * <li>module-info.java specific keywords</li>
 * </ul>
 */
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
    PERMITS,
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
    YIELD;

    @Override
    public String getDescription() {
        return name();
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
