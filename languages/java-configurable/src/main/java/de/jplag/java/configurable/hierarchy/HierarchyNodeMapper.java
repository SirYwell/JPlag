package de.jplag.java.configurable.hierarchy;

import java.io.IOException;
import java.util.Optional;

import de.jplag.java.configurable.Extractor;
import de.jplag.java.configurable.ExtractorGenerator;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;

public interface HierarchyNodeMapper {

    static ExtractorGenerator createExtractor(HierarchyNodeMapper nodeMapper) {
        return new ExtractorGenerator() {
            @Override
            public Extractor generate(CompilationUnitTree compilationUnit, Trees trees) {
                try {
                    return new HierarchyNodeMapperExtractor(HierarchySupport.loadDefaultHierarchy(), nodeMapper);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String name() {
                return nodeMapper.name();
            }
        };
    }

    Optional<HierarchyNode> map(HierarchyNode input);

    String name();
}
