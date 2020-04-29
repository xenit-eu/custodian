package eu.xenit.custodian.domain.changes;

import eu.xenit.custodian.ports.spi.build.BuildModification;
import java.util.stream.Stream;

public class BuildModificationResult implements ChangeApplicationResult {

    private final BuildModification modification;
    private final String description;

    public BuildModificationResult(BuildModification modification, String description) {

        this.modification = modification;
        this.description = description;
    }

    @Override
    public boolean isSuccess() {
        return this.modification.isModified();
    }

    @Override
    public String getShortDescription() {
        return this.description;
    }

    @Override
    public Stream<Patch> getPatches() {
        return this.modification.getPatches();
    }
}
