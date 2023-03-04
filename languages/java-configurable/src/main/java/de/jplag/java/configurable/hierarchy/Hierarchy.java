package de.jplag.java.configurable.hierarchy;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.graph.ImmutableGraph;

@SuppressWarnings("UnstableApiUsage")
public class Hierarchy {
    private final ImmutableGraph<HierarchyNode> tree;
    private final Map<ExtendedKind, AstElement> kindMap;

    Hierarchy(ImmutableGraph<HierarchyNode> tree) {
        this.tree = tree;
        this.kindMap = buildKindMap(tree);
    }

    private static Map<ExtendedKind, AstElement> buildKindMap(ImmutableGraph<HierarchyNode> tree) {
        return tree.nodes().stream().filter(node -> node instanceof AstElement).map(node -> (AstElement) node)
                .collect(Collectors.toMap(AstElement::kind, e -> e));
    }

    public Optional<AstElement> element(ExtendedKind kind) {
        return Optional.ofNullable(this.kindMap.get(kind));
    }

    public Collection<HierarchyNode> successors(HierarchyNode hierarchyNode) {
        return this.tree.successors(hierarchyNode);
    }

    public Collection<HierarchyNode> predecessors(HierarchyNode hierarchyNode) {
        return this.tree.predecessors(hierarchyNode);
    }
}
