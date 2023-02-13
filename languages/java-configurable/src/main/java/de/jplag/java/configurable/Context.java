package de.jplag.java.configurable;

/**
 * The context in which a token might be extracted.
 * @param moment the current moment of visitation.
 */
public record Context(Moment moment) {
}
