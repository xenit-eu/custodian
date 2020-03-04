package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.domain.buildsystem.ModuleDependency;
import eu.xenit.custodian.domain.buildsystem.Repository;
import eu.xenit.custodian.domain.buildsystem.VersionSpecification;
import java.util.Collections;
import java.util.Set;

public interface MavenModuleDependency extends MavenDependency, ModuleDependency,
        MavenArtifactSpecificationProvider, MavenArtifactSpecificationDescriptor {

    String SCOPE_COMPILE = "compile";

    default MavenArtifactSpecificationDescriptor getMavenArtifactsDescriptor() {
        return this;
    }

    static MavenModuleDependency from(MavenModuleIdentifier moduleId, MavenVersionSpecification version, String scope) {
        return new DefaultMavenModuleDependency(moduleId, version, "jar", null, scope, Collections.emptyList(),
                Boolean.FALSE.toString());
    }

    static MavenModuleDependency from(String groupId, String artifactId, String version) {
        return from(groupId, artifactId, version, SCOPE_COMPILE);
    }

    static MavenModuleDependency from(String groupId, String artifactId, String version, String scope){
        return from(MavenModuleIdentifier.from(groupId, artifactId), MavenVersionSpecification.from(version), scope);
    }

}
