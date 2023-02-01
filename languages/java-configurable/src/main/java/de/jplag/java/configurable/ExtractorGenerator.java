package de.jplag.java.configurable;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;

/**
 * A functional interface that creates an {@link Extractor}.
 */
@FunctionalInterface
public interface ExtractorGenerator {
    Extractor generate(CompilationUnitTree compilationUnit, Trees trees);
}
