package eu.xenit.custodian.gradle.sentinel.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultRepositoriesContainer implements RepositoriesContainer {

    private List<RepositoryModel> repositories = new ArrayList<>();

    public void addMavenRepository(String url, String name) {
        this.repositories.add(new MavenRepositoryModel(url, name));
    }

    @Override
    public List<RepositoryModel> all() {
        return Collections.unmodifiableList(this.repositories);
    }
}
