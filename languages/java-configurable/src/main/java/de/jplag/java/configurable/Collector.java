package de.jplag.java.configurable;

import de.jplag.Token;

/**
 * Collects tokens extracted by an {@link Extractor} and passes them to the {@link Parser}.
 */
public class Collector {
    private final Extractor extractor;
    private final TokenFactory tokenFactory;
    private final Parser parser;

    public Collector(Extractor extractor, TokenFactory tokenFactory, Parser parser) {
        this.extractor = extractor;
        this.tokenFactory = tokenFactory;
        this.parser = parser;
    }

    public void collect(Context context) {
        Token token = this.extractor.extract(context, this.tokenFactory);
        if (token != null) {
            this.parser.add(token);
        }
    }
}
