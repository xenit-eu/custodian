package eu.xenit.custodian.adapters.buildsystem.maven;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompositeMavenVersionRangeQueryResult implements MavenVersionRangeQueryResult {

    private final Collection<MavenVersionRangeQueryResult> collection;
    private final MavenVersionSpecification specification;

    public CompositeMavenVersionRangeQueryResult(MavenVersionSpecification specification,
            Collection<MavenVersionRangeQueryResult> collection) {

        Objects.requireNonNull(specification, "Argument 'specification' is required");
        Objects.requireNonNull(collection, "Argument 'collection' is required");


        this.specification = specification;
        this.collection = collection;

        // check the collections have the same version-spec
        this.collection.forEach(queryResult -> {
            if (!queryResult.getSpecification().equals(this.specification)) {
                String msg = String.format("%s %s does not match %s", MavenVersionSpecification.class.getSimpleName(),
                        this.specification, queryResult.getSpecification());
                throw new IllegalArgumentException(msg);
            }
        });
    }

    public CompositeMavenVersionRangeQueryResult(MavenVersionSpecification specification,
            MavenVersionRangeQueryResult... collection) {
        this(specification, Arrays.asList(collection));
    }

    @Override
    public Optional<MavenModuleVersion> getHighestVersion() {
        return this.versions().reduce((one, two) -> two);
    }

    @Override
    public Stream<MavenModuleVersion> versions() {
        return StreamUtils.intersect(this.collection.stream().map(MavenVersionRangeQueryResult::versions));
    }

    @Override
    public MavenVersionSpecification getSpecification() {
        return this.specification;
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
