package eu.xenit.custodian.adapters.buildsystem.gradle.api;


public class DefaultMavenRepository implements MavenRepository {

    private final String id;
    private final String url;

    DefaultMavenRepository(String id, String url) {
        this.id = id;
        this.url = url;
    }

    @Override
    public String getId() {
        return this.id;
    }
    public String getUrl() {
        return url;
    }
}
