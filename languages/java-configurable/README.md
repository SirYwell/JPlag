# JPlag Java language frontend

A configurable language module for Java 17.

### java specification compatibility

The language module supports up to Java 17. Preview features might not be fully supported.

### Token Extraction

The token extraction can be managed via a simple text based file.

`src/main/resources/example.txt` contains a basic example of how such file can look like.
The file currently is hardcoded in `JavacAdapter`.

### Usage

The language module can't be used outside the IDE/via JUnit currently.
