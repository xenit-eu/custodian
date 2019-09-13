package eu.xenit.custodian.sentinel.adapters.dependencies;

import lombok.Builder;
import lombok.Getter;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.result.ComponentSelectionReason;

@Getter
@Builder
public class DependencyResolution {

    public enum DependencyResolutionState {
        RESOLVED,
        FAILED
    }

    private final DependencyResolutionState state;

    private final ModuleVersionIdentifier selected;
    private final ComponentSelectionReason reason;
    private final String repository;

    private final String failure;

}
