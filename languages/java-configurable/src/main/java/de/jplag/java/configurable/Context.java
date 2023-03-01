package de.jplag.java.configurable;

import de.jplag.java.configurable.hierarchy.ExtendedKind;

/**
 * The context in which a token might be extracted.
 * @param kind the kind of the ast element
 * @param moment the current moment of visitation.
 */
public record Context(ExtendedKind kind, Moment moment) {
}
