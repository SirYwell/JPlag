package de.jplag.java.configurable.hierarchy;

import java.util.HashMap;
import java.util.Map;

import com.sun.source.tree.Tree;

public sealed interface ExtendedKind {

    static ExtendedKind byTreeKind(Tree.Kind treeKind) {
        return byName(treeKind.name());
    }

    static ExtendedKind byName(String name) {
        // local cache to hide from outside
        class Cache {
            static final Map<String, Default> CACHE = new HashMap<>();
        }
        return switch (name) {
            case "TRY_WITH_RESOURCES" -> Extended.TRY_WITH_RESOURCES;
            case "FINALLY" -> Extended.FINALLY;
            case "ELSE" -> Extended.ELSE;
            // throws exception if not present without adding key to map
            default -> Cache.CACHE.computeIfAbsent(name, k -> new Default(Tree.Kind.valueOf(k)));
        };
    }

    String name();

    enum Extended implements ExtendedKind {
        TRY_WITH_RESOURCES,
        FINALLY,
        ELSE
    }

    record Default(Tree.Kind kind) implements ExtendedKind {
        @Override
        public String name() {
            return kind.name();
        }
    }
}
