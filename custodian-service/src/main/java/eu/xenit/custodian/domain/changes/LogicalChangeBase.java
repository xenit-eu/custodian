package eu.xenit.custodian.domain.changes;

import eu.xenit.custodian.domain.buildsystem.Build;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class LogicalChangeBase implements LogicalChange {

    @Getter(AccessLevel.PROTECTED)
    private final Build build;

    protected LogicalChangeBase(Build build) {
        Objects.requireNonNull(build, "Argument 'build' is required");

        this.build = build;
    }

    @Override
    public abstract ChangeApplicationResult apply();
}
