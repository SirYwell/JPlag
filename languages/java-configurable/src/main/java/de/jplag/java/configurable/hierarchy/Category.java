package de.jplag.java.configurable.hierarchy;

import java.util.Optional;

/**
 * A branch node in a hierarchy.
 * @param name the name of the category.
 * @param optionalParent the parent if existent, or null.
 */
public record Category(String name, HierarchyNode optionalParent) implements HierarchyNode {

    @Override
    public Optional<HierarchyNode> parent() {
        return Optional.ofNullable(optionalParent());
    }
}
