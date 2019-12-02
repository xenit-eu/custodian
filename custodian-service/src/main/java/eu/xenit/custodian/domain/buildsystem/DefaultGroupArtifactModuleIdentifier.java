package eu.xenit.custodian.domain.buildsystem;

import java.util.Objects;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class DefaultGroupArtifactModuleIdentifier implements GroupArtifactModuleIdentifier {

    private final String group;
    private final String name;

    DefaultGroupArtifactModuleIdentifier(String group, String name)
    {
        Objects.requireNonNull(name, "name must not be null");

        this.group = group != null ? group : "";
        this.name = name;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return this.getGroup() + ":" + this.getName();
    }
}
