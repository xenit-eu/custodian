package eu.xenit.custodian.domain.changes;

import java.nio.file.Path;

public class DefaultPatch implements Patch {

    private final Path file;
    private final String original;
    private final String patched;

    DefaultPatch(Path file, String original, String patched) {
        this.file = file;
        this.original = original;
        this.patched = patched;
    }

    @Override
    public Path getFile() {
        return null;
    }

    @Override
    public String getOriginal() {
        return null;
    }

    @Override
    public String getPatched() {
        return null;
    }
}
