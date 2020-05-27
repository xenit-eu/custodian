package eu.xenit.custodian.domain.usecases.updates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Patch {

    Path getFile();

    String getOriginal();
    String getPatched();

    // type ? modified obviously, but what about file added, deleted ?
    // get diff / delta's ?!

    static Patch from(Path path, String original) throws IOException {
        String patched = Files.readString(path);
        return new DefaultPatch(path, original, patched);
    }

}
