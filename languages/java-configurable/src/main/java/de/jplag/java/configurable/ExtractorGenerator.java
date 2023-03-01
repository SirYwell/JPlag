package de.jplag.java.configurable;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;

/**
 * A functional interface that creates an {@link TokenFactory}.
 */
public interface ExtractorGenerator {
    Extractor generate(CompilationUnitTree compilationUnit, Trees trees);

    /**
     * {@return a String describing the generator using [a-zA-Z]}
     */
    String name();
}
