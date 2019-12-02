package eu.xenit.custodian.adapters.buildsystem.maven;

import java.util.Objects;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class DefaultMavenModuleIdentifier implements MavenModuleIdentifier {

    private final String group;
    private final String name;

    DefaultMavenModuleIdentifier(String group, String name)
    {
        Objects.requireNonNull(group, "group must not be null");
        Objects.requireNonNull(name, "name must not be null");

        this.group = group;
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

    @Override
    public String toString() {
        return this.getId();
    }
}
