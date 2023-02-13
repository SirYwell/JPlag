package de.jplag.java.configurable;

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
    protected void pre(Tree tree) {
        tree.accept(this.collector, new Context(Moment.PRE));
    }

    @Override
    protected void middle(Tree tree) {
        tree.accept(this.collector, new Context(Moment.MIDDLE));
    }

    @Override
    protected void post(Tree tree) {
        tree.accept(this.collector, new Context(Moment.POST));
    }

    @Override
    protected void enterAll(Iterable<? extends Tree> iterable) {

    }

    @Override
    protected void exitAll(Iterable<? extends Tree> iterable) {

    }
}
