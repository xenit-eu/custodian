package eu.xenit.custodian.adapters.buildsystem.maven;

public class DefaultMavenModuleVersionIdentifier implements MavenModuleVersionIdentifier {

    private final MavenModuleIdentifier module;
    private final MavenModuleVersion version;

    DefaultMavenModuleVersionIdentifier(String group, String name, String version) {
        this(MavenModuleIdentifier.from(group, name), MavenModuleVersion.from(version));
    }

    DefaultMavenModuleVersionIdentifier(MavenModuleIdentifier module, MavenModuleVersion version) {
        this.module = module;
        this.version = version;
    }

    @Override
    public MavenModuleIdentifier getModuleId() {
        return this.module;
    }

    @Override
    public MavenModuleVersion getVersion() {
        return this.version;
    }
}
