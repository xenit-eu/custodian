package eu.xenit.custodian.sentinel.adapters.repositories;

import eu.xenit.custodian.sentinel.domain.AnalysisContentPart;
import eu.xenit.custodian.sentinel.domain.ItemContainer;
import java.util.LinkedHashMap;
import java.util.Map;

public class RepositoriesContainer extends ItemContainer<String, RepositoryResult> implements AnalysisContentPart {

    public RepositoriesContainer()
    {
        this(new LinkedHashMap<>());
    }

    public RepositoriesContainer(Map<String, RepositoryResult> repositories) {
        super(repositories);
    }

    @Override
    public String getContributionName() {
        return RepositoriesAnalysisContributor.NAME;
    }
}
