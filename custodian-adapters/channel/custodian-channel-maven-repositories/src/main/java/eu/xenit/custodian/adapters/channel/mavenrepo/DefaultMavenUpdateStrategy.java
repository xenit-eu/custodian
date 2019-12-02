package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class DefaultMavenUpdateStrategy implements MavenDependencyUpdateStrategy {

    private List<MavenDependencyUpdatePlan> plans = new ArrayList<>();

    public DefaultMavenUpdateStrategy() {
        this(Arrays.asList(
                new DefaultMavenDependencyUpdatePlan("minor", MavenVersionRangeCalculator::getMinorUpdateVersionRange),
                new DefaultMavenDependencyUpdatePlan("major", MavenVersionRangeCalculator::getMajorUpdateVersionRange),
                new DefaultMavenDependencyUpdatePlan("incremental",
                        MavenVersionRangeCalculator::getIncrementalVersionRange)
        ));
    }

    public DefaultMavenUpdateStrategy(Collection<MavenDependencyUpdatePlan> plans) {
        this.plans.addAll(plans);
    }

    @Override
    public Stream<MavenDependencyUpdatePlan> options(MavenArtifactSpecificationDescriptor dependency) {
        return this.plans.stream();
    }
}
