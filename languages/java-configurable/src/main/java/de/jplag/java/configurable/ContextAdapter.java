package de.jplag.java.configurable;

import de.jplag.java.configurable.hierarchy.ExtendedKind;

import com.sun.source.tree.IfTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.source.util.SimpleTreeVisitor;

/**
 * An adapter between token extraction and AST walking.
 */
public class ContextAdapter extends OrderedTreeScanner {
    private static final ExtendedKindAdapter EXTENDED_KIND_ADAPTER = new ExtendedKindAdapter();
    private final Collector collector;

    public ContextAdapter(Collector collector) {
        this.collector = collector;
    }

    @Override
    protected void pre(Tree tree) {
        collect(tree, Moment.PRE);
    }

    @Override
    protected void middle(Tree tree) {
        collect(tree, Moment.MIDDLE);
    }

    @Override
    protected void post(Tree tree) {
        collect(tree, Moment.POST);
    }

    @Override
    protected void enterAll(Iterable<? extends Tree> iterable) {

    }

    @Override
    protected void exitAll(Iterable<? extends Tree> iterable) {

    }

    private void collect(Tree tree, Moment moment) {
        this.collector.collect(createContext(tree, moment));
    }

    private Context createContext(Tree tree, Moment moment) {
        return new Context(tree, tree.accept(EXTENDED_KIND_ADAPTER, moment), moment);
    }

    /**
     * Handles the transformation from Tree to ExtendedTreeKind. Also handles special cases for elements that don't have
     * their own Tree/TreeKind.
     */
    private static class ExtendedKindAdapter extends SimpleTreeVisitor<ExtendedKind, Moment> {
        @Override
        public ExtendedKind visitTry(TryTree node, Moment moment) {
            // MIDDLE is extracted at "finally" appearance
            if (moment == Moment.MIDDLE) {
                return ExtendedKind.Extended.FINALLY;
            }
            if (!node.getResources().isEmpty()) {
                return ExtendedKind.Extended.TRY_WITH_RESOURCES;
            }
            return super.visitTry(node, moment);
        }

        @Override
        public ExtendedKind visitIf(IfTree node, Moment moment) {
            // MIDDLE is extracted at "else" appearance
            if (moment == Moment.MIDDLE) {
                return ExtendedKind.Extended.ELSE;
            }
            return super.visitIf(node, moment);
        }

        @Override
        protected ExtendedKind defaultAction(Tree node, Moment moment) {
            return ExtendedKind.byName(node.getKind().name());
        }
    }
}
