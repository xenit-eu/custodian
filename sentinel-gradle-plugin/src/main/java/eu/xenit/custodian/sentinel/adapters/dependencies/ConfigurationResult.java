package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.domain.AnalysisContentPart;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
@Builder
public class ConfigurationResult implements AnalysisContentPart {

    @Getter
    private final String name;
    private final boolean transitive;

    @Default
    private final Set<String> extendsFrom = new HashSet<>();

    private final DependencyContainer dependencies;
}
