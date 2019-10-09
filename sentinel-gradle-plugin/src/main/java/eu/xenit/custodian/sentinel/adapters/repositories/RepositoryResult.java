package eu.xenit.custodian.sentinel.adapters.repositories;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
@Builder
public class RepositoryResult {

    private String name;
    private String type;
    private URI url;
    private boolean authenticated;

    @Default
    private Collection<String> metadataSources = Collections.emptyList();

    private IvyRepositoryDetails ivy;
    private FlatDirsRepositoryDetails flatDirs;

}
