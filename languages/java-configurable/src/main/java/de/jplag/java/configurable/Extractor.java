package de.jplag.java.configurable;

import de.jplag.Token;

@FunctionalInterface
public interface Extractor {

    Token extract(Context context, TokenFactory factory);
}
