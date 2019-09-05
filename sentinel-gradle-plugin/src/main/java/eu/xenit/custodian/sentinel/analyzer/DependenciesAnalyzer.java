package eu.xenit.custodian.sentinel.analyzer;

import eu.xenit.custodian.sentinel.analyzer.model.AnalyzedDependency;
import eu.xenit.custodian.sentinel.analyzer.model.DependencyResolution;
import eu.xenit.custodian.sentinel.analyzer.model.DependencyResolution.DependencyResolutionBuilder;
import eu.xenit.custodian.sentinel.analyzer.model.DependencyResolution.DependencyResolutionState;
import eu.xenit.custodian.sentinel.analyzer.model.DependencyContainer;

import java.util.Optional;
import java.util.stream.Collectors;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.artifacts.result.DependencyResult;
import org.gradle.api.artifacts.result.ResolutionResult;
import org.gradle.api.artifacts.result.ResolvedDependencyResult;
import org.gradle.api.artifacts.result.UnresolvedDependencyResult;
import org.gradle.api.internal.artifacts.result.ResolvedComponentResultInternal;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

class DependenciesAnalyzer {

    private static final Logger logger = Logging.getLogger(DependenciesAnalyzer.class);

    DependencyContainer analyze(Configuration configuration) {

        logger.info("Collecting dependencies from configuration '{}' - can be resolved: {}", configuration.getName(),
                configuration.isCanBeResolved());
        ResolvableDependencies incoming = configuration.getIncoming();

        // Early exit is there are no incoming dependendencies anyway
        if (incoming.getDependencies().isEmpty()) {
            return new DependencyContainer();
        }
        return analyzeIncomingDependencies(incoming);

    }

    DependencyContainer analyzeIncomingDependencies(ResolvableDependencies incoming) {
        return this.analyzeIncomingDependencies(incoming.getDependencies(), incoming.getResolutionResult());
    }

    DependencyContainer analyzeIncomingDependencies(DependencySet incomingDependencies,
            ResolutionResult resolutionResult) {

        DependencyContainer dependencies = new DependencyContainer();

        // Collect all incoming dependenciers
//        incomingDependencies.forEach(incomingDep -> {
//            AnalyzedDependency dep = AnalyzedDependency.create(incomingDep);
//            dependencies.add(dep);
//            logger.info("-- incoming dependency: {}", dep.getId());
//        });

        incomingDependencies.stream()
                .map(AnalyzedDependency::create)
                .peek(dep -> logger.info("-- incoming dependency: {}", dep.getId()))
                .forEach(dependencies::add);

        // Resolve dependencies
        resolutionResult.allDependencies(dependencyResult -> {
            Optional<AnalyzedDependency> analyzedDependency = asModuleComponentSelector(dependencyResult.getRequested())
                    .map(ModuleComponentSelector::getModuleIdentifier)
                    .map(dependencies::get);

            if (!analyzedDependency.isPresent()) {
                // Not a declared dependency - this is (probably?) a transitive dependency - skipping
                return;
            }

            analyzedDependency.get().setResolution(getDependencyResolution(dependencyResult));
        });

        return dependencies;
    }

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
