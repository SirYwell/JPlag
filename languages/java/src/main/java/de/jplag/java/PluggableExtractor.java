package de.jplag.java;

import com.sun.source.tree.Tree;

/**
 * An adapter between token extraction and AST walking.
 */
public class PluggableExtractor extends OrderedTreeScanner {
    private final Collector collector;

    public PluggableExtractor(Collector collector) {
        this.collector = collector;
    }

    @Override
    protected void enter(Tree tree, Role role) {
        tree.accept(this.collector, new Context(role, Moment.PRE));
    }

    @Override
    protected void exit(Tree tree, Role role) {
        tree.accept(this.collector, new Context(role, Moment.POST));
    }

    @Override
    protected void enterAll(Iterable<? extends Tree> iterable, Role role) {

    }

    @Override
    protected void exitAll(Iterable<? extends Tree> iterable, Role role) {

    }
}
