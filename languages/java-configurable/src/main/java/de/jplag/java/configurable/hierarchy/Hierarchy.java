package de.jplag.java.configurable.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.graph.Graphs;
import com.google.common.graph.ImmutableGraph;

@SuppressWarnings("UnstableApiUsage")
public class Hierarchy {
    static final Category ALL = new Category("ALL");
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
        return Graphs.reachableNodes(this.tree, hierarchyNode);
    }

    public Collection<HierarchyNode> predecessors(HierarchyNode hierarchyNode) {
        List<HierarchyNode> predecessors = new ArrayList<>();
        HierarchyNode node = hierarchyNode;
        while (true) {
            Set<HierarchyNode> preds = this.tree.predecessors(node);
            if (preds.isEmpty()) {
                break;
            }
            HierarchyNode next = preds.iterator().next();// tree only has one pred
            predecessors.add(next);
            node = next;
        }
        return List.copyOf(predecessors);
    }

    ImmutableGraph<HierarchyNode> tree() {
        return this.tree;
    }
}
