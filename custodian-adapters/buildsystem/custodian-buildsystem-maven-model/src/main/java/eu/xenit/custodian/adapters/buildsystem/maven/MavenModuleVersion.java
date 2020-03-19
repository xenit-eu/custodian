package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.asserts.build.buildsystem.ModuleVersion;
import java.util.Objects;

public interface MavenModuleVersion extends ModuleVersion {

    String getValue();

    int getMajorVersion();
    int getMinorVersion();
    int getIncrementalVersion();
    int getBuildNumber();

    String getQualifier();

    static MavenModuleVersion from(String value) {
        Objects.requireNonNull(value, "Argument 'value' is required");
        return new DefaultMavenModuleVersion(value);
    }
}
