package de.jplag.java;

import de.jplag.Token;

import com.sun.source.tree.Tree;
import com.sun.source.util.SimpleTreeVisitor;

/**
 * Collects tokens extracted by an {@link Extractor} and passes them to the {@link Parser}.
 */
public class Collector extends SimpleTreeVisitor<Void, Context> {
    private final Extractor extractor;
    private final Parser parser;

    public Collector(Extractor extractor, Parser parser) {
        this.extractor = extractor;
        this.parser = parser;
    }

    @Override
    protected Void defaultAction(Tree node, Context ctx) {
        Token token = node.accept(this.extractor, ctx);
        if (token != null) {
            this.parser.add(token);
        }
        return null;
    }
}
