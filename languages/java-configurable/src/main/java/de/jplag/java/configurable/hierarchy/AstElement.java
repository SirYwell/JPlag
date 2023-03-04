package de.jplag.java.configurable.hierarchy;

/**
 * A leaf node in a hierarchy.
 * @param kind the kind represented by this node.
 */
public record AstElement(ExtendedKind kind) implements HierarchyNode {

    @Override
    public String name() {
        return kind().name();
    }
}
