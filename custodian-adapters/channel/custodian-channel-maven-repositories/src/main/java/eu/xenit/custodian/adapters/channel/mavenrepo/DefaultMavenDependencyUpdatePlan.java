package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.CompositeMavenVersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationDescriptor;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;
import eu.xenit.custodian.adapters.channel.mavenrepo.MavenDependencyUpdateStrategy.MavenDependencyUpdatePlan;
import eu.xenit.custodian.adapters.channel.mavenrepo.MavenDependencyUpdateStrategy.MavenDependencyUpdateProposal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultMavenDependencyUpdatePlan implements MavenDependencyUpdatePlan {

    private final String name;
    private final Function<MavenModuleVersion, MavenVersionSpecification> func;

    public DefaultMavenDependencyUpdatePlan(String name, Function<MavenModuleVersion, MavenVersionSpecification> func) {
        Objects.requireNonNull(name, "Argument 'name' is required");
        Objects.requireNonNull(func, "Argument 'func' is required");

        this.name = name;
        this.func = func;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MavenVersionSpecification getVersionSpecification(MavenModuleVersion currentVersion) {
        return this.func.apply(currentVersion);
    }

    @Override
    public MavenDependencyUpdateProposal execute(MavenArtifactResolver resolver,
            MavenArtifactSpecificationDescriptor artifact) {
        // Get the current (pinned) version
        Optional<MavenModuleVersion> optionalPinnedVersion = artifact.getVersionSpec().asPinnedVersion();
        if (!optionalPinnedVersion.isPresent()) {
            throw new IllegalArgumentException("artifact has non-pinned version");
        }
        MavenModuleVersion currentVersion = optionalPinnedVersion.get();

        // Get the version spec to see we can find updates
        MavenVersionSpecification versionSpec = this.getVersionSpecification(currentVersion);

        MavenVersionRangeQueryResult queryResult = getDependencyUpdate(resolver, artifact, versionSpec);

        return new DefaultMavenDependencyUpdateProposal(this, queryResult);


    }

    private static MavenVersionRangeQueryResult getDependencyUpdate(MavenArtifactResolver resolver,
            MavenArtifactSpecificationDescriptor artifact,
            MavenVersionSpecification versionSpec) {

        // If a module dependency has multiple artifacts configured (gradle only, not supported by maven pom.xml),
        // we should get the latest version that both artifacts have in common
        List<MavenVersionRangeQueryResult> collect = artifact
                .getArtifactSpecs()
                .stream()
                .map(spec -> spec.customize(config -> {
                    config.setVersionSpec(versionSpec);
                }))
                .map(resolver::resolve)
                .collect(Collectors.toList());

        if (collect.size() == 1) {
            return collect.get(0);
        }

        return new CompositeMavenVersionRangeQueryResult(versionSpec, collect);
    }
}
