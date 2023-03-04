package de.jplag.java.configurable.hierarchy;

import static de.jplag.java.configurable.hierarchy.ExtendedKind.byTreeKind;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.jplag.Token;
import de.jplag.TokenType;
import de.jplag.java.configurable.Context;
import de.jplag.java.configurable.Extractor;
import de.jplag.java.configurable.TokenFactory;

import com.sun.source.tree.Tree.Kind;

class HierarchyNodeMapperExtractor implements Extractor {
    private static final Set<? extends ExtendedKind> EXTRACTION_PAIRS = Set.of(byTreeKind(Kind.CLASS), byTreeKind(Kind.INTERFACE),
            byTreeKind(Kind.ENUM), byTreeKind(Kind.ANNOTATION_TYPE), byTreeKind(Kind.RECORD), byTreeKind(Kind.LAMBDA_EXPRESSION),
            byTreeKind(Kind.METHOD), byTreeKind(Kind.ENHANCED_FOR_LOOP), byTreeKind(Kind.FOR_LOOP), byTreeKind(Kind.DO_WHILE_LOOP),
            byTreeKind(Kind.WHILE_LOOP), byTreeKind(Kind.CATCH), byTreeKind(Kind.SWITCH), byTreeKind(Kind.SWITCH_EXPRESSION), byTreeKind(Kind.IF));
    private static final Set<? extends ExtendedKind> EXTRACT_MIDDLE = EnumSet.of(ExtendedKind.Extended.ELSE, ExtendedKind.Extended.FINALLY);
    private final Hierarchy hierarchy;
    private final HierarchyNodeMapper nodeMapper;

    public HierarchyNodeMapperExtractor(Hierarchy hierarchy, HierarchyNodeMapper nodeMapper) {
        this.hierarchy = hierarchy;
        this.nodeMapper = nodeMapper;
    }

    @Override
    public Token extract(Context context, TokenFactory factory) {
        return switch (context.moment()) {
            case PRE -> {
                String suffix = "";
                if (EXTRACTION_PAIRS.contains(context.kind())) {
                    suffix = "{";
                }
                yield toTokenType(context, "", suffix).map(type -> factory.createStart(type, context.tree(), context.kind().name().length()))
                        .orElse(null);
            }
            case MIDDLE -> {
                if (EXTRACT_MIDDLE.contains(context.kind())) {
                    yield toTokenType(context, "", "").map(type -> factory.createStart(type, context.tree(), context.kind().name().length()))
                            .orElse(null);
                }
                yield null;
            }
            case POST -> {
                if (!EXTRACTION_PAIRS.contains(context.kind())) {
                    yield null; // don't extract end at all
                }
                String prefix = "}";
                yield toTokenType(context, prefix, "").map(type -> factory.createEnd(type, context.tree(), context.kind().name().length()))
                        .orElse(null);
            }
        };
    }

    private Optional<TokenType> toTokenType(Context context, String prefix, String suffix) {
        record SimpleTokenType(String name) implements TokenType {

            @Override
            public String getDescription() {
                return name();
            }

            private static final Map<String, TokenType> CACHE = new HashMap<>();
        }
        Optional<AstElement> element = this.hierarchy.element(context.kind());
        return element.flatMap(this.nodeMapper::map)
                .map(node -> SimpleTokenType.CACHE.computeIfAbsent(prefix + context.kind().name() + suffix, SimpleTokenType::new));
    }
}
