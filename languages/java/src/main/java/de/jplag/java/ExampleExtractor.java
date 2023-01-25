package de.jplag.java;

import java.util.Optional;

import de.jplag.Token;
import de.jplag.TokenType;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;

/**
 * Extracts a start and end token for each tree element.
 */
public class ExampleExtractor extends Extractor {
    private final TokenTypeMatcher matcher = new TokenTypeMatcher();

    protected ExampleExtractor(CompilationUnitTree compilationUnit, Trees trees) {
        super(compilationUnit, trees);
    }

    @Override
    protected Token defaultAction(Tree node, Context context) {
        return switch (context.moment()) {
            case PRE -> createStart(matcher.match(node.getKind(), context).orElseThrow(), node, 1);
            case POST -> createEnd(matcher.match(node.getKind(), context).orElseThrow(), node, 1);
        };
    }

    public static class TokenTypeMatcher implements MatchRule {

        public Optional<? extends TokenType> match(Tree.Kind kind, Context context) {
            return Optional.of(new SimpleTokenType(nameGen(kind, context)));
        }

        private String nameGen(Tree.Kind kind, Context context) {
            return switch (context.moment()) {
                case PRE -> kind.name() + "{";
                case POST -> "}" + kind.name();
            };
        }

        record SimpleTokenType(String name) implements TokenType {

            @Override
            public String getDescription() {
                return name();
            }
        }
    }
}
