package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.adapters.dependencies.DependencyResolution.DependencyResolutionBuilder;
import eu.xenit.custodian.sentinel.adapters.dependencies.DependencyResolution.DependencyResolutionState;
import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import java.util.Optional;
import java.util.stream.Stream;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ExternalModuleDependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.artifacts.SelfResolvingDependency;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.artifacts.result.DependencyResult;
import org.gradle.api.artifacts.result.ResolvedDependencyResult;
import org.gradle.api.artifacts.result.UnresolvedDependencyResult;
import org.gradle.api.internal.artifacts.result.ResolvedComponentResultInternal;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class DependenciesAnalyzer implements PartialAnalyzer<DependenciesContainer> {

    private static final Logger logger = Logging.getLogger(DependenciesAnalyzer.class);

    public DependenciesContainer analyze(Project project) {
        DependenciesContainer result = new DependenciesContainer();

        project.getConfigurations()
                .stream()

                .filter(config -> !config.getDependencies().isEmpty())
                // filter out configurations that we're not allowed to resolve
//                .filter(Configuration::isCanBeResolved)
//                .peek(config -> config.setCanBeResolved(true))

                // run analysis on each configuration
                .map(this::analyzeConfiguration)

                // collect results
                .forEach(result::add);

        return result;
    }

    ConfigurationDependenciesContainer analyzeConfiguration(Configuration configuration) {
        return analyzeConfiguration(configuration.getName(), configuration.getDependencies().stream());
    }

    ConfigurationDependenciesContainer analyzeConfiguration(String name, Stream<Dependency> dependencies) {
        return new ConfigurationDependenciesContainer(name, dependencies
                .peek(d -> logger.debug("-- [{}] - {}", name, d.toString()))
                // Filtering out all non-external-module dependencies
                .filter(ExternalModuleDependency.class::isInstance)
                .map(ExternalModuleDependency.class::cast)
                .map(DeclaredModuleDependency::create)
                .peek(dep -> logger.info("-- declared dependency: {}", dep.getId())));
    }

    //    ConfigurationDependenciesContainer analyzeIncomingDependencies(DependencySet declaredDependencies,
//            ResolvableDependencies incoming) {
//        return this.analyzeIncomingDependencies(declaredDependencies, incoming.getResolutionResult());
//    }

//    ConfigurationDependenciesContainer analyzeIncomingDependencies(DependencySet declaredDependencies,
//            ResolutionResult resolutionResult) {
//
//        ConfigurationDependenciesContainer dependencies = new ConfigurationDependenciesContainer();
//
//        declaredDependencies.stream()
//                .filter(dep -> {
//                    //  A SelfResolvingDependency is a Dependency which is able to resolve itself,
//                    // independent of a repository.
//                    // Examples:
//                    // * ProjectDependency
//                    // * gradleTestKit()
//                    // * file-collection
//                    if (dep instanceof SelfResolvingDependency) {
//                        logger.warn("Skipping self resolving dependency: group:{}, name:{} - isProject:{}",
//                                dep.getGroup(), dep.getName(), dep instanceof ProjectDependency);
//                        return false;
//                    }
//                    return true;
//                })
//                .map(AnalyzedDependency::create)
//                .peek(dep -> logger.info("-- incoming dependency: {}", dep.getId()))
//                .forEach(dependencies::add);
//
//        // Resolve dependencies
//        resolutionResult.allDependencies(dependencyResult -> {
//            Optional<AnalyzedDependency> analyzedDependency = asModuleComponentSelector(dependencyResult.getRequested())
//                    .map(ModuleComponentSelector::getModuleIdentifier)
//                    .map(dependencies::get);
//
//            if (!analyzedDependency.isPresent()) {
//                // Not a declared dependency - this is (probably?) a transitive dependency - skipping
//                return;
//            }
//
//            analyzedDependency.get().setResolution(getDependencyResolution(dependencyResult));
//        });
//
//        return dependencies;
//    }

    private DependencyResolution getDependencyResolution(DependencyResult dependencyResult) {
        Optional<ResolvedDependencyResult> resolved = Optional.of(dependencyResult)
                .filter(ResolvedDependencyResult.class::isInstance)
                .map(ResolvedDependencyResult.class::cast);

        Optional<UnresolvedDependencyResult> unresolved = Optional.of(dependencyResult)
                .filter(UnresolvedDependencyResult.class::isInstance)
                .map(UnresolvedDependencyResult.class::cast);

        DependencyResolutionBuilder builder = DependencyResolution.builder();

        if (resolved.isPresent()) {
            builder
                    .state(DependencyResolutionState.RESOLVED)
                    .selected(resolved.get().getSelected().getModuleVersion())
                    .reason(resolved.get().getSelected().getSelectionReason())
                    .repository(this.getRepositoryInternal(resolved.get()));


        } else if (unresolved.isPresent()) {
            builder
                    .state(DependencyResolutionState.FAILED)
                    .failure(unresolved.get().getFailure().getMessage());
        } else {
            throw new RuntimeException("Unexpected dependency state: " + dependencyResult.toString());
        }

        return builder.build();
    }

    private Optional<ModuleComponentSelector> asModuleComponentSelector(ComponentSelector selector) {
        // Note: in a component-selector, the required version can be an empty string!
        return Optional.of(selector)
                .filter(ModuleComponentSelector.class::isInstance)
                .map(ModuleComponentSelector.class::cast);
    }

    private String getRepositoryInternal(ResolvedDependencyResult resolvedDependencyResult) {
        return Optional.of(resolvedDependencyResult.getSelected())
                .filter(ResolvedComponentResultInternal.class::isInstance)
                .map(ResolvedComponentResultInternal.class::cast)
                .map(ResolvedComponentResultInternal::getRepositoryName)
                .orElse("");
    }
}
