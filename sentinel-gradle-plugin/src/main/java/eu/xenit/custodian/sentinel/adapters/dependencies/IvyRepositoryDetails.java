package eu.xenit.custodian.sentinel.adapters.dependencies;

import java.util.Collection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IvyRepositoryDetails {
    private Collection<String> ivyPatterns;
    private Collection<String> artifactPatterns;
    private String layoutType;
    private boolean m2Compatible;
}
