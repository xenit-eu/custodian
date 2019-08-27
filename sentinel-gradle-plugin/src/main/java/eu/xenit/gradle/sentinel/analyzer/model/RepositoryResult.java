package eu.xenit.gradle.sentinel.analyzer.model;

import java.net.URI;
import java.util.Collection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RepositoryResult {

    private String name;
    private String type;
    private URI url;
    private boolean authenticated;
    private Collection<String> metadataSources;

    private IvyRepositoryDetails ivy;
    private FlatDirsRepositoryDetails flatDirs;

}
