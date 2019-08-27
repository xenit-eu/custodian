package eu.xenit.gradle.sentinel.analyzer.model;

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
