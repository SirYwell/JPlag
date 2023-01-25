package de.jplag.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opentest4j.TestAbortedException;

import de.jplag.ParsingException;
import de.jplag.Token;
import de.jplag.TokenPrinter;

class LanguageTest {

    @Test
    void test(@TempDir Path path) {
        TokenResult result = extractFromString(path, "X", """
                public class X {
                    int i;
                    void m(String p) {
                        System.out.println(p);
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
        Language language = new Language();
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