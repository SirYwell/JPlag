package de.jplag.java.configurable.hierarchy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

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
class HierarchyParser {

    private static final int INDENTATION_SPACES = 2;

    public Map<String, HierarchyNode> parse(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        Queue<String> queue = new ArrayDeque<>(lines);
        Category root = new Category("ALL", null);
        List<HierarchyNode> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            parseCategoryOrTreeKind(queue, root, result);
        }
        return result.stream().collect(Collectors.toMap(HierarchyNode::name, v -> v));
    }

    private void parseCategoryOrTreeKind(Queue<String> lines, Category parent, List<HierarchyNode> result) {
        String element = lines.remove();
        int depth = indentationDepth(element);
        String cleanedName = clean(element);
        if (isCategory(element)) {
            Category category = new Category(cleanedName, parent);
            result.add(category);
            parseCategory(lines, depth, category, result);
        } else {
            AstElement astElement = new AstElement(ExtendedKind.byName(cleanedName), parent);
            result.add(astElement);
        }
    }

    private void parseCategory(Queue<String> lines, int depth, Category parent, List<HierarchyNode> result) {
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

    public static void main(String[] args) throws IOException {
        Map<String, HierarchyNode> parse = new HierarchyParser().parse(Path.of("languages/java-configurable/src/main/resources/config.txt"));
        int i = 3;
    }
}
