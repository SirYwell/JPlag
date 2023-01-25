package de.jplag.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.jplag.Token;
import de.jplag.TokenType;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;

/**
 * Generates an {@link Extractor} from a text file.
 * Format:
 * {@code
 * <Tree.Kind> <Role> <Moment>: <TokenType>
 * }
 */
public class TextFileExtractorGenerator implements ExtractorGenerator {
    private final Path path;

    public TextFileExtractorGenerator(Path path) {
        this.path = path;
    }

    @Override
    public Extractor generate(CompilationUnitTree compilationUnit, Trees trees) {
        List<String> lines;
        try {
            lines = Files.readAllLines(this.path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        record TT(String name) implements TokenType {
            @Override
            public String getDescription() {
                return name();
            }
        }
        List<MatchRule> matchRules = lines.stream().map(s -> s.split(":")).map(arr -> (MatchRule) (kind, context) -> {
            String[] first = arr[0].split(" ");
            if (kind.name().equals(first[0]) && context.role().name().equals(first[1]) && context.moment().name().equals(first[2])) {
                return Optional.of(new TT(arr[1]));
            }
            return Optional.empty();
        }).toList();
        return new Extractor(compilationUnit, trees) {
            @Override
            protected Token defaultAction(Tree node, Context context) {
                return matchRules.stream() //
                        .map(rule -> rule.match(node.getKind(), context)) //
                        .filter(Optional::isPresent) //
                        .map(Optional::get) //
                        .map(tokenType -> switch (context.moment()) {
                    case PRE -> createStart(tokenType, node, 1);
                    case POST -> createEnd(tokenType, node, 1);
                }) //
                        .filter(Objects::nonNull) //
                        .findFirst().orElse(null);
            }
        };
    }
}
