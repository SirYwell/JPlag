package de.jplag.java;

import java.io.File;

import javax.tools.Diagnostic;

import de.jplag.Token;
import de.jplag.TokenType;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.Tree;
import com.sun.source.util.SimpleTreeVisitor;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.Trees;

/**
 * Base class for token extraction.
 */
public abstract class Extractor extends SimpleTreeVisitor<Token, Context> {
    private final File file;
    private final LineMap lineMap;
    private final CompilationUnitTree compilationUnit;
    private final SourcePositions sourcePositions;

    protected Extractor(CompilationUnitTree compilationUnit, Trees trees) {
        this.file = new File(compilationUnit.getSourceFile().toUri());
        this.lineMap = compilationUnit.getLineMap();
        this.compilationUnit = compilationUnit;
        this.sourcePositions = trees.getSourcePositions();
    }

    protected final Token createStart(TokenType type, Tree tree, int length) {
        long startPosition = this.sourcePositions.getStartPosition(this.compilationUnit, tree);
        if (startPosition == Diagnostic.NOPOS) {
            return null;
        }
        long column = lineMap.getColumnNumber(startPosition);
        return new Token(type, this.file, (int) lineMap.getLineNumber(startPosition), (int) column, length);
    }

    protected final Token createEnd(TokenType type, Tree tree, int length) {
        long startPosition = this.sourcePositions.getEndPosition(this.compilationUnit, tree);
        long column = lineMap.getColumnNumber(startPosition);
        return new Token(type, this.file, (int) lineMap.getLineNumber(startPosition), (int) column, length);
    }
}
