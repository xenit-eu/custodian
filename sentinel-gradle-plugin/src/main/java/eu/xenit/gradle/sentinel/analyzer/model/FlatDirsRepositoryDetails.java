package eu.xenit.gradle.sentinel.analyzer.model;

import java.util.Collection;
import lombok.Builder;

@Builder
public class FlatDirsRepositoryDetails {
    private Collection<String> dirs;
}
