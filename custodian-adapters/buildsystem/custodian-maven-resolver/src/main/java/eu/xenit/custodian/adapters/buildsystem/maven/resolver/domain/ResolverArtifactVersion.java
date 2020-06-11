package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface ResolverArtifactVersion extends Comparable<ResolverArtifactVersion> {

    String getValue();

    int getMajorVersion();
    int getMinorVersion();
    int getIncrementalVersion();

    String getQualifier();

    static ResolverArtifactVersion from(String value) {
        Objects.requireNonNull(value, "Argument 'value' is required");
        return new DefaultResolverArtifactVersion(value);
    }

    static ResolverArtifactVersion from(int... parts) {
        if (parts.length == 0) {
            throw new IllegalArgumentException("parts");
        }

        String version = IntStream.of(parts).mapToObj(Integer::toString).collect(Collectors.joining("."));
        return ResolverArtifactVersion.from(version);
    }
}

