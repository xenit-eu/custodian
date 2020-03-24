package eu.xenit.custodian.adapters.buildsystem.gradle.notation;

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleModuleDependency;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersionIdentifier;

public class GradleNotationFormatter {

    public static String format(MavenModuleVersionIdentifier id) {
        return String.format("%s:%s:%s", id.getGroup(), id.getName(), id.getVersion().getValue());
    }

    public static String format(GradleModuleDependency moduleDependency) {
        return String.format("%s '%s:%s:%s'", moduleDependency.getTargetConfiguration(),
                moduleDependency.getGroup(), moduleDependency.getName(), moduleDependency.getVersionSpec().getValue());
    }
}
