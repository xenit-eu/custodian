package eu.xenit.custodian.sentinel.adapters.repositories;

import java.util.Collection;
import lombok.Builder;

@Builder
public class FlatDirsRepositoryDetails {
    private Collection<String> dirs;
}
