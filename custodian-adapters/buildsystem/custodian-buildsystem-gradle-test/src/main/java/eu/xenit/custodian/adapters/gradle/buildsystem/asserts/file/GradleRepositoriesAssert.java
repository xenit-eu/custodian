package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleArtifactRepository.GradleRepositoryType;
import org.assertj.core.api.AbstractStringAssert;

public class GradleRepositoriesAssert extends AbstractStringAssert<GradleRepositoriesAssert> {

    private GradleRepositoriesAssert(String content) {
        super(content, GradleRepositoriesAssert.class);
    }

    public GradleRepositoriesAssert hasRepository(String url) {
        return this.contains("maven { url '" + url + "' }");
    }

    public GradleRepositoriesAssert hasMavenCentral() {
        return this.contains("mavenCentral()");
    }

    public static GradleRepositoriesAssert from(String gradleBuildContent) {
        String dependencies = GradleBuildParser.extractSection("repositories", gradleBuildContent);
        return new GradleRepositoriesAssert(dependencies);
    }

}
