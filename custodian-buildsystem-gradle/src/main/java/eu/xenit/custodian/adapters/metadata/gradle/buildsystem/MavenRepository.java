package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

public class MavenRepository implements GradleRepository {

    private final String id;
    private final String url;

    MavenRepository(String id, String url) {
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
