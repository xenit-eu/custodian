package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.DependenciesAssert;
import org.assertj.core.api.AbstractStringAssert;

public class DependenciesFileAssert extends AbstractStringAssert<DependenciesFileAssert> implements DependenciesAssert {

    private DependenciesFileAssert(String content) {
        super(content, DependenciesFileAssert.class);
    }

    // TODO there are many many many more notations possible ...
    public DependenciesFileAssert hasDependency(String configuration, String dependency) {
        return this.contains(configuration + " \"" + dependency + "\"");
    }

    public DependenciesFileAssert hasDependency(GradleModuleDependency dependency) {
        return this.hasDependency(dependency.getTargetConfiguration(),
                String.format("%s:%s:%s", dependency.getGroup(), dependency.getName(), dependency.getVersion()));
    }

    public static DependenciesFileAssert from(String gradleBuildContent) {
        String dependencies = GradleBuildParser.extractSection("dependencies", gradleBuildContent);
        return new DependenciesFileAssert(dependencies);
    }


}
