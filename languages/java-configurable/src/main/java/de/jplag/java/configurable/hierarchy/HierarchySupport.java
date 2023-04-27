package de.jplag.java.configurable.hierarchy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableGraph;

@SuppressWarnings("UnstableApiUsage")
public final class HierarchySupport {

    private static final String HIERARCHY_RESOURCE_PATH = "/hierarchy.txt";

    private HierarchySupport() {
        throw new AssertionError();
    }

    public static Hierarchy loadDefaultHierarchy() throws IOException {
        Path tempFile = Files.createTempFile("hierarchy", ".txt");
        try (InputStream inputStream = HierarchySupport.class.getResourceAsStream(HIERARCHY_RESOURCE_PATH);
                OutputStream outputStream = Files.newOutputStream(tempFile)) {
            Objects.requireNonNull(inputStream, "Missing resource: " + HIERARCHY_RESOURCE_PATH).transferTo(outputStream);
            return new HierarchyParser().parse(tempFile);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    public static String toTikzGraph(Hierarchy hierarchy) {
        ImmutableGraph<HierarchyNode> tree = hierarchy.tree();
        StringBuilder builder = new StringBuilder();
        int depth = depth(Hierarchy.ALL, tree, 0);
        builder.append("\\begin{tikzpicture}\n");

        Set<HierarchyNode> successors = tree.successors(Hierarchy.ALL);
        for (HierarchyNode successor : successors) {
            builder.append("\\node[rectangle,draw] {")
                    .append(escapeLaTeX(successor.name()))
                    .append("}\n");
            processSuccessors(builder, tree, successor, 1, depth);
        }

        builder.append("\\end{tikzpicture}\n");
        return builder.toString();
    }

    private static void processSuccessors(StringBuilder builder, ImmutableGraph<HierarchyNode> tree, HierarchyNode source, int depth, int maxDepth) {
        Set<HierarchyNode> successors = tree.successors(source);
        for (HierarchyNode outer : successors) {
            String indent = "\t".repeat(depth + 1);
            builder.append(indent)
                    .append("child { node[rectangle,draw] { ")
                    .append(escapeLaTeX(outer.name()))
                    .append(" }");
            if (tree.outDegree(outer) > 0) {
                builder.append("\n");
                processSuccessors(builder, tree, outer, depth + 1, maxDepth);
                builder.append(indent);
            }
            builder.append("}\n");
        }
    }

    private static String escapeLaTeX(String name) {
        return name.replace("_", "\\_");
    }

    private static int depth(HierarchyNode node, ImmutableGraph<HierarchyNode> tree, int depth) {
        Set<HierarchyNode> successors = tree.successors(node);
        return successors.stream().mapToInt(s -> depth(s, tree, depth + 1)).max().orElse(depth);
    }

    public static String toMermaidGraph(Hierarchy hierarchy) {
        StringBuilder builder = new StringBuilder("graph\n");
        String indent = "  ";
        for (EndpointPair<HierarchyNode> edge : hierarchy.tree().edges()) {
            if (edge.source() == Hierarchy.ALL) {
                continue;
            }
            builder.append(indent)
                    .append(edge.source().name())
                    .append(" --> ")
                    .append(edge.target().name())
                    .append("\n");
        }
        return builder.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(toMermaidGraph(loadDefaultHierarchy()));
    }
}
