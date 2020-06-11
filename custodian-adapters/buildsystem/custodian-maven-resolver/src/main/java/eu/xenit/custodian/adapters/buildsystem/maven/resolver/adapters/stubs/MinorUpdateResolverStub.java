package eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.stubs;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.MavenResolverApi;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.DefaultVersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.MavenResolverPort;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import java.util.Collection;
import java.util.List;

/**
 * Stub implementation that looks at the lower bound of the requested version specification and attempts to bump the
 * minor version.
 *
 * If the bumped version does not match the version-specification, it returns an empty result.
 */
public class MinorUpdateResolverStub implements MavenResolverPort {


    @Override
    public VersionRangeQueryResult resolveVersionRange(Collection<ResolverMavenRepository> repositories,
            ResolverArtifactSpecification spec) {

        var versionSpec = ResolverVersionSpecification.from(spec.getVersion());
        var bounds = versionSpec.getBounds();

        if (bounds.size() == 0) {
            return new DefaultVersionRangeQueryResult(spec.getVersion());
        }

        var lowerBound = bounds.get(0).getLowerBound();
        if (lowerBound == null) {
            // the spec does not provide a lower bound, what do we expect to do now ?
            return new DefaultVersionRangeQueryResult(spec.getVersion());
        }

        // bump the minor version
        var newVersion = ResolverArtifactVersion
                .from(lowerBound.getMajorVersion() + "." + (lowerBound.getMinorVersion() + 1));

        if (!versionSpec.matches(newVersion)) {
            // the new version does not match the provided spec ?
            // is the upper bound lower than just a minor bump ?
            return new DefaultVersionRangeQueryResult(spec.getVersion());
        }

        return new DefaultVersionRangeQueryResult(versionSpec, List.of(newVersion));
    }
}
