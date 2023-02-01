package de.jplag.java.configurable;

import java.util.Optional;

import de.jplag.TokenType;

import com.sun.source.tree.Tree;

/**
 * A functional interface that optionally returns a token type if a token should be extracted for the given tree kind
 * and context.
 */
@FunctionalInterface
public interface MatchRule {
    Optional<? extends TokenType> match(Tree.Kind kind, Context context);
}
