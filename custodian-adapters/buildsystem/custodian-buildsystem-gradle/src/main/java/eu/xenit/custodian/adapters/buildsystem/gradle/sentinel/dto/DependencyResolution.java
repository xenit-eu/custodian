package eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto;

import java.util.Collection;
import lombok.Data;

@Data
public class DependencyResolution {

    public enum DependencyResolutionState {
        RESOLVED,
        FAILED
    }

    private DependencyResolutionState state;
    private String selected;
    private String repository;
    private Collection<String> reason;

//     "state": "RESOLVED",
//             "selected": "com.fasterxml.jackson.core:jackson-databind:2.9.9.3",
//             "repository": "MavenRepo",
//             "reason": [
//             "requested",
//             "selected by rule"
//             ]

}
