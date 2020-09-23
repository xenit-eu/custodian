package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.notation.GradleNotationFormatter;
import org.assertj.core.api.AbstractStringAssert;

public class DependenciesAssert extends AbstractStringAssert<DependenciesAssert> {

    private DependenciesAssert(String content) {
        super(content, DependenciesAssert.class);
    }

    // TODO there are many many many more notations possible ...
    public DependenciesAssert hasDependency(String configuration, String dependency) {
        return this.contains(configuration + " \"" + dependency + "\"");
    }

    public DependenciesAssert hasDependency(GradleModuleDependency dependency) {
        return this.hasDependency(dependency.getTargetConfiguration(),
                String.format("%s:%s:%s", dependency.getGroup(), dependency.getName(), dependency.getVersion()));
    }

    public static DependenciesAssert from(String gradleBuildContent) {
        String dependencies = GradleBuildParser.extractSection("dependencies", gradleBuildContent);
        return new DependenciesAssert(dependencies);
    }


}
