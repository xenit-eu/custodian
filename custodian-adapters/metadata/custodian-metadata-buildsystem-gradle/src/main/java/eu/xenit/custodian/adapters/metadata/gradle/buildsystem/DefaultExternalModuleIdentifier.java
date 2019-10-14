package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class DefaultExternalModuleIdentifier implements ExternalModuleIdentifier {

    private final String group;
    private final String name;
    private final String version;
    private final String classifier;
    private final String extension;

    DefaultExternalModuleIdentifier(String group, String name, String version, String classifier, String extension)
    {
        Objects.requireNonNull(group, "group must not be null");
        Objects.requireNonNull(name, "name must not be null");

        this.group = group;
        this.name = name;
        this.version = version;
        this.classifier = classifier;
        this.extension = extension;
    }

    @Nonnull
    @Override
    public String getGroup() {
        return this.group;
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }

    @Nullable
    @Override
    public String getVersion() {
        return this.version;
    }

    @Nullable
    @Override
    public String getClassifier() {
        return this.classifier;
    }

    @Nullable
    @Override
    public String getType() {
        return this.extension;
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
