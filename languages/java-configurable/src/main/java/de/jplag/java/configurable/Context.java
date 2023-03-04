package de.jplag.java.configurable;

import de.jplag.java.configurable.hierarchy.ExtendedKind;

import com.sun.source.tree.Tree;

/**
 * The context in which a token might be extracted.
 * @param tree
 * @param kind the kind of the ast element
 * @param moment the current moment of visitation.
 */
public record Context(Tree tree, ExtendedKind kind, Moment moment) {
}
