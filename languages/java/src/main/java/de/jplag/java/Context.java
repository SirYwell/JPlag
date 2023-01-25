package de.jplag.java;

/**
 * The context in which a token might be extracted.
 * @param role the current role of the element.
 * @param moment the current moment of visitation.
 */
public record Context(Role role, Moment moment) {
}
