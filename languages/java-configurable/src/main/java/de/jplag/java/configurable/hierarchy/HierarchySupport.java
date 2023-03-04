package de.jplag.java.configurable.hierarchy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

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
}
