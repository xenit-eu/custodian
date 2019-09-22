package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import java.util.stream.Collectors;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class ConfigurationsAnalyzer implements PartialAnalyzer<ConfigurationContainer> {

    private static final Logger logger = Logging.getLogger(ConfigurationsAnalyzer.class);

    private DependenciesAnalyzer dependenciesAnalyzer = new DependenciesAnalyzer();

    public ConfigurationContainer analyze(Project project) {
        ConfigurationContainer result = new ConfigurationContainer();

        project.getConfigurations()
                .stream()

                // filter out configurations that we're not allowed to resovle
                .filter(Configuration::isCanBeResolved)

                // run analysis on each configuration
                .map(this::analyzeConfiguration)

                // collect results
                .forEach(result::add);

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
