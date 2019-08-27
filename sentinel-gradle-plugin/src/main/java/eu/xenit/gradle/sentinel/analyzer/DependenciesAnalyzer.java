package eu.xenit.gradle.sentinel.analyzer;

import eu.xenit.gradle.sentinel.analyzer.model.AnalyzedDependency;
import eu.xenit.gradle.sentinel.analyzer.model.DependencyContainer;
import eu.xenit.gradle.sentinel.analyzer.model.DependencyResolution;
import eu.xenit.gradle.sentinel.analyzer.model.DependencyResolution.DependencyResolutionBuilder;
import eu.xenit.gradle.sentinel.analyzer.model.DependencyResolution.DependencyResolutionState;
import java.util.Optional;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.artifacts.result.ResolutionResult;
import org.gradle.api.artifacts.result.ResolvedDependencyResult;
import org.gradle.api.artifacts.result.UnresolvedDependencyResult;
import org.gradle.api.internal.artifacts.result.ResolvedComponentResultInternal;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class DependenciesAnalyzer {

    private static final Logger logger = Logging.getLogger(DependenciesAnalyzer.class);

    public DependencyContainer analyze(Configuration configuration) {

        logger.info("Collecting dependencies from configuration '{}'", configuration.getName());

        DependencyContainer dependencies = new DependencyContainer();
        ResolvableDependencies incoming = configuration.getIncoming();

        // Early exit is there are no incoming dependendencies anyway
        if (incoming.getDependencies().isEmpty()) {
            return dependencies;
        }

        // Collect all incoming dependenciers
        incoming.getDependencies().forEach(incomingDep -> {
            AnalyzedDependency dep = AnalyzedDependency.create(incomingDep);
            dependencies.add(dep);
            logger.info("-- incoming dependency: {}", dep.getId());
        });

        // Resolve dependencies
        ResolutionResult resolutionResult = incoming.getResolutionResult();
        resolutionResult.allDependencies(dep -> {

            Optional<AnalyzedDependency> analyzedDependency = asModuleComponentSelector(dep.getRequested())
                    .map(ModuleComponentSelector::getModuleIdentifier)
                    .map(dependencies::get);

            if (!analyzedDependency.isPresent()) {
                // Not a declared dependency - this is (probably?) a transitive dependency - skipping
                return;
            }

            Optional<ResolvedDependencyResult> resolved = Optional.of(dep)
                    .filter(ResolvedDependencyResult.class::isInstance)
                    .map(ResolvedDependencyResult.class::cast);

            Optional<UnresolvedDependencyResult> unresolved = Optional.of(dep)
                    .filter(UnresolvedDependencyResult.class::isInstance)
                    .map(UnresolvedDependencyResult.class::cast);

            DependencyResolutionBuilder resolutionBuilder = DependencyResolution.builder();
            if (resolved.isPresent()) {
                resolutionBuilder
                        .state(DependencyResolutionState.RESOLVED)
                        .selected(resolved.get().getSelected().getModuleVersion())
                        .reason(resolved.get().getSelected().getSelectionReason())
                        .repository(this.getRepositoryInternal(resolved.get()));


            } else if (unresolved.isPresent()) {
                resolutionBuilder
                        .state(DependencyResolutionState.FAILED)
                        .failure(unresolved.get().getFailure().getMessage());
            } else {
                throw new RuntimeException("Unexpected dependency state: " + dep.toString());
            }

            analyzedDependency.get().setResolution(resolutionBuilder.build());

        });

        return dependencies;
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
