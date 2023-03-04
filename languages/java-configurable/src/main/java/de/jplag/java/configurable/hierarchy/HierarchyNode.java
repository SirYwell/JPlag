package de.jplag.java.configurable.hierarchy;

/**
 * An element of a hierarchy tree. Each element is associated with its parent (or null, if it is the root of the tree)
 */
public sealed interface HierarchyNode permits Category,AstElement {

    String name();
}
