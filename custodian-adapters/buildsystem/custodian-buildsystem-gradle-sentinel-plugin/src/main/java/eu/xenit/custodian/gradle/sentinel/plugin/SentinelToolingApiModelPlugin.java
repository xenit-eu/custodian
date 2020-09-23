package eu.xenit.custodian.gradle.sentinel.plugin;

import eu.xenit.custodian.gradle.sentinel.model.GradleBuildModel;
import eu.xenit.custodian.gradle.sentinel.model.ProjectModel;
import eu.xenit.custodian.gradle.sentinel.model.DefaultGradleBuildModel;
import eu.xenit.custodian.gradle.sentinel.model.DefaultProjectModel;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentinelToolingApiModelPlugin implements Plugin<Project> {

    private final ToolingModelBuilderRegistry registry;

    @Inject
    public SentinelToolingApiModelPlugin(ToolingModelBuilderRegistry registry) {
        this.registry = registry;

    }

    @Override
    public void apply(Project target) {
        this.registry.register(new SentinelToolingModelBuilder());
    }

    private static class SentinelToolingModelBuilder implements ToolingModelBuilder {

        private static final Logger LOGGER = LoggerFactory.getLogger("FooPlugin");

        @Override
        public boolean canBuild(String modelName) {
            return modelName.equals(GradleBuildModel.class.getName());
        }

        @Override
        public Object buildAll(String modelName, Project project) {
            Map<String, ProjectModel> projectMap = this.buildAllProjectModels(project.getRootProject());

//            List<String> pluginClassNames = new ArrayList<String>();
//            for(Plugin plugin : project.getPlugins()) {
//                pluginClassNames.add(plugin.getClass().getName());
//            }

            return new DefaultGradleBuildModel(projectMap);
        }

        private Map<String, ProjectModel> buildAllProjectModels(Project project) {
            Map<String, ProjectModel> result = project.getChildProjects().values().stream()
                    .map(this::buildAllProjectModels)
                    .flatMap(map -> map.entrySet().stream())
                    .collect(Collectors.toMap(
                            Entry::getKey,
                            Entry::getValue));

            result.put(project.getPath(), this.buildProjectModel(project));

            return result;
        }

        public ProjectModel buildProjectModel(Project project) {
            DefaultProjectModel model = new DefaultProjectModel(project.getPath());

            model.setName(project.getName());
            model.setProjectDir(project.getProjectDir().toString());
            model.setBuildDir(project.getBuildDir().toString());
            model.setBuildFile(project.getBuildFile().toString());

            return model;
        }
    }
}
