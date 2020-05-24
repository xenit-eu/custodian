package eu.xenit.custodian.adapters.buildsystem.gradle.spi.sentinel.dto;

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleModuleDependency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dependency {

    private Dependency(String group, String artifact)
    {
        this(group, artifact, null);
    }

    private Dependency(String group, String artifact, String version) {
        this(group, artifact, version, null, null);
    }

    private Dependency(String group, String artifact, String version, String classifier, String type) {
        this.group = group;
        this.artifact = artifact;
        this.version = version;
        this.classifier = classifier;
        this.type = type;
    }

    private String group;
    private String artifact;
    private String version;
    private String classifier;
    private String type;

    private DependencyResolution resolution;

    public static Dependency withCoordinates(String group, String name, String version) {
        return new Dependency(group, name, version);
    }

    public static Dependency from(String notation) {
        String[] split = notation.split(":");
        if (split.length < 2) {
            throw new IllegalArgumentException(NOTATION_MSG);
        } else if (split.length == 2) {
            return new Dependency(split[0], split[1], null);
        } else if (split.length == 3) {
            return new Dependency(split[0], split[1], split[2]);
        } else {
            throw new IllegalArgumentException(NOTATION_MSG);
        }
    }

//    public static Dependency from(GradleModuleVersionIdentifier id) {
//        return new Dependency(id.getGroup(), id.getName(), id.getVersion().getValue());
//    }

    private static String NOTATION_MSG = "Expected notation <group>:<artifact>:<version>";

}
