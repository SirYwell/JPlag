package de.jplag.java2;

import de.jplag.ParsingException;
import de.jplag.java2.lexer.IdentifierToken;
import de.jplag.java2.lexer.JavaKeyword;
import de.jplag.java2.lexer.JavaLexer;
import de.jplag.java2.lexer.KeywordToken;
import de.jplag.java2.lexer.OperatorToken;
import de.jplag.java2.lexer.SeparatorToken;
import de.jplag.java2.lexer.Token;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class LexerExtractor {
    private static final char LF = '\n';
    private static final char CR = '\r';
    private static final Set<JavaKeyword> BOOLEAN_LITERALS = EnumSet.of(JavaKeyword.FALSE, JavaKeyword.TRUE);

    private final Path path;
    private int currentLine;
    /**
     * The position of the current line break in the content string
     */
    private int currentLineBreakIndex;


    public LexerExtractor(File file) {
        this.path = file.toPath();
    }

    public void parse(Parser parser) throws ParsingException {
        try {
            char[] chars = Files.readString(path).toCharArray();
            JavaLexer lexer = new JavaLexer(chars, 0, chars.length);
            Token next;
            List<Token> list = new ArrayList<>();
            while ((next = lexer.lex()) != null) {
                list.add(next);
            }
            addTokens(list, chars, parser);
        } catch (IOException e) {
            throw new ParsingException(path.toFile(), e);
        }
    }

    private void addTokens(List<Token> list, char[] content, Parser parser) {
        int lastEnd = 0;
        for (int i = 0; i < list.size(); i++) {
            Token token = list.get(i);
            advanceLineBreaks(content, lastEnd, token.start());
            int column = token.start() - currentLineBreakIndex;
            if (token instanceof SeparatorToken separatorToken) {
                switch (separatorToken.separator()) {
                    case "@" -> {
                        if (i + 1 < list.size() && list.get(i + 1) instanceof KeywordToken kw && kw.keyword() == JavaKeyword.INTERFACE) {
                            i++;
                            parser.add(JavaTokenType.J_ANNO_T, path.toFile(), currentLine, column, kw.end() - token.start());
                        }
                    }
                    case "{" -> parser.add(JavaTokenType.J_BLOCK_BEGIN, path.toFile(), currentLine, column, token.length());
                    case "}" -> parser.add(JavaTokenType.J_BLOCK_END, path.toFile(), currentLine, column, token.length());
                }
            } else if (token instanceof KeywordToken keywordToken && !BOOLEAN_LITERALS.contains(keywordToken.keyword())) {
                parser.add(new de.jplag.Token(keywordToken.keyword(), path.toFile(), currentLine, column,token.length()));
            } else if (token instanceof OperatorToken operatorToken) {
                String operator = operatorToken.operator();
                if ((operator.contains("=") && !operator.equals("==")) || operator.equals("++") || operator.equals("--")) {
                    parser.add(JavaTokenType.J_ASSIGN, path.toFile(), currentLine, column, token.length());
                } else if (operator.equals("?")) {
                    parser.add(JavaTokenType.J_QUESTIONMARK, path.toFile(), currentLine, column, token.length());
                }
            } else if (token instanceof IdentifierToken) {
                if (i + 1 < list.size() && list.get(i + 1) instanceof SeparatorToken sw && sw.separator().equals("(")) {
                    i++;
                    parser.add(JavaTokenType.J_METHOD, path.toFile(), currentLine, column, token.length());
                }
            }
            lastEnd = token.end();
        }
    }

    // from text module
    private void advanceLineBreaks(char[] content, int lastTokenEnd, int nextTokenBegin) {
        for (int i = lastTokenEnd; i < nextTokenBegin; i++) {
            if (content[i] == LF) {
                currentLine++;
                currentLineBreakIndex = i;
            } else if (content[i] == CR) {
                if (i + 1 < content.length && content[i + 1] == LF) { // CRLF
                    i++; // skip following LF
                }
                currentLine++;
                currentLineBreakIndex = i;
            }
        }
    }

}
