package eu.xenit.custodian.sentinel.analyzer.repositories;

import eu.xenit.custodian.sentinel.analyzer.ItemContainer;
import java.util.LinkedHashMap;
import java.util.Map;

public class RepositoriesContainer extends ItemContainer<String, RepositoryResult> {

    public RepositoriesContainer()
    {
        this(new LinkedHashMap<>());
    }

    public RepositoriesContainer(Map<String, RepositoryResult> repositories) {
        super(repositories, RepositoryResult::getName);
    }

}
