package de.jplag.java2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import de.jplag.ParsingException;
import de.jplag.java2.lexer.JavaKeyword;
import de.jplag.java2.lexer.JavaLexer;
import de.jplag.java2.lexer.KeywordToken;
import de.jplag.java2.lexer.OperatorToken;
import de.jplag.java2.lexer.SeparatorToken;
import de.jplag.java2.lexer.Token;

/**
 * Extracts tokens from Java code similar to {@code CPP.jj} in the C++ language module.
 */
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

    public void parse(JavaLexerAdapter parser) throws ParsingException {
        this.currentLine = 1;
        this.currentLineBreakIndex = 0;
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

    private void addTokens(List<Token> list, char[] content, JavaLexerAdapter parser) {
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
                            parser.add(JavaTokenType.ANNO_T, path.toFile(), currentLine, column, kw.end() - token.start());
                        }
                    }
                    case "{" -> parser.add(JavaTokenType.BLOCK_BEGIN, path.toFile(), currentLine, column, token.length());
                    case "}" -> parser.add(JavaTokenType.BLOCK_END, path.toFile(), currentLine, column, token.length());
                }
            } else if (token instanceof KeywordToken keywordToken && !BOOLEAN_LITERALS.contains(keywordToken.keyword())) {
                parser.add(new de.jplag.Token(keywordToken.keyword(), path.toFile(), currentLine, column, token.length()));
            } else if (token instanceof OperatorToken operatorToken) {
                String operator = operatorToken.operator();
                if (isAssignmentOperator(operator)) {
                    parser.add(JavaTokenType.ASSIGN, path.toFile(), currentLine, column, token.length());
                } else if (operator.equals("?")) {
                    parser.add(JavaTokenType.QUESTIONMARK, path.toFile(), currentLine, column, token.length());
                }
            }
            lastEnd = token.end();
        }
    }

    private boolean isAssignmentOperator(String operator) {
        return switch (operator) {
            case "==", "!=", "<=", ">=" -> false;
            case "++", "--" -> true;
            default -> operator.endsWith("=");
        };
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
