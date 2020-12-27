package eu.xenit.custodian.gradle.sentinel.plugin;

import eu.xenit.custodian.gradle.sentinel.model.DefaultGradleBuildModel;
import eu.xenit.custodian.gradle.sentinel.model.DefaultProjectModel;
import eu.xenit.custodian.gradle.sentinel.model.GradleBuildModel;
import eu.xenit.custodian.gradle.sentinel.model.ProjectModel;
import eu.xenit.custodian.gradle.sentinel.plugin.service.PluginRegistrationLookup;
import java.io.IOException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.LenientConfiguration;
import org.gradle.api.artifacts.ResolvedDependency;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class SentinelToolingModelBuilder implements ToolingModelBuilder {

    private static final Logger log = LoggerFactory.getLogger(SentinelToolingModelBuilder.class);

    private final PluginRegistrationLookup pluginRegistrationLookup = new PluginRegistrationLookup();

    SentinelToolingModelBuilder() {
        this.pluginRegistrationLookup.loadPlugins(SentinelToolingApiModelPlugin.class.getClassLoader());
    }

    @Override
    public boolean canBuild(String modelName) {
        return modelName.equals(GradleBuildModel.class.getName());
    }

    @Override
    public Object buildAll(String modelName, Project project) {

        project.getPlugins().whenPluginAdded(p -> {
            log.warn("!! plugin added: " + p);
        });

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
                        Map.Entry::getKey,
                        Map.Entry::getValue));

        result.put(project.getPath(), this.buildProjectModel(project));

        return result;
    }

    public ProjectModel buildProjectModel(Project project) {
        DefaultProjectModel model = new DefaultProjectModel(project.getPath());

        model.setName(project.getName());
        model.setProjectDir(project.getProjectDir().toString());
        model.setBuildDir(project.getBuildDir().toString());
        model.setBuildFile(project.getBuildFile().toString());

//            model.getPlugins().addAll(project.getPlugins().stream().map(p -> new DefaultPluginModel(p.getClass().getName())));
        log.warn("PLUGINS: " + project.getPlugins().size());

//            project.getPluginManager().

        Configuration buildClasspath = project.getBuildscript().getConfigurations().getAt("classpath");
        LenientConfiguration lenientConfig = buildClasspath.getResolvedConfiguration().getLenientConfiguration();
        Set<ResolvedDependency> dependencies = lenientConfig.getFirstLevelModuleDependencies();


        log.warn("DEPENDENCIES: " + dependencies.size());
        dependencies.forEach(dep -> {
            log.warn(dep.getModuleGroup() + ":" + dep.getModuleName() + ":" + dep.getModuleVersion());
        });

        project.getPlugins().stream()
                .peek(plugin -> log.warn("looking up plugin: "+plugin.getClass().getName()))
                .map(plugin -> this.pluginRegistrationLookup.lookupPluginByClass(plugin.getClass()))
                .forEach(pluginRegistration -> {
                    if (pluginRegistration.isPresent()) {

                        model.getPlugins().add(pluginRegistration.get().getId());
                    } else {
                        log.warn("plugin optional was empty");
                    }
                });

        return model;
    }
}
