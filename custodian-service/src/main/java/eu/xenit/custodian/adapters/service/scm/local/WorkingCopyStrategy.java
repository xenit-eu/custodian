package eu.xenit.custodian.adapters.service.scm.local;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@FunctionalInterface
public interface WorkingCopyStrategy {

    WorkingCopyStrategy INPLACE = (Path input) -> input;
    WorkingCopyStrategy TEMPDIR = (Path input) -> Files.createTempDirectory(input.toFile().getName() + "-");

    Path getWorkingCopyLocation(Path input) throws IOException;

}
