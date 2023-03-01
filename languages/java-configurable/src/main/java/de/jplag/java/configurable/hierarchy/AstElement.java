package de.jplag.java.configurable.hierarchy;

import java.util.Optional;

/**
 * A leaf node in a hierarchy.
 * @param kind the kind represented by this node.
 * @param optionalParent the parent if existent, or null.
 */
public record AstElement(ExtendedKind kind, HierarchyNode optionalParent) implements HierarchyNode {
    @Override
    public Optional<HierarchyNode> parent() {
        return Optional.ofNullable(optionalParent());
    }

    @Override
    public String name() {
        return kind().name();
    }
}
