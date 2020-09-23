package eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.PathResolver;
import java.nio.file.Path;

class BuildRootDirectory implements PathResolver {

    /**
     * The roto directory for the Grald build. The default is a fake in-memory filesystem root.
     *
     * That is because Gradle model we are building here, is possibly only conceptual (just the model)
     * and not materialized (no real files on disk)
     */
    private Path directory = createDefaultDirectory();

    @Override
    public Path resolve(String path) {
        return this.directory.resolve(path);
    }

    @Override
    public Path resolve(Path path) {
        return this.directory.resolve(path);
    }

    @Override
    public PathResolver newRelativeResolver(String path) {
        return null;
    }

    void setDirectory(Path directory) {
        this.directory = directory != null ? directory : createDefaultDirectory();
    }

    private static Path createDefaultDirectory() {
        return Jimfs.newFileSystem(Configuration.unix()).getPath("/");
    }
}
