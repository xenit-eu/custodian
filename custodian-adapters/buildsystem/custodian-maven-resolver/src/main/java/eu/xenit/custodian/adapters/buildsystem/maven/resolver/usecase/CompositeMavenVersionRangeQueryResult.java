package eu.xenit.custodian.adapters.buildsystem.maven.resolver.usecase;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompositeMavenVersionRangeQueryResult implements VersionRangeQueryResult {

    private final Collection<VersionRangeQueryResult> collection;

    public CompositeMavenVersionRangeQueryResult(VersionRangeQueryResult... collection) {
        this(Arrays.asList(collection));
    }

    public CompositeMavenVersionRangeQueryResult(Collection<VersionRangeQueryResult> collection) {
        Objects.requireNonNull(collection, "Argument 'collection' is required");
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Argument 'collection' cannot be empty");
        }

        this.collection = collection;

        // verify the collections have the same version-spec
        var versionSpecs = collection.stream().map(VersionRangeQueryResult::getSpecification)
                .collect(Collectors.toSet());
        if (versionSpecs.size() > 1) {
            String msg = String.format("%s mismatch: %s", ResolverVersionSpecification.class.getSimpleName(),
                    versionSpecs.stream()
                            .map(ResolverVersionSpecification::getValue)
                            .collect(Collectors.joining(", ")));
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public Optional<ResolverArtifactVersion> getHighestVersion() {
        return this.versions().reduce((one, two) -> two);
    }

    @Override
    public Stream<ResolverArtifactVersion> versions() {
        return StreamUtils.intersect(this.collection.stream().map(VersionRangeQueryResult::versions));
    }

    @Override
    public ResolverVersionSpecification getSpecification() {
        return collection.stream()
                .findAny()
                .map(VersionRangeQueryResult::getSpecification)
                .orElseThrow();

    }

    static class StreamUtils {

        private static <T, U extends Stream<T>> Stream<T> intersect(Stream<U> stream) {
            final Iterator<U> iterator = stream.iterator();

            // Short circuit if we're intersecting 0 streams
            if (!iterator.hasNext()) {
                return Stream.empty();
            }

            // Create a hashset of the stream that retains insertion order
            final Set<T> result = iterator.next().collect(Collectors.toCollection(LinkedHashSet::new));

            // Intersect with every next stream using Set.retainAll
            while (iterator.hasNext()) {
                result.retainAll(iterator.next().collect(Collectors.toSet()));
            }

            return result.stream();
        }

    }


}
