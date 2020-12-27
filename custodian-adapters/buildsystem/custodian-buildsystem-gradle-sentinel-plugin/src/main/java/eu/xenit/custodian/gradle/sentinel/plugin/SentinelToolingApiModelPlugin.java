package eu.xenit.custodian.gradle.sentinel.plugin;

import javax.inject.Inject;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentinelToolingApiModelPlugin implements Plugin<Project> {

    private static final Logger log = LoggerFactory.getLogger(SentinelToolingApiModelPlugin.class);

    private final ToolingModelBuilderRegistry registry;

    @Inject
    public SentinelToolingApiModelPlugin(ToolingModelBuilderRegistry registry) {
        this.registry = registry;

    }

    @Override
    public void apply(Project target) {
        this.registry.register(new SentinelToolingModelBuilder());
    }

}
