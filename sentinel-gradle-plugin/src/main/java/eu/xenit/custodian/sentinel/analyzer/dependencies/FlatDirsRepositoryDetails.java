package eu.xenit.custodian.sentinel.analyzer.dependencies;

import java.util.Collection;
import lombok.Builder;

@Builder
public class FlatDirsRepositoryDetails {
    private Collection<String> dirs;
}
