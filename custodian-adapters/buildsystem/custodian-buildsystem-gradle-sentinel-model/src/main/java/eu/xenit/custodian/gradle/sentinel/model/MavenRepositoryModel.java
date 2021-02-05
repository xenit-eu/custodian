package eu.xenit.custodian.gradle.sentinel.model;

public class MavenRepositoryModel implements RepositoryModel {

    private final String url;
    private final String name;

    MavenRepositoryModel(String url, String name) {
        this.url = url;
        this.name = name;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
