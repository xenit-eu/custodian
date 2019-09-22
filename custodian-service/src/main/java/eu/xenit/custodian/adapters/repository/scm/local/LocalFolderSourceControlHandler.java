package eu.xenit.custodian.adapters.repository.scm.local;

import eu.xenit.custodian.domain.repository.scm.ProjectHandle;
import eu.xenit.custodian.domain.repository.scm.ProjectReference;
import eu.xenit.custodian.domain.repository.scm.SourceControlHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class LocalFolderSourceControlHandler implements SourceControlHandler {

    private final WorkingCopyStrategy strategy;

    public LocalFolderSourceControlHandler(WorkingCopyStrategy strategy)
    {
        this.strategy = strategy;
    }

    public LocalFolderSourceControlHandler() {
        this(WorkingCopyStrategy.INPLACE);
    }

    @Override
    public boolean canHandle(ProjectReference reference) {

        try {
            getPath(reference);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    @Override
    public ProjectHandle checkout(ProjectReference reference) throws IOException {
        Path input = getPath(reference);
        Path target = this.strategy.getWorkingCopyLocation(input);

        // copy input to output ?
        // who is responsible ?
        this.copyFolder(input, target);

        return new LocalProjectHandle(reference, target);
    }

    private static Path getPath(ProjectReference reference) {
        return Paths.get(reference.getUri());
    }

    private void copyFolder(Path source, Path target) throws IOException {
        if (Files.isSameFile(source, target)) {
            // Nothing to do here - using InPlaceWorkingCopyStrategy ?
            return;
        }

        try (Stream<Path> stream = Files.walk(source)) {
            stream.forEach((child) -> {
                try {
                    Path relative = source.relativize(child);
                    Path destination = target.resolve(relative);
                    if (!destination.toFile().isDirectory()) {
                        System.out.println("copying "+child+" to "+destination);
                        Files.copy(child, destination, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            });
        }
    }

    class LocalProjectHandle implements ProjectHandle {

        private final ProjectReference projectRef;
        private final Path location;

        LocalProjectHandle(ProjectReference projectRef, Path location)
        {
            this.projectRef = projectRef;
            this.location = location;
        }

        @Override
        public ProjectReference getReference() {
            return this.projectRef;
        }

        @Override
        public Path getLocation() {
            return this.location;
        }
    }

}
