package eu.xenit.gradle.sentinel.analyzer.model;

import java.util.Collection;
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
