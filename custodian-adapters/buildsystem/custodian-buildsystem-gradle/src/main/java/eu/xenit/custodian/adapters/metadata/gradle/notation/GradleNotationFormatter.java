package eu.xenit.custodian.adapters.metadata.gradle.notation;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersionIdentifier;

public class GradleNotationFormatter {

    public static String format(MavenModuleVersionIdentifier id) {
        return String.format("%s:%s:%s", id.getGroup(), id.getName(), id.getVersion().getValue());
    }
}
