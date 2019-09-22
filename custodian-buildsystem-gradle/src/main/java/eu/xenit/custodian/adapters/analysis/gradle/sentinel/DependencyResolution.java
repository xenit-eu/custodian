package eu.xenit.custodian.adapters.analysis.gradle.sentinel;

import java.util.Collection;
import lombok.Data;

@Data
public class DependencyResolution {

    private String state;
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
