package eu.xenit.gradle.sentinel.analyzer;

import eu.xenit.gradle.sentinel.analyzer.model.AnalyzedDependency;
import eu.xenit.gradle.sentinel.analyzer.model.ConfigurationContainer;
import eu.xenit.gradle.sentinel.analyzer.model.ConfigurationResult;
import eu.xenit.gradle.sentinel.analyzer.model.DependencyContainer;
import eu.xenit.gradle.sentinel.analyzer.model.DependencyResolution;
import eu.xenit.gradle.sentinel.analyzer.model.DependencyResolution.DependencyResolutionBuilder;
import eu.xenit.gradle.sentinel.analyzer.model.DependencyResolution.DependencyResolutionState;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.artifacts.result.ResolutionResult;
import org.gradle.api.artifacts.result.ResolvedDependencyResult;
import org.gradle.api.artifacts.result.UnresolvedDependencyResult;
import org.gradle.api.internal.artifacts.result.ResolvedComponentResultInternal;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class ConfigurationsAnalyzer {

    private static final Logger logger = Logging.getLogger(ConfigurationsAnalyzer.class);

    private DependenciesAnalyzer dependenciesAnalyzer = new DependenciesAnalyzer();

    public ConfigurationContainer analyze(Project project) {
        ConfigurationContainer result = new ConfigurationContainer();

        project.getConfigurations().forEach(config -> {
            result.add(this.analyzeConfiguration(config));
        });

        return result;
    }

    private ConfigurationResult analyzeConfiguration(Configuration configuration) {

        DependencyContainer dependencies = dependenciesAnalyzer.analyze(configuration);

        ConfigurationResult result = ConfigurationResult.builder()
                .name(configuration.getName())
                .dependencies(dependencies)
                .transitive(configuration.isTransitive())
                .extendsFrom(
                        configuration.getExtendsFrom().stream().map(Configuration::getName).collect(Collectors.toSet()))
                .build();

        return result;
    }
}
