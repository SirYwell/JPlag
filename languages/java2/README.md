# JPlag Java language frontend

The JPlag Java frontend allows the use of JPlag with submissions in Java. <br>
It is based on a simple handwritten Java lexer.

### Java specification compatibility

The lexer supports up to Java 19, with the following known limitations
- Unicode escape sequences which are syntactically relevant aren't recognized (e.g., `String s = \u0022Hello";`)
- The `non-sealed` keyword is not represented as keyword

### Token Extraction

The choice of tokens is intended to be similar to the existing C++ language module.
There are two enums for token types:
- `de.jplag.java2.lexer.JavaKeyword`: Keywords
- `de.jplag.java2.JavaTokenType`: Other token types

Contextual keywords (e.g., `record`, `yield`) are always extracted as this would require syntactical
information.

### Usage

This language module is only available using  a `JPlagOption` object with `new de.jplag.java2.JavaLexerLanguage()` as `language` in the Java API as described in the usage information in the [readme of the main project](https://github.com/jplag/JPlag#usage) and [in the wiki](https://github.com/jplag/JPlag/wiki/1.-How-to-Use-JPlag).
