package de.jplag.java.configurable.hierarchy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph.Builder;

/**
 * Parses a hierarchy from a text file. Format:
 * 
 * <pre>
 *     {@code
 *     CATEGORY_NAME:
 *       AST_ELEMENT_NAME
 *       SUB_CATEGORY_NAME:
 *         OTHER_AST_ELEMENT
 *       THIRD_AST_ELEMENT
 *     OTHER_TOP_LEVEL_CATEGORY:
 *       FOURTH_AST_ELEMENT
 *     }
 * </pre>
 */
@SuppressWarnings("UnstableApiUsage")
class HierarchyParser {

    private static final int INDENTATION_SPACES = 2;

    public Hierarchy parse(Path file) throws IOException {
        Builder<HierarchyNode> treeBuilder = GraphBuilder.directed().<HierarchyNode>immutable();
        List<String> lines = Files.readAllLines(file);
        Queue<String> queue = new ArrayDeque<>(lines);
        Category root = Hierarchy.ALL;
        while (!queue.isEmpty()) {
            parseCategoryOrTreeKind(queue, root, treeBuilder);
        }
        return new Hierarchy(treeBuilder.build());
    }

    private void parseCategoryOrTreeKind(Queue<String> lines, Category parent, Builder<HierarchyNode> result) {
        String element = lines.remove();
        int depth = indentationDepth(element);
        String cleanedName = clean(element);
        if (isCategory(element)) {
            Category category = new Category(cleanedName);
            result.putEdge(parent, category);
            parseCategory(lines, depth, category, result);
        } else {
            AstElement astElement = new AstElement(ExtendedKind.byName(cleanedName));
            result.putEdge(parent, astElement);
        }
    }

    private void parseCategory(Queue<String> lines, int depth, Category parent, Builder<HierarchyNode> result) {
        while (!lines.isEmpty()) {
            if (indentationDepth(lines.element()) <= depth) {
                return;
            }
            parseCategoryOrTreeKind(lines, parent, result);
        }
    }

    private String clean(String element) {
        String tmp = element.strip();
        if (isCategory(tmp)) {
            return tmp.substring(0, tmp.length() - 1);
        }
        return tmp;
    }

    private static boolean isCategory(String tmp) {
        return tmp.endsWith(":");
    }

    private int indentationDepth(String string) {
        int leadingSpaces = countLeadingSpaces(string);
        if (leadingSpaces % INDENTATION_SPACES != 0) {
            throw new IllegalArgumentException("Illegal indentation depth");
        }
        return leadingSpaces / INDENTATION_SPACES;
    }

    private int countLeadingSpaces(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ') {
                return i;
            }
        }
        throw new IllegalArgumentException("String only contains spaces");
    }
}
