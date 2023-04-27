package de.jplag.java.configurable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opentest4j.TestAbortedException;

import de.jplag.ParsingException;
import de.jplag.Token;
import de.jplag.TokenPrinter;
import de.jplag.java.configurable.hierarchy.HierarchyNode;
import de.jplag.java.configurable.hierarchy.HierarchyNodeMapper;

class LanguageTest {

    @Test
    void test(@TempDir Path path) {
        TokenResult result = extractFromString(path, "X", """
                public class X {
                    int i;
                    void m(String p) {
                        System.out.println(p);
                        a: synchronized(p) {
                           b:{
                           int i = (((5))) + (2 * 2);
                           }
                        }
                        enum A {
                            ;;;
                        }
                    }
                }
                """);
        System.out.println(TokenPrinter.printTokens(result.tokens(), result.file()));
    }

    @Test
    void testSwitch(@TempDir Path path) {
        TokenResult result = extractFromString(path, "X", """
                public class X {
                    int i;
                    void m(String p) {
                        switch (p) {
                            case "a" -> System.exit(0);
                            case "b" -> System.exit(1);
                        }
                        int i = switch (p) {
                            case "a" -> 0;
                            case "b" -> 1;
                            default -> -1;
                        };
                        switch (p) {
                            case "a": System.exit(0);
                            case "b": System.exit(1);
                        }
                        int j = switch (p) {
                            case "a": yield 0;
                            case "b": yield 1;
                            default: yield -1;
                        };
                    }
                }
                """);
        System.out.println(TokenPrinter.printTokens(result.tokens(), result.file()));
    }

    @Test
    void testIfElse(@TempDir Path path) {
        TokenResult result = extractFromString(path, "X", """
                public class X {
                    int i;
                    void m(boolean a, boolean b) {
                        if (a) {
                            a = !a;
                        } else if (b) {
                            b = !b;
                        } else {
                            b = a;
                        }
                    }
                }
                """);
        System.out.println(TokenPrinter.printTokens(result.tokens(), result.file()));
    }

    TokenResult extractFromString(@TempDir Path path, String name, String content) {
        Path filePath = path.resolve(name + ".java");
        try {
            Files.writeString(filePath, content);
        } catch (IOException e) {
            throw new TestAbortedException("Failed to write temp file", e);
        }
        ConfigurableJavaLanguage language = new ConfigurableJavaLanguage(HierarchyNodeMapper.createExtractor(new HierarchyNodeMapper() {
            @Override
            public Optional<HierarchyNode> map(HierarchyNode input) {
                return Optional.of(input);
            }

            @Override
            public String name() {
                return "test";
            }
        }));
        List<Token> tokens;
        try {
            tokens = language.parse(Set.of(filePath.toFile()));
        } catch (ParsingException e) {
            throw new TestAbortedException("Failed to extract tokens", e);
        }
        return new TokenResult(tokens, filePath.toFile());
    }

    record TokenResult(List<Token> tokens, File file) {
    }

}