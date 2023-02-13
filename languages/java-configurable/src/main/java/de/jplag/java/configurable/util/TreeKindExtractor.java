package de.jplag.java.configurable.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.source.tree.Tree;

public class TreeKindExtractor {
    public static void main(String[] args) {
        Map<? extends Class<? extends Tree>, List<Tree.Kind>> categories = Arrays.stream(Tree.Kind.values())
                .filter(k -> k != Tree.Kind.OTHER) // invalid kind
                .collect(Collectors.groupingBy(Tree.Kind::asInterface));
        StringBuilder builder = new StringBuilder();
        for (var entry : categories.entrySet()) {
            builder.append("- ")
                    .append(entry.getKey().getSimpleName().replace("Tree", ""));
            for (Tree.Kind kind : entry.getValue()) {
                builder.append(System.lineSeparator())
                        .append("  - ")
                        .append(kind.name());
            }
            builder.append(System.lineSeparator());
        }
        System.out.println(builder);
    }
}
